package com.app.Music_Web.Domain.Entities;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "liked_song")
public class LikedSong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="like_id")
    private Long likeId;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="liked_date",nullable =  false)
    private Date likedDate;
    
    //khóa ngoại liên kết đến bảng Song
    @ManyToOne
    @JoinColumn(name = "song_id",nullable = false)
    private Song song;

    //khóa ngoại liên kết đến bảng User
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
}
