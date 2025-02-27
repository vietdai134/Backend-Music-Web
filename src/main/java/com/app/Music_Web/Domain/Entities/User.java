package com.app.Music_Web.Domain.Entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false,name = "user_name")
    private String userName;

    @Column(nullable = false,unique = true,name = "email")
    private String email;

    @Column(nullable = true,name = "password")
    private String password;

    @Column(nullable = false,name = "account_type")
    private String accountType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false,name = "created_date")
    private Date createdDate;

    //liên kết 1-n với bảng UserPayments
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<UserPayment> userPayments=new ArrayList<>();

    //liên kết 1-n với bảng UserAuth
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<UserAuth> userAuths=new ArrayList<>();

    //liên kết 1-n với bảng UserStatus
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<UserStatus> userStatusHistory=new ArrayList<>();

    //liên kết 1-n với bảng SongUpload
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<SongUpload> songUploads=new ArrayList<>();

    //liên kết 1-n với bảng SongApproval
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<SongApproval> songApprovals=new ArrayList<>();

    //liên kết 1-n với bảng album
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<Album> albums=new ArrayList<>();

    //liên kết 1-n với bảng playlist
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<Playlist> playlists=new ArrayList<>();

    //liên kết 1-n với bảng likedSong
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<LikedSong> likedSongs=new ArrayList<>();

    //liên kết 1-n với bảng likedSong
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<ListenHistory> listeningHistories=new ArrayList<>();

    //liên kết 1-n với bảng follow
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<Follow> followerLists=new ArrayList<>();

    //liên kết 1-n với bảng follow
    @OneToMany(mappedBy = "followed", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<Follow> followedLists=new ArrayList<>();

    //liên kết 1-n với bảng follow
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<UserRole> userRoles=new ArrayList<>();
}
