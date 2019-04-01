package com.androidvoyage.ncsmusicplayer.view.activities;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.androidvoyage.ncsmusicplayer.view.base.BaseActivity;
import com.androidvoyage.ncsmusicplayer.view.viewmodels.MainViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.androidvoyage.ncsmusicplayer.R;
import com.androidvoyage.ncsmusicplayer.view.fragments.SongsList;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements SongsList.OnFragmentInteractionListener, BottomNavigationView.OnNavigationItemSelectedListener {


    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigationView;

    @BindView(R.id.mainContentLayout)
    FrameLayout frameLayout;

    private MainViewModel mainViewModel;

    public static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        mainViewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())
                .create(MainViewModel.class);

        addFragment(R.id.mainContentLayout
                , SongsList.newInstance("NCS Music", "")
                , "NCS_Songs");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_ncs:
                replaceFragment(R.id.mainContentLayout
                        , SongsList.newInstance("NCS Music", "")
                        , "NCS_Songs"
                        , "NCS_Songs");
                break;
            case R.id.action_music:
                replaceFragment(R.id.mainContentLayout
                        , SongsList.newInstance("Music", "")
                        , "Music"
                        , "Music");
                break;
            case R.id.action_settings:
                replaceFragment(R.id.mainContentLayout
                        , SongsList.newInstance("Settings", "")
                        , "Setting"
                        , "Music");
                break;
        }
        return true;
    }
}