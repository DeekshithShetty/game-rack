package com.saiyanstudio.gamerack;

import android.app.FragmentManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.saiyanstudio.gamerack.adapters.ViewPagerAdapter;
import com.saiyanstudio.gamerack.common.Constants;
import com.saiyanstudio.gamerack.common.Utility;
import com.saiyanstudio.gamerack.fragments.BaseDetailsFragment;
import com.saiyanstudio.gamerack.fragments.EditGameDialogFragment;
import com.saiyanstudio.gamerack.fragments.ExpansionDetailsFragment;
import com.saiyanstudio.gamerack.handlers.DatabaseHandler;
import com.saiyanstudio.gamerack.messageevents.RemoveGameMessageEvent;
import com.saiyanstudio.gamerack.messageevents.UpdateGameMessageEvent;
import com.saiyanstudio.gamerack.models.Expansion;
import com.saiyanstudio.gamerack.models.Game;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameDetailsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabs_game_details) TabLayout tabLayout;
    @BindView(R.id.viewpager_game_details) ViewPager viewPager;

    @BindView(R.id.adView) AdView adView;

    private Game selectedGame;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        ButterKnife.bind(this);

        // Load ads
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(Constants.AdMobs.testDeviceId)
                .build();
        adView.loadAd(adRequest);

        db = new DatabaseHandler(this);

        selectedGame = getIntent().getParcelableExtra(Constants.IntentKeys.game);

        toolbar.setTitle(selectedGame.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupViewPager(viewPager, selectedGame);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager, Game selectedGame) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle baseGameBundle = new Bundle();
        baseGameBundle.putParcelable(Constants.IntentKeys.baseGame, selectedGame);

        BaseDetailsFragment baseDetailsFragment = new BaseDetailsFragment();
        baseDetailsFragment.setArguments(baseGameBundle);

        Bundle expansionsBundle = new Bundle();
        expansionsBundle.putParcelableArrayList(Constants.IntentKeys.expansion, (ArrayList<Expansion>) selectedGame.getExpansionList());

        ExpansionDetailsFragment expansionDetailsFragment = new ExpansionDetailsFragment();
        expansionDetailsFragment.setArguments(expansionsBundle);

        adapter.addFragment(baseDetailsFragment, Constants.ViewPager.baseGame);
        adapter.addFragment(expansionDetailsFragment, Constants.ViewPager.expansion);
        viewPager.setAdapter(adapter);
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
        inflater.inflate(R.menu.menu_game_details, menu);

        boolean isFavorite = selectedGame.isFavorite();
        MenuItem item = menu.findItem(R.id.action_isFavorite);
        if(isFavorite) {
            item.setIcon(getResources().getDrawable(R.drawable.ic_star_white_24dp));
            item.setChecked(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.action_isFavorite:
                boolean isChecked = item.isChecked();
                if(isChecked) {
                    item.setIcon(getResources().getDrawable(R.drawable.ic_star_border_white_24dp));
                    Utility.showSnackBar(getApplicationContext(), findViewById(R.id.viewpager_game_details), getString(R.string.msg_removed_from_favorites));
                   } else {
                    item.setIcon(getResources().getDrawable(R.drawable.ic_star_white_24dp));
                    Utility.showSnackBar(getApplicationContext(), findViewById(R.id.viewpager_game_details), getString(R.string.msg_added_to_favorites));
                }
                item.setChecked(!isChecked);
                selectedGame.setFavorite(!isChecked);

                // Update in db
                db.updateGame(selectedGame);
                db.closeDB();

                // Send UpdateGameMessageEvent
                EventBus.getDefault().post(new UpdateGameMessageEvent(selectedGame));
                break;

            case R.id.action_remove_game:
                Utility.showSnackBar(getApplicationContext(), findViewById(R.id.viewpager_game_details), getString(R.string.msg_removing_game_from_library));

                // Delete from db
                db.deleteGame(selectedGame);
                db.closeDB();

                // Send RemoveGameMessageEvent
                EventBus.getDefault().post(new RemoveGameMessageEvent(selectedGame));
                Utility.showSnackBar(getApplicationContext(), findViewById(R.id.viewpager_game_details), getString(R.string.msg_removed_from_library));
                finish();
                break;

            case R.id.action_edit:
                Bundle editGameBundle = new Bundle();
                editGameBundle.putParcelable(Constants.IntentKeys.editGame, selectedGame);

                FragmentManager fragmentManager = getFragmentManager();
                EditGameDialogFragment editGameDialogFragment = new EditGameDialogFragment();
                editGameDialogFragment.setArguments(editGameBundle);
                editGameDialogFragment.setCancelable(false);
                editGameDialogFragment.show(fragmentManager, Constants.FragmentTags.editGameDialogFragment);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
