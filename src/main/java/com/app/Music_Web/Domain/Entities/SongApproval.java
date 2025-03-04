package com.app.Music_Web.Domain.Entities;

import java.util.Date;

import com.app.Music_Web.Domain.Enums.ApprovalStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "song_approval")
public class SongApproval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "approval_id")
    private Long approvalId;

    @Enumerated(EnumType.STRING)
    @Column(name="approval_status",nullable = false)
    private ApprovalStatus approvalStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="approved_date",nullable = false)
    private Date approvedDate;

    //khóa ngoại liên kết đến bảng User
    @ManyToOne
    @JoinColumn(name = "approved_by",nullable = false)
    private User user;
    
    //khóa ngoại liên kết đến bảng Song
    @ManyToOne
    @JoinColumn(name = "song_id",nullable = false)
    private Song song;
}
