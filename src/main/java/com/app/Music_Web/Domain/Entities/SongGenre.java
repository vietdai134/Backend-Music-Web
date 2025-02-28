package com.app.Music_Web.Domain.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "song_genre")
public class SongGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //khóa ngoại liên kết đến bảng Song
    @ManyToOne
    @JoinColumn(name = "song_id",nullable = false)
    private Song song;

    //khóa ngoại liên kết đến bảng Genre
    @ManyToOne
    @JoinColumn(name = "genre_id",nullable = false)
    private Genre genre;
}
