package com.androidvoyage.ncsmusicplayer.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidvoyage.ncsmusicplayer.R;
import com.androidvoyage.ncsmusicplayer.adapters.LocalSongsAdapter;
import com.androidvoyage.ncsmusicplayer.manager.SongManager;
import com.androidvoyage.ncsmusicplayer.utils.AppConstants;
import com.androidvoyage.ncsmusicplayer.view.activities.PlaySongActivity;
import com.androidvoyage.ncsmusicplayer.view.viewmodels.SongListViewModel;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SongsList extends Fragment implements LocalSongsAdapter.ItemClickListener, SongListViewModel.OnSongsLoad {

    @BindView(R.id.rvLocalMusic)
    RecyclerView rvLocalMusic;

    @BindView(R.id.songsListProgress)
    ProgressBar songsListProgress;

    @BindView(R.id.tvNoSongs)
    TextView tvNoSongs;

    LocalSongsAdapter adapter;

    private String TAG = SongsList.class.getSimpleName();

    private SongListViewModel songListViewModel;

    public SongsList() {
    }

    public static SongsList newInstance(String param1, String param2) {
        SongsList fragment = new SongsList();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private ArrayList<HashMap<String, String>> songsListData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_songs_list, container, false);
        ButterKnife.bind(this, view);
        songListViewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity()
                        .getApplication())
                .create(SongListViewModel.class);
        songListViewModel.setOnSongsLoad(this);
        songsListData = new ArrayList<>();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        songListViewModel.getSongsList();

    }


    @Override
    public void onItemClick(View view, int position) {
        Intent playSong = new Intent(getActivity(), PlaySongActivity.class);
        playSong.putExtra(AppConstants.SONG_DATA, songsListData.get(position));
        startActivity(playSong);
    }

    @Override
    public void allSongsLoaded(ArrayList<HashMap<String, String>> songs) {
        if (songs != null) {
            if (songs.size() > 0) {
                songsListProgress.setVisibility(View.GONE);
                songsListData = songs;
                setUpSongsRecyclerView();
            } else {
                songsListProgress.setVisibility(View.GONE);
                tvNoSongs.setVisibility(View.VISIBLE);
            }
        } else {
            songsListProgress.setVisibility(View.GONE);
            tvNoSongs.setVisibility(View.VISIBLE);
        }
    }

    private void setUpSongsRecyclerView() {
        rvLocalMusic.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new LocalSongsAdapter(getActivity(), songsListData);
        adapter.setClickListener(this);
//        DividerItemDecoration itemDecor = new DividerItemDecoration(getActivity(), 1);
//        rvLocalMusic.addItemDecoration(itemDecor);
        rvLocalMusic.setAdapter(adapter);
        rvLocalMusic.setVisibility(View.VISIBLE);
    }
}
