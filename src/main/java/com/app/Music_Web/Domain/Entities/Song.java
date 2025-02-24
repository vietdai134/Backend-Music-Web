package com.app.Music_Web.Domain.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.time.LocalDateTime;

@Entity
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String artist;
    private String genre;
    private LocalDateTime createdAt; // Thêm field thời gian

    public Song() {
        this.createdAt = LocalDateTime.now(); // Ghi thời gian hiện tại khi tạo
    }

    public Song(String title, String artist, String genre) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}