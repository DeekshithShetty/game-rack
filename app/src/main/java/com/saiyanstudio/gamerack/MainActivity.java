package com.saiyanstudio.gamerack;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.saiyanstudio.gamerack.adapters.ViewPagerAdapter;
import com.saiyanstudio.gamerack.common.Constants;
import com.saiyanstudio.gamerack.fragments.GameListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabs_game_list) TabLayout tabLayout;
    @BindView(R.id.viewpager_game_list) ViewPager viewPager;
    @BindView(R.id.fab_add_game) FloatingActionButton fab;

    @BindView(R.id.adView) AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // Load ads
        MobileAds.initialize(this, Constants.AdMobs.appId);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(Constants.AdMobs.testDeviceId)
                .build();
        adView.loadAd(adRequest);

        setSupportActionBar(toolbar);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent addGameActivityIntent = new Intent(MainActivity.this, AddGameActivity.class);
                startActivity(addGameActivityIntent);

                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        GameListFragment allGameListFragment = new GameListFragment();
        Bundle allGameListBundle = new Bundle();
        allGameListBundle.putString(Constants.IntentKeys.allGameList, Constants.Status.all);
        allGameListFragment.setArguments(allGameListBundle);

        GameListFragment tbpGameListFragment = new GameListFragment();
        Bundle tbpGameListBundle = new Bundle();
        tbpGameListBundle.putString(Constants.IntentKeys.tbpGameList, Constants.Status.toBePlayed);
        tbpGameListFragment.setArguments(tbpGameListBundle);

        GameListFragment completedGameListFragment = new GameListFragment();
        Bundle completedGameListBundle = new Bundle();
        completedGameListBundle.putString(Constants.IntentKeys.completedGameList, Constants.Status.completed);
        completedGameListFragment.setArguments(completedGameListBundle);

        adapter.addFragment(allGameListFragment, Constants.ViewPager.allGames);
        adapter.addFragment(tbpGameListFragment, Constants.ViewPager.tbpGames);
        adapter.addFragment(completedGameListFragment, Constants.ViewPager.completedGames);
        viewPager.setAdapter(adapter);
    }
}
