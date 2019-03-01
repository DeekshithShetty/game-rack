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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent addGameActivityIntent = new Intent(MainActivity.this, AddGameActivity.class);
                startActivity(addGameActivityIntent);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
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
