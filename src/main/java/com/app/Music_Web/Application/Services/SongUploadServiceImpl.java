package com.app.Music_Web.Application.Services;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.Music_Web.Application.DTO.SongDTO;
import com.app.Music_Web.Application.DTO.SongUploadDTO;
import com.app.Music_Web.Application.Ports.In.SongUpload.FindSongUploadService;
import com.app.Music_Web.Application.Ports.In.SongUpload.SaveSongUploadService;
import com.app.Music_Web.Application.Ports.Out.SongRepositoryPort;
import com.app.Music_Web.Application.Ports.Out.SongUploadRepositoryPort;
import com.app.Music_Web.Application.Ports.Out.UserRepositoryPort;
import com.app.Music_Web.Domain.Entities.Song;
import com.app.Music_Web.Domain.Entities.SongUpload;
import com.app.Music_Web.Domain.Entities.User;
import com.app.Music_Web.Domain.Enums.ApprovalStatus;

@Service
public class SongUploadServiceImpl implements SaveSongUploadService, FindSongUploadService{
    private final SongUploadRepositoryPort songUploadRepositoryPort;
    private final SongRepositoryPort songRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    public SongUploadServiceImpl (
        SongUploadRepositoryPort songUploadRepositoryPort,
        SongRepositoryPort songRepositoryPort,
        UserRepositoryPort userRepositoryPort
    ){
        this.songUploadRepositoryPort=songUploadRepositoryPort;
        this.songRepositoryPort=songRepositoryPort;
        this.userRepositoryPort=userRepositoryPort;
    }
    @Override
    public SongUploadDTO saveSongUpload(Long songId, Long userId) {
        Song song = songRepositoryPort.findBySongId(songId);
        Optional<User> userOptional = userRepositoryPort.findById(userId);
        User user = userOptional.orElseThrow(() -> 
            new IllegalArgumentException("User with ID " + userId + " not found"));

        SongUpload songUpload= SongUpload.builder()
                            .uploadDate(new Date())
                            .user(user)
                            .song(song)
                            .build();
        SongUpload saveSongUpload= songUploadRepositoryPort.save(songUpload);

        return SongUploadDTO.builder()
                .uploadId(saveSongUpload.getUploadId())
                .uploadDate(saveSongUpload.getUploadDate())
                .uploadedBy(saveSongUpload.getUser().getUserId())
                .songId(saveSongUpload.getSong().getSongId())
                .build();
    }
    @Override
    public Page<SongUploadDTO> findAllWithSong(ApprovalStatus approvalStatus, Pageable pageable) {
        Page<Object[]> result= songUploadRepositoryPort.findAllWithSong(approvalStatus, pageable);
        return result.map(objects -> SongUploadDTO.builder()
        .uploadId((Long) objects[0])          // su.uploadId
        .uploadDate((Date) objects[1])        // su.uploadDate
        .songDto(SongDTO.builder()
            .title((String) objects[2])       // s.title.title
            .artist((String) objects[3])      // s.artist.artist
            .songImage((String) objects[4])   // s.songImage
            .fileSongId((String) objects[5])  // s.fileSongId
            .build())
        .userName((String) objects[6])        // u.userName.userName
        // .songId((Long) objects[7])            // s.songId
        // .uploadedBy((Long) objects[8])        // u.userId
        .build());
    }
    @Override
    public Page<SongUploadDTO> searchUploadByTitleOrArtist(String keyword, ApprovalStatus status, Pageable pageable) {
        Page<Object[]> result= songUploadRepositoryPort.searchUploadByTitleOrArtist(keyword,status, pageable);
        return result.map(objects -> SongUploadDTO.builder()
        .uploadId((Long) objects[0])          // su.uploadId
        .uploadDate((Date) objects[1])        // su.uploadDate
        .songDto(SongDTO.builder()
            .title((String) objects[2])       // s.title.title
            .artist((String) objects[3])      // s.artist.artist
            .songImage((String) objects[4])   // s.songImage
            .fileSongId((String) objects[5])  // s.fileSongId
            .build())
        .userName((String) objects[6])        // u.userName.userName
        // .songId((Long) objects[7])            // s.songId
        // .uploadedBy((Long) objects[8])        // u.userId
        .build());
    }
    
}
