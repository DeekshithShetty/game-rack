package com.saiyanstudio.gamerack;

import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.saiyanstudio.gamerack.adapters.AddGameAdapter;
import com.saiyanstudio.gamerack.common.Constants;
import com.saiyanstudio.gamerack.common.Utility;
import com.saiyanstudio.gamerack.fragments.GameInfoDialogFragment;
import com.saiyanstudio.gamerack.listeners.RecyclerTouchListener;
import com.saiyanstudio.gamerack.models.Game;
import com.saiyanstudio.gamerack.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddGameActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_search) Toolbar toolbar;
    @BindView(R.id.editText_search) AutoCompleteTextView searchTextView;
    @BindView(R.id.imageButton_clear) ImageButton clearButton;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.textView_no_games_found_msg) TextView noGamesMsg;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    @BindView(R.id.adView) AdView adView;

    private AddGameAdapter addGameAdapter;
    private List<Game> gamesList = new ArrayList<>();
    private ArrayList<String> searchHistoryList;
    private ArrayAdapter<String> adapter;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        ButterKnife.bind(this);

        // Load ads
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(Constants.AdMobs.testDeviceId)
                .build();
        adView.loadAd(adRequest);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(Constants.AdMobs.interstitialAdUnitId);
        interstitialAd.loadAd(adRequest);

        toolbar.setTitle("Search");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchHistoryList = new ArrayList<>();

        prefs = getSharedPreferences(Constants.SharedPrefTags.gameSearch, Context.MODE_PRIVATE);
        editor = prefs.edit();

        final Set<String> searchTextSet = prefs.getStringSet(Constants.SharedPrefTags.gameSearchKey, null);
        if(searchTextSet != null)
            searchHistoryList.addAll(searchTextSet);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, searchHistoryList);
        searchTextView.setAdapter(adapter);

        searchTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String searchText = v.getText().toString();

                if (actionId == EditorInfo.IME_ACTION_SEARCH && !searchText.isEmpty()) {

                    ((AutoCompleteTextView)v).dismissDropDown();

                    // Hide keyboard on search
                    Utility.hideSoftKeyboard(AddGameActivity.this);

                    // Check whether the device is connected to internet
                    if(Utility.isNetworkConnected(AddGameActivity.this)){
                        if(!Utility.isInternetAvailable()){
                            //Utility.showSnackBar(AddGameActivity.this, recyclerView, getString(R.string.msg_no_internet_available));
                            //return true;
                        }
                    } else {
                        Utility.showSnackBar(AddGameActivity.this, recyclerView, getString(R.string.msg_go_online_to_search));
                        return true;
                    }

                    // Hide no game msg and show progress bar
                    noGamesMsg.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);

                    // Store the search text in shared preference
                    Set<String> set = prefs.getStringSet(Constants.SharedPrefTags.gameSearchKey, new HashSet<String>());
                    set.add(v.getText().toString());
                    editor.putStringSet(Constants.SharedPrefTags.gameSearchKey, set);
                    editor.commit();

                    //This removes all strings that are equal to searchText in the adapter
                    for(int i = 0; i < adapter.getCount(); i++){
                        if(adapter.getItem(i).equals(searchText))
                            adapter.remove(searchText);
                    }
                    adapter.add(searchText);

                    // TODO: Move request body query strings somewhere else to make  it more maintainable
                    String strRequestBody = "search \"" + searchText + "\";fields name, first_release_date, cover.*;where category = (0,4);";

                    Call<List<Game>> searchGameCall
                            = RetrofitClient.getIgdbApiService(AddGameActivity.this).getGameInfoByQuery(strRequestBody);

                    searchGameCall.enqueue(new Callback<List<Game>>() {
                        @Override
                        public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {
                            progressBar.setVisibility(View.GONE);
                            if (response.isSuccessful()) {
                                if((response.body() != null) && response.body().size() > 0)
                                    prepareGameData(response.body());
                                else
                                    noGamesMsg.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Game>> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            noGamesMsg.setVisibility(View.VISIBLE);
                        }
                    });

                    return true;
                }
                return false;
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchTextView.setText("");
                Utility.showSoftKeyboard(AddGameActivity.this);
            }
        });

        addGameAdapter = new AddGameAdapter(this, gamesList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(addGameAdapter);

        recyclerView.addOnItemTouchListener(
            new RecyclerTouchListener(AddGameActivity.this, new RecyclerTouchListener.OnItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {
                    Game selectedGame = gamesList.get(position);
                    if (interstitialAd.isLoaded()) {
                        interstitialAd.show();
                    }

                    showGameInfoDialog(selectedGame);
                }
            })
        );
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_clear_history) {
            editor.remove(Constants.SharedPrefTags.gameSearchKey).commit();
            adapter.clear();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        // Hide keyboard on search
        Utility.hideSoftKeyboard(AddGameActivity.this);

        return true;
    }

    //region Private Helpers

    private void prepareGameData(List<Game> list) {
        gamesList.clear();

        for (Game retrofitGame: list) {
            gamesList.add(retrofitGame);
        }

        addGameAdapter.notifyDataSetChanged();
    }

    private void showGameInfoDialog(Game selectedGame) {

        Bundle addGameBundle = new Bundle();
        addGameBundle.putParcelable(Constants.IntentKeys.addGame, selectedGame);

        FragmentManager fragmentManager = getFragmentManager();
        GameInfoDialogFragment gameInfoDialogFragment = new GameInfoDialogFragment();
        gameInfoDialogFragment.setArguments(addGameBundle);
        gameInfoDialogFragment.setCancelable(false);
        gameInfoDialogFragment.show(fragmentManager, Constants.FragmentTags.gameInfoDialogFragment);
    }

    //endregion
}
