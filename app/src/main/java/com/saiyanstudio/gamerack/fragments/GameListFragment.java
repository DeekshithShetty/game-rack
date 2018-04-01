package com.saiyanstudio.gamerack.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyanstudio.gamerack.GameDetailsActivity;
import com.saiyanstudio.gamerack.R;
import com.saiyanstudio.gamerack.adapters.GamesAdapter;
import com.saiyanstudio.gamerack.common.Constants;
import com.saiyanstudio.gamerack.common.Utility;
import com.saiyanstudio.gamerack.common.WrapContentLinearLayoutManager;
import com.saiyanstudio.gamerack.handlers.DatabaseHandler;
import com.saiyanstudio.gamerack.listeners.RecyclerTouchListener;
import com.saiyanstudio.gamerack.messageevents.AddGameMessageEvent;
import com.saiyanstudio.gamerack.messageevents.GetAllGameMessageEvent;
import com.saiyanstudio.gamerack.messageevents.RemoveGameMessageEvent;
import com.saiyanstudio.gamerack.messageevents.UpdateExpansionMessageEvent;
import com.saiyanstudio.gamerack.messageevents.UpdateGameMessageEvent;
import com.saiyanstudio.gamerack.models.Expansion;
import com.saiyanstudio.gamerack.models.Game;
import com.saiyanstudio.gamerack.services.GameListLoadingService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by deekshith on 05-11-2017.
 */

public class GameListFragment extends Fragment {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.textView_no_games_msg) TextView noGamesMsg;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    FloatingActionButton fab;

    private String statusFilter;
    private List<Game> gamesList;
    private GamesAdapter gamesAdapter;
    private DatabaseHandler db;

    public GameListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewFragment = inflater.inflate(R.layout.fragment_game_list, container, false);

        fab = getActivity().findViewById(R.id.fab_add_game);

        ButterKnife.bind(this, viewFragment);

        db = new DatabaseHandler(getActivity());

        // Get the filter string from viewpager
        Bundle bundle = getArguments();
        if(bundle != null) {
            statusFilter = bundle.getString(Constants.IntentKeys.allGameList);
            if(statusFilter == null){
                statusFilter = bundle.getString(Constants.IntentKeys.tbpGameList);
                if(statusFilter == null)
                    statusFilter = bundle.getString(Constants.IntentKeys.completedGameList);
            }
        }

        Intent serviceIntent = new Intent(getActivity(), GameListLoadingService.class);
        serviceIntent.putExtra(Constants.IntentKeys.statusFilter, statusFilter);
        getActivity().startService(serviceIntent);

        gamesList = new ArrayList<>();

        gamesAdapter = new GamesAdapter(getActivity(), gamesList);

        WrapContentLinearLayoutManager mLayoutManager = new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(gamesAdapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerTouchListener(getActivity(), new RecyclerTouchListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        Game game = gamesAdapter.getList().get(position);

                        Intent intent = new Intent(getActivity(), GameDetailsActivity.class);
                        intent.putExtra(Constants.IntentKeys.game, game);
                        startActivity(intent);
                    }
                })
        );

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0){
                    if(isLastItemDisplaying(recyclerView)){
                        fab.show();
                    } else {
                        fab.hide();
                    }
                } else if (dy < 0) {
                    fab.show();
                }
            }

            private boolean isLastItemDisplaying(RecyclerView recyclerView) {
                if (recyclerView.getAdapter().getItemCount() != 0) {
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                        return true;
                }
                return false;
            }

        });

        return viewFragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Game> temp = new ArrayList();
                for(Game g: gamesList){
                    String gameName = Normalizer.normalize(g.getName().toLowerCase(), Normalizer.Form.NFD);
                    gameName = gameName.replaceAll("\\p{M}", "");
                    if(gameName.contains(newText)){
                        temp.add(g);
                    }
                }

                gamesAdapter.filter(temp);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    //region Event Handlers

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetAllGameMessageEvent(GetAllGameMessageEvent event) {

        if(gamesList.size() == 0 && event.getStatusFilter().equalsIgnoreCase(statusFilter)){
            List<Game> eventGamesList = event.getGameList();

            for(int i = 0; i < eventGamesList.size(); i++ ){
                gamesList.add(eventGamesList.get(i));
                gamesAdapter.notifyItemInserted(i);
            }

            if(gamesList.size() <= 0) {
                noGamesMsg.setVisibility(View.VISIBLE);
            } else {
                noGamesMsg.setVisibility(View.GONE);
            }

            progressBar.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddGameMessageEvent(AddGameMessageEvent event) {

        Game newAddedGame = event.getGame();

        Utility.showSnackBar(getActivity(), recyclerView, getString(R.string.msg_added_to_library));

        if(statusFilter != null && statusFilter.equalsIgnoreCase(Constants.Status.all)
                || newAddedGame.getStatus().equalsIgnoreCase(statusFilter)){
            noGamesMsg.setVisibility(View.GONE);
            gamesList.add(0, newAddedGame);
            recyclerView.getLayoutManager().scrollToPosition(0);
            gamesAdapter.notifyItemInserted(0);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRemoveGameMessageEvent(RemoveGameMessageEvent event) {
        Game removedGame = event.getGame();

        int index = -1;
        for(int i = 0; i < gamesList.size(); i++){
            if(removedGame.getId() == gamesList.get(i).getId()) index = i;
        }

        if(index > -1) {
            gamesList.remove(index);
            gamesAdapter.notifyItemRemoved(index);
        }

        if(gamesList.size() == 0)
            noGamesMsg.setVisibility(View.VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateGameMessageEvent(UpdateGameMessageEvent event) {
        Game updatedGame = event.getGame();

        int index = -1;
        for(int i = 0; i < gamesList.size(); i++){
            if(updatedGame.getId() == gamesList.get(i).getId()) index = i;
        }

        if(index > -1) {

            if(statusFilter != null && statusFilter.equalsIgnoreCase(Constants.Status.all)
                    || updatedGame.getStatus().equalsIgnoreCase(statusFilter)){

                Game oldGame = gamesList.get(index);
                oldGame.setFavorite(updatedGame.isFavorite());
                oldGame.setPlatform(updatedGame.getPlatform());
                oldGame.setStore(updatedGame.getStore());
                oldGame.setStatus(updatedGame.getStatus());
                gamesAdapter.notifyItemChanged(index);

            } else {

                gamesList.remove(index);
                gamesAdapter.notifyItemRemoved(index);

            }
        } else if(updatedGame.getStatus().equalsIgnoreCase(statusFilter)) { // case where status got updated to something new
            noGamesMsg.setVisibility(View.GONE);
            gamesList.add(0, updatedGame);
            recyclerView.getLayoutManager().scrollToPosition(0);
            gamesAdapter.notifyItemInserted(0);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateExpansionMessageEvent(UpdateExpansionMessageEvent event) {
        Expansion updatedExpansion = event.getExpansion();

        int index = -1;
        for(int i = 0; i < gamesList.size(); i++){
            if(updatedExpansion.getBaseGameId() == gamesList.get(i).getId()) index = i;
        }

        if(index > -1) {
            Game oldGame = gamesList.get(index);
            List<Expansion> oldExpansionList = oldGame.getExpansionList();
            for (Expansion oldExpansion: oldExpansionList) {
                if(oldExpansion.getId() == updatedExpansion.getId())
                    oldExpansion.setCompleted(updatedExpansion.isCompleted());
            }
            gamesAdapter.notifyItemChanged(index);
        }
    }

    //endregion

}
