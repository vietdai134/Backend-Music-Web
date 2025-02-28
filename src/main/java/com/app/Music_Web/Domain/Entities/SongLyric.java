package com.app.Music_Web.Domain.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "song_lyric")
public class SongLyric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lyric_id")
    private Long lyricsId;
    
    @Column(name="lyric",nullable = true)
    private String lyric;

    //khóa ngoại liên kết đến bảng Song
    @OneToOne
    @JoinColumn(name = "song_id")
    private Song song;
}
