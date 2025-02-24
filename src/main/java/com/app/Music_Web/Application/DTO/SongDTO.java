package com.app.Music_Web.Application.DTO;

public class SongDTO {
    private Long id;
    private String title;
    private String artist;
    private String genre;

    public SongDTO() {}
    public SongDTO(Long id, String title, String artist, String genre) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.genre = genre;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
}