package com.app.Music_Web.Domain.Entities;

import java.util.Date;
import java.util.List;

import com.app.Music_Web.Domain.Enums.AccountType;
import com.app.Music_Web.Domain.ValueObjects.User.UserEmail;
import com.app.Music_Web.Domain.ValueObjects.User.UserName;
import com.app.Music_Web.Domain.ValueObjects.User.UserPassword;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Builder
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Embedded
    private UserName userName;

    @Embedded
    private UserEmail email;

    @Embedded
    private UserPassword password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,name = "account_type")
    private AccountType accountType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false,name = "created_date")
    private Date createdDate;

    //liên kết 1-n với bảng UserPayments
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<UserPayment> userPayments;

    //liên kết 1-n với bảng UserAuth
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<UserAuth> userAuths;

    //liên kết 1-n với bảng UserStatus
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<UserStatus> userStatusHistory;

    //liên kết 1-n với bảng SongUpload
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<SongUpload> songUploads;

    //liên kết 1-n với bảng SongApproval
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<SongApproval> songApprovals;

    //liên kết 1-n với bảng album
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<Album> albums;

    //liên kết 1-n với bảng playlist
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<Playlist> playlists;

    //liên kết 1-n với bảng likedSong
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<LikedSong> likedSongs;

    //liên kết 1-n với bảng listenHistory
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<ListenHistory> listeningHistories;

    //liên kết 1-n với bảng follow
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<Follow> followerLists;

    //liên kết 1-n với bảng follow
    @OneToMany(mappedBy = "followed", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<Follow> followedLists;

    //liên kết 1-n với bảng userRole
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<UserRole> userRoles;
}
