package com.androidvoyage.ncsmusicplayer.manager;

import android.media.MediaMetadataRetriever;
import android.os.Environment;

import com.androidvoyage.ncsmusicplayer.utils.AppConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class SongManager {

    final String MEDIA_PATH = Environment.getExternalStorageDirectory()
            .getPath() + "/";
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    private String mp3Pattern = ".mp3";

    // Constructor
    public SongManager() {

    }

    /**
     * Function to read all mp3 files and store the details in
     * ArrayList
     */
    public ArrayList<HashMap<String, String>> getPlayList() {
        System.out.println(MEDIA_PATH);
        if (MEDIA_PATH != null) {
            File home = new File(MEDIA_PATH);
            File[] listFiles = home.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    System.out.println(file.getAbsolutePath());
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        addSongToList(file);
                    }
                }
            }
        }
        // return songs list array
        return songsList;
    }

    private void scanDirectory(File directory) {
        if (directory != null) {
            File[] listFiles = directory.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        addSongToList(file);
                    }

                }
            }
        }
    }

    private void addSongToList(File song) {
        if (song.getName().endsWith(mp3Pattern)) {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(song.getPath());
            HashMap<String, String> songMap = new HashMap<>();

            if (mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) != null) {
                songMap.put(AppConstants.SONG_TITLE, mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
            } else {
                songMap.put(AppConstants.SONG_TITLE, song.getName());
            }

            songMap.put(AppConstants.SONG_PATH, song.getPath());
            songMap.put(AppConstants.ALBUM, mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
            songMap.put(AppConstants.Duration, convertToMinute(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));
            songMap.put(AppConstants.AUTHOR, mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR));
            songMap.put(AppConstants.BIT_RATE, mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE));
            songMap.put(AppConstants.YEAR, mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR));
            songMap.put(AppConstants.LOCATION, mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_LOCATION));
            // Adding each song to SongList
            songsList.add(songMap);
        }
    }

    private String convertToMinute(String extractMetadata) {
        Integer duration = Integer.parseInt(extractMetadata);
        long minutes = (duration / 1000) / 60;
        long seconds = (duration / 1000) % 60;
        return String.valueOf(minutes) + "." + String.valueOf(seconds);
    }
}
