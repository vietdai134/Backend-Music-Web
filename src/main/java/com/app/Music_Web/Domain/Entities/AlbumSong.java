package com.app.Music_Web.Domain.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "album_song")
public class AlbumSong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_song_id")
    private Long albumSongId;

    //khóa ngoại liên kết với bảng album
    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    //khóa ngoai liên kết với bảng song
    @ManyToOne
    @JoinColumn(name = "song_id")
    private Song song;
}
