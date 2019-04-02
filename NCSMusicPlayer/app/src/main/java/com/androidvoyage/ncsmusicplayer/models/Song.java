package com.androidvoyage.ncsmusicplayer.models;

public class Song {

    int id;
    String displayName;
    String artist;
    String data;


    public Song(int id, String displayName, String artist, String data) {
        this.id = id;
        this.displayName = displayName;
        this.artist = artist;
        this.data = data;
    }
}
