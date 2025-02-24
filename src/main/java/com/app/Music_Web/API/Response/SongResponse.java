package com.app.Music_Web.API.Response;

public class SongResponse {
    private Long id;
    private String title;

    public SongResponse(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
}