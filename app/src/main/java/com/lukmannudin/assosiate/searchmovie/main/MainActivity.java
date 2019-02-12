package com.lukmannudin.assosiate.searchmovie.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.lukmannudin.assosiate.searchmovie.Alarm.AlarmReceiver;
import com.lukmannudin.assosiate.searchmovie.R;
import com.lukmannudin.assosiate.searchmovie.ReminderSetting;
import com.lukmannudin.assosiate.searchmovie.Utils;
import com.lukmannudin.assosiate.searchmovie.main.Favorites.FavoritesFragment;
import com.lukmannudin.assosiate.searchmovie.main.now_playing_fragment.NowPlayngFragment;
import com.lukmannudin.assosiate.searchmovie.main.search_fragment.SearchFragment;
import com.lukmannudin.assosiate.searchmovie.main.up_coming_fragment.UpComingFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment pageContent = new NowPlayngFragment();
    private String selectedItem = null;
    private String itemSelect = "item_select";
    private String KEY_FRAGMENT = "fragment";
    private AlarmReceiver alarmReceiver;
    private int idNotif = 0;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        alarmReceiver = new AlarmReceiver();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.now_playing: {
                        pageContent = NowPlayngFragment.newInstance(R.id.now_playing);
                        Log.i("cekPage1", String.valueOf(R.id.now_playing));
                        break;
                    }
                    case R.id.up_coming: {
                        pageContent = UpComingFragment.newInstance(R.id.up_coming);
                        Log.i("cekPage2", String.valueOf(R.id.up_coming));
                        break;
                    }

                    case R.id.favorites_movie: {
                        pageContent = FavoritesFragment.newInstance(R.id.favorites_movie);
                        Log.i("cekPage3", String.valueOf(R.id.favorites_movie));
                        break;
                    }
                    case R.id.search_movie: {
                        pageContent = new SearchFragment();
                        break;
                    }
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, pageContent).commit();
                return true;
            }
        });
        int pageId = getIntent().getIntExtra(Utils.page, 0);

        if (pageId != 0) {
            switch (pageId) {
                case R.id.now_playing: {
                    pageContent = NowPlayngFragment.newInstance(R.id.now_playing);
                    bottomNavigationView.getMenu().findItem(R.id.now_playing).setChecked(true);
                    break;
                }
                case R.id.up_coming: {
                    pageContent = UpComingFragment.newInstance(R.id.up_coming);
                    bottomNavigationView.getMenu().findItem(R.id.up_coming).setChecked(true);
                    break;
                }
                case R.id.favorites_movie: {
                    pageContent = FavoritesFragment.newInstance(R.id.favorites_movie);
                    bottomNavigationView.getMenu().findItem(R.id.favorites_movie).setChecked(true);
                    break;
                }
            }
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, pageContent).commit();
        } else {
            pageContent = getSupportFragmentManager().getFragment(savedInstanceState, KEY_FRAGMENT);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, pageContent).commit();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(itemSelect, selectedItem);
        getSupportFragmentManager().putFragment(outState, KEY_FRAGMENT, pageContent);
        super.onSaveInstanceState(outState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_language: {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS), 0);
                return true;
            }
            case R.id.reminder_setting: {
                Intent intent = new Intent(this, ReminderSetting.class);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}