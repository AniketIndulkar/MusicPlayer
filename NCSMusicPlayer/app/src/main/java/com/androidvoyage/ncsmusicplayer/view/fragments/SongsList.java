package com.androidvoyage.ncsmusicplayer.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidvoyage.ncsmusicplayer.R;
import com.androidvoyage.ncsmusicplayer.adapters.LocalSongsAdapter;
import com.androidvoyage.ncsmusicplayer.manager.SongManager;
import com.androidvoyage.ncsmusicplayer.view.viewmodels.SongListViewModel;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.widget.LinearLayout.HORIZONTAL;

public class SongsList extends Fragment implements LocalSongsAdapter.ItemClickListener {

    @BindView(R.id.rvLocalMusic)
    RecyclerView rvLocalMusic;
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

        songsListData = new ArrayList<>();
        SongManager plm = new SongManager();
        // get all songs from sdcard
        songsListData = plm.getPlayList();
        setUpSongsRecyclerView();
        return view;
    }
    private void setUpSongsRecyclerView() {
// set up the RecyclerView
        rvLocalMusic.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new LocalSongsAdapter(getActivity(), songsListData);
        adapter.setClickListener(this);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getActivity(), 1);
        rvLocalMusic.addItemDecoration(itemDecor);
        rvLocalMusic.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
