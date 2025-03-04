package com.app.Music_Web.Domain.Entities;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Entity
@Builder
@Table(name = "playlist")
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_id")
    private Long playlistId;

    @Column(name="playlist_name",nullable = false)
    private String playlistName;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_date",nullable =  false)
    private Date createdDate;
    
    //khóa ngoại liên kết đến bảng User
    @ManyToOne
    @JoinColumn(name = "created_by",nullable = false)
    private User user; // User ID

    //liên kết 1-n với bảng playlistSong
    @OneToMany(mappedBy="playlist", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<PlaylistSong> playlistSongs;
}
