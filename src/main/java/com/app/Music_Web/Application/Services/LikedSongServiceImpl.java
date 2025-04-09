package com.app.Music_Web.Application.Services;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.Music_Web.Application.DTO.LikedSongDTO;
import com.app.Music_Web.Application.Mapper.LikedSongMapper;
import com.app.Music_Web.Application.Ports.In.LikedSong.DeleteLikedSongService;
import com.app.Music_Web.Application.Ports.In.LikedSong.FindLikedSongService;
import com.app.Music_Web.Application.Ports.In.LikedSong.SaveLikedSongService;
import com.app.Music_Web.Application.Ports.Out.LikedSongRepositoryPort;
import com.app.Music_Web.Application.Ports.Out.SongRepositoryPort;
import com.app.Music_Web.Application.Ports.Out.UserRepositoryPort;
import com.app.Music_Web.Domain.Entities.LikedSong;
import com.app.Music_Web.Domain.Entities.Song;
import com.app.Music_Web.Domain.Entities.User;
import com.app.Music_Web.Domain.ValueObjects.User.UserEmail;

@Service
public class LikedSongServiceImpl implements FindLikedSongService,
                                            DeleteLikedSongService,
                                            SaveLikedSongService {
    private final UserRepositoryPort userRepositoryPort;
    private final LikedSongRepositoryPort likedSongRepositoryPort;
    private final SongRepositoryPort songRepositoryPort;
    public LikedSongServiceImpl(
        UserRepositoryPort userRepositoryPort,
        LikedSongRepositoryPort likedSongRepositoryPort,
        SongRepositoryPort songRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.likedSongRepositoryPort = likedSongRepositoryPort;
        this.songRepositoryPort = songRepositoryPort;
    }
    @Override
    public void saveLikedSong(Long songId, String email) {
        Song song = songRepositoryPort.findBySongId(songId);
        UserEmail userEmail = new UserEmail(email);
        User user = userRepositoryPort.findByEmail(userEmail);
        LikedSong likedSong = LikedSong.builder()
                .song(song)
                .user(user)
                .likedDate(new Date())
                .build();
        likedSongRepositoryPort.save(likedSong);
    }

    @Override
    @Transactional
    public void deleteLikedSong(Long songId, String email) {
        UserEmail userEmail = new UserEmail(email);
        User user = userRepositoryPort.findByEmail(userEmail);
        likedSongRepositoryPort.deleteBySong_SongIdAndUser_UserId(songId, user.getUserId());
    }
    @Override
    public List<LikedSongDTO> findLikedSongsByUser(String email) {
        UserEmail userEmail = new UserEmail(email);
        User user = userRepositoryPort.findByEmail(userEmail);
        List<LikedSong> likedSongs= likedSongRepositoryPort.findAllByUser_UserId(user.getUserId());
        if(likedSongs.isEmpty()){
            return null;
        }
        return likedSongs.stream()
            .map(LikedSongMapper::toLikedSongDTO)
            .toList();
    }
    
}
