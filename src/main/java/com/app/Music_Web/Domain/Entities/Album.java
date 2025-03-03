package com.app.Music_Web.Domain.Entities;

import java.util.Date;
import java.util.List;

import com.app.Music_Web.Domain.ValueObjects.Album.AlbumName;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Table(name = "album")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="album_id")
    private Long albumId;

    @Embedded
    private AlbumName albumName;

    @Column(name="album_image",nullable = false)
    private String albumImage;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_date",nullable = false)
    private Date createdDate;

    //khóa ngoại liên kết đến bảng User
    @ManyToOne
    @JoinColumn(name = "created_by",nullable = false)
    private User user; // User ID
    
    //liên kết 1-n với bảng AlbumSong
    @OneToMany(mappedBy="album", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<AlbumSong> albumSongs;
}
