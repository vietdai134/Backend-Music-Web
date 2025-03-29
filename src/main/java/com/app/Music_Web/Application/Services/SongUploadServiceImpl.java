package com.app.Music_Web.Application.Services;

import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.app.Music_Web.Application.DTO.SongUploadDTO;
import com.app.Music_Web.Application.Ports.In.SongUpload.SaveSongUploadService;
import com.app.Music_Web.Application.Ports.Out.SongRepositoryPort;
import com.app.Music_Web.Application.Ports.Out.SongUploadRepositoryPort;
import com.app.Music_Web.Application.Ports.Out.UserRepositoryPort;
import com.app.Music_Web.Domain.Entities.Song;
import com.app.Music_Web.Domain.Entities.SongUpload;
import com.app.Music_Web.Domain.Entities.User;

@Service
public class SongUploadServiceImpl implements SaveSongUploadService{
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
    
}
