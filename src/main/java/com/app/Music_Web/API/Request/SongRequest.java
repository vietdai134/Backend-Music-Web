package com.app.Music_Web.API.Request;

public class SongRequest {
    private String title;
    private String artist;
    private String genre;

    public SongRequest() {}
    public SongRequest(String title, String artist, String genre) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
}