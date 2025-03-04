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
@Table(name = "follow")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="follow_id")
    private Long followId;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="follow_date",nullable = false)
    private Date followDate;
    
    //khóa ngoại liên kết đến bảng User
    @ManyToOne
    @JoinColumn(name = "follower_id",nullable = false)
    private User follower;

    //khóa ngoại liên kết đến bảng User
    @ManyToOne
    @JoinColumn(name = "followed_id",nullable = false)
    private User followed;
}
