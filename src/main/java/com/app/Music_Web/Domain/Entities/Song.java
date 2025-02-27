package com.app.Music_Web.Domain.Entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "song")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_id")
    private Long songId;

    @Column(name="title",nullable = false)
    private String title;

    @Column(name="artist",nullable = false)
    private String artist;

    @Column(name="genre",nullable = false)
    private String genre;

    @Column(name="song_image",nullable = false)
    private String songImage;

    @Column(name="source_url",nullable = true)
    private String sourceUrl;

    @Column(name="downloadable",nullable = false)
    private boolean downloadable;

    //liên kết 1-n với bảng SongUpload
    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<SongUpload> songUploads=new ArrayList<>();

    //liên kết 1-n với bảng SongUpload
    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<SongApproval> songApprovals=new ArrayList<>();

    //liên kết 1-1 với bảng SongLyric
    @OneToOne(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true )
    private SongLyric songLyric;

    //liên kết 1-n với bảng AlbumSong
    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<AlbumSong> albumSongs=new ArrayList<>();

    //liên kết 1-n với bảng AlbumSong
    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<PlaylistSong> playlistSongs=new ArrayList<>();

    //liên kết 1-n với bảng LikedSong
    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<LikedSong> likedSongs=new ArrayList<>();

    //liên kết 1-n với bảng ListeningHistory
    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<ListenHistory> listeningHistories=new ArrayList<>();
}