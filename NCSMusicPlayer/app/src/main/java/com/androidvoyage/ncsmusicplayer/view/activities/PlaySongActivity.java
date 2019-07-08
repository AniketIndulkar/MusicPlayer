package com.androidvoyage.ncsmusicplayer.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.androidvoyage.ncsmusicplayer.R;
import com.androidvoyage.ncsmusicplayer.utils.AppConstants;
import com.androidvoyage.ncsmusicplayer.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.tankery.lib.circularseekbar.CircularSeekBar;

public class PlaySongActivity extends AppCompatActivity implements CircularSeekBar.OnCircularSeekBarChangeListener, MediaPlayer.OnCompletionListener {

    HashMap<String, String> songsData;

    @BindView(R.id.ivSongImage)
    CircleImageView ivSongImage;

    @BindView(R.id.circularSeekBar)
    CircularSeekBar circularSeekBar;

    @BindView(R.id.tvSongTitle)
    TextView tvSongTitle;

    @BindView(R.id.tvSongAlbum)
    TextView tvSongAlbum;

    @BindView(R.id.tvCurrentTime)
    TextView tvCurrentTime;

    @BindView(R.id.tvEndTime)
    TextView tvEndTime;

    @BindView(R.id.ivShuffle)
    ImageView ivShuffle;

    @BindView(R.id.ivRewind)
    ImageView ivRewind;

    @BindView(R.id.ivPlay)
    ImageView ivPlay;

    @BindView(R.id.ivForward)
    ImageView ivForward;

    @BindView(R.id.ivRepeat)
    ImageView ivRepeat;

    MediaPlayer mediaPlayer = new MediaPlayer();
    boolean isPlaying = false;
    Handler mHandler;
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000; // 5000 milliseconds
    private int currentSongIndex = 0;
    private boolean isShuffle = false;
    private boolean isRepeat = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        ButterKnife.bind(this);
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        songsData = (HashMap<String, String>) getIntent().getSerializableExtra(AppConstants.SONG_DATA);
        setDataToView();
        try {
            mediaPlayer.setDataSource(songsData.get(AppConstants.SONG_PATH));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnCompletionListener(this);
        mHandler = new Handler(Looper.myLooper());
    }

    private void setDataToView() {
        tvSongTitle.setText(songsData.get(AppConstants.SONG_TITLE));
        tvSongAlbum.setText(songsData.get(AppConstants.ALBUM));
        tvEndTime.setText(songsData.get(AppConstants.Duration));
        tvCurrentTime.setText("0.00");
        circularSeekBar.setMax(100);
        circularSeekBar.setOnSeekBarChangeListener(this);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(songsData.get(AppConstants.SONG_PATH));
        byte[] data = mmr.getEmbeddedPicture();
        if (data != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            ivSongImage.setImageBitmap(bitmap);
        }
    }

    @OnClick(R.id.ivPlay)
    public void playMusic(View view) {
        if (!isPlaying) {
            ivPlay.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
            isPlaying = true;
            circularSeekBar.setProgress(0);
            circularSeekBar.setMax(100);
            updateProgressBar();
        } else {
            ivPlay.setImageResource(R.drawable.ic_play);
            mediaPlayer.pause();
            isPlaying = false;
        }

    }


    @OnClick(R.id.ivForward)
    public void forwardPlay(View view) {
        int currentPosition = mediaPlayer.getCurrentPosition();
        if (currentPosition + seekForwardTime <= mediaPlayer.getDuration()) {
            mediaPlayer.seekTo(currentPosition + seekForwardTime);
        } else {
            mediaPlayer.seekTo(mediaPlayer.getDuration());
        }
    }

    @OnClick(R.id.ivRewind)
    public void rewindPlay(View view) {
        int currentPosition = mediaPlayer.getCurrentPosition();
        if (currentPosition - seekBackwardTime >= 0) {
            mediaPlayer.seekTo(currentPosition - seekBackwardTime);
        } else {
            mediaPlayer.seekTo(0);
        }
    }


    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();
            tvCurrentTime.setText("" + Utils.milliSecondsToTimer(currentDuration));
            int progress = Utils.getProgressPercentage(currentDuration, totalDuration);
            circularSeekBar.setProgress(progress);
            mHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {

    }

    @Override
    public void onStopTrackingTouch(CircularSeekBar seekBar) {

        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mediaPlayer.getDuration();
        int currentPosition = Utils.progressToTimer((int) seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mediaPlayer.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }

    @Override
    public void onStartTrackingTouch(CircularSeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }
}
