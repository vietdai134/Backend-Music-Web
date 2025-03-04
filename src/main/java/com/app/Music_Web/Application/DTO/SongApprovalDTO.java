package com.app.Music_Web.Application.DTO;

import java.util.Date;

import com.app.Music_Web.Domain.Enums.ApprovalStatus;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongApprovalDTO {
    private Long approvalId;
    private ApprovalStatus approvalStatus;
    private Date approvedDate;
    private Long approvedBy;
    private Long songId;
}
