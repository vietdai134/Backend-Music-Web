package com.app.Music_Web.Application.Services;

import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.app.Music_Web.Application.DTO.SongApprovalDTO;
import com.app.Music_Web.Application.Ports.In.SongApproval.SaveSongApprovalService;
import com.app.Music_Web.Application.Ports.In.SongApproval.UpdateSongApprovalService;
import com.app.Music_Web.Application.Ports.Out.SongApprovalRepositoryPort;
import com.app.Music_Web.Application.Ports.Out.SongRepositoryPort;
import com.app.Music_Web.Application.Ports.Out.SongUploadRepositoryPort;
import com.app.Music_Web.Application.Ports.Out.UserRepositoryPort;
import com.app.Music_Web.Domain.Entities.Song;
import com.app.Music_Web.Domain.Entities.SongApproval;
import com.app.Music_Web.Domain.Entities.SongUpload;
import com.app.Music_Web.Domain.Entities.User;
import com.app.Music_Web.Domain.Enums.ApprovalStatus;

import jakarta.transaction.Transactional;

@Service
public class SongApprovalServiceImpl implements SaveSongApprovalService, UpdateSongApprovalService{
    private final SongApprovalRepositoryPort songApprovalRepositoryPort;
    private final SongUploadRepositoryPort songUploadRepositoryPort;
    private final SongRepositoryPort songRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    public SongApprovalServiceImpl(
        SongApprovalRepositoryPort songApprovalRepositoryPort,
        SongUploadRepositoryPort songUploadRepositoryPort,
        SongRepositoryPort songRepositoryPort,
        UserRepositoryPort userRepositoryPort
    ){
        this.songApprovalRepositoryPort=songApprovalRepositoryPort;
        this.songUploadRepositoryPort=songUploadRepositoryPort;
        this.songRepositoryPort=songRepositoryPort;
        this.userRepositoryPort=userRepositoryPort;
    }

    @Override
    public SongApprovalDTO saveSongApproval(Long songId, Long userId, ApprovalStatus status) {
        Song song = songRepositoryPort.findBySongId(songId);
        Optional<User> userOptional = userRepositoryPort.findById(userId);
        User user = userOptional.orElseThrow(() -> 
            new IllegalArgumentException("User with ID " + userId + " not found"));

        // Tạo entity SongApproval
        SongApproval songApproval = SongApproval.builder()
                .approvalStatus(status)
                .approvedDate(new Date())
                .song(song)
                .user(user) // Truyền User thay vì Optional<User>
                .build();

        // Lưu vào cơ sở dữ liệu
        SongApproval savedApproval = songApprovalRepositoryPort.save(songApproval);

        // Chuyển đổi sang DTO và trả về
        return SongApprovalDTO.builder()
                .approvalId(savedApproval.getApprovalId())
                .approvalStatus(savedApproval.getApprovalStatus())
                .approvedDate(savedApproval.getApprovedDate())
                .songId(savedApproval.getSong().getSongId())
                .approvedBy(savedApproval.getUser().getUserId())
                .build();
    }

    @Override
    @Transactional
    public void changeStatusSong(Long songId, ApprovalStatus approvalStatus){
        songApprovalRepositoryPort.updateStatusSong(songId, approvalStatus);
    }

    @Override
    @Transactional
    public void changeStatusUploadSong(Long uploadId, ApprovalStatus approvalStatus){
        SongUpload songUpload= songUploadRepositoryPort.findByUploadId(uploadId);
        songApprovalRepositoryPort.updateStatusSong(songUpload.getSong().getSongId(), approvalStatus);
    }
}
