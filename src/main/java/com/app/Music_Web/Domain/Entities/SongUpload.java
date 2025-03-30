package com.app.Music_Web.Domain.Entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
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
@Table(name = "song_upload", indexes = {
    @Index(name = "idx_upload_date", columnList = "upload_date")
})
public class SongUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upload_id")
    private Long uploadId;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="upload_date",nullable = false)
    private Date uploadDate;

    //khóa ngoại liên kết đến bảng User
    @ManyToOne
    @JoinColumn(name = "uploaded_by",nullable = false)
    private User user;

    //khóa ngoại liên kết đến bảng Song
    @ManyToOne
    @JoinColumn(name = "song_id",nullable = false)
    private Song song;

}
