package com.app.Music_Web.Domain.Entities;

import java.util.List;

import com.app.Music_Web.Domain.ValueObjects.Song.SongArtist;
import com.app.Music_Web.Domain.ValueObjects.Song.SongTitle;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "song")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_id")
    private Long songId;

    @Embedded
    private SongTitle title;

    @Embedded
    private SongArtist artist;

    @Column(name="song_image",nullable = false)
    private String songImage;

    @Column(name="file_song_id",nullable = false)
    private String fileSongId;

    @Column(name="downloadable",nullable = false)
    private boolean downloadable;

    //liên kết 1-n với bảng SongUpload
    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<SongUpload> songUploads;

    //liên kết 1-n với bảng SongApproval
    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<SongApproval> songApprovals;

    //liên kết 1-1 với bảng SongLyric
    @OneToOne(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true )
    private SongLyric songLyric;

    //liên kết 1-n với bảng AlbumSong
    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<AlbumSong> albumSongs;

    //liên kết 1-n với bảng PlaylistSong
    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<PlaylistSong> playlistSongs;

    //liên kết 1-n với bảng LikedSong
    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<LikedSong> likedSongs;

    //liên kết 1-n với bảng ListeningHistory
    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<ListenHistory> listeningHistories;

    //liên kết 1-n với bảng SongGenre
    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<SongGenre> songGenres;
}