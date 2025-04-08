package com.app.Music_Web.Application.Services;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.Music_Web.Application.DTO.PlaylistDTO;
import com.app.Music_Web.Application.DTO.PlaylistSongDTO;
import com.app.Music_Web.Application.Mapper.PlaylistMapper;
import com.app.Music_Web.Application.Ports.In.Playlist.DeletePlaylistService;
import com.app.Music_Web.Application.Ports.In.Playlist.FindPlaylistService;
import com.app.Music_Web.Application.Ports.In.Playlist.SavePlaylistService;
import com.app.Music_Web.Application.Ports.In.Playlist.UpdatePlaylistService;
import com.app.Music_Web.Application.Ports.Out.PlaylistRepositoryPort;
import com.app.Music_Web.Application.Ports.Out.PlaylistSongRepositoryPort;
import com.app.Music_Web.Application.Ports.Out.SongRepositoryPort;
import com.app.Music_Web.Application.Ports.Out.UserRepositoryPort;
import com.app.Music_Web.Domain.Entities.Playlist;
import com.app.Music_Web.Domain.Entities.PlaylistSong;
import com.app.Music_Web.Domain.Entities.Song;
import com.app.Music_Web.Domain.Entities.User;
import com.app.Music_Web.Domain.ValueObjects.User.UserEmail;

@Service
public class PlaylistServiceImpl implements SavePlaylistService,
                                            FindPlaylistService,
                                            DeletePlaylistService,
                                            UpdatePlaylistService{
    private final PlaylistRepositoryPort playlistRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final PlaylistSongRepositoryPort playlistSongRepositoryPort;
    private final SongRepositoryPort songRepositoryPort;
    public PlaylistServiceImpl(
        PlaylistRepositoryPort playlistRepositoryPort,
        UserRepositoryPort userRepositoryPort,
        PlaylistSongRepositoryPort playlistSongRepositoryPort,
        SongRepositoryPort songRepositoryPort
        ) {
        this.playlistRepositoryPort = playlistRepositoryPort;
        this.userRepositoryPort=userRepositoryPort;
        this.playlistSongRepositoryPort=playlistSongRepositoryPort;
        this.songRepositoryPort=songRepositoryPort;
    }
    @Override
    public PlaylistDTO createPlaylist(String playListName, String email) {
        UserEmail userEmail = new UserEmail(email);
        User user = userRepositoryPort.findByEmail(userEmail);
        
        Playlist playlist= Playlist.builder()
                .playlistName(playListName)
                .user(user)
                .createdDate(new Date())
                .build();
        Playlist savedPlaylist = playlistRepositoryPort.save(playlist);
        return PlaylistMapper.toDTO(savedPlaylist);
    }
    @Override
    public List<PlaylistDTO> findPlaylistByUserId(String email) {
        UserEmail userEmail = new UserEmail(email);
        User user = userRepositoryPort.findByEmail(userEmail);

        List<Playlist> playlist=playlistRepositoryPort.findByUser_UserId(user.getUserId());
        return playlist.stream()
                       .map(PlaylistMapper::toDTO)
                       .toList();
    }
    @Override
    public void saveSongToPlaylist(Long songId, Long playlistId) {
        Song song = songRepositoryPort.findBySongId(songId);
        Playlist playlist=playlistRepositoryPort.findByPlaylistId(playlistId);
        boolean exists=playlistSongRepositoryPort.existsBySong_SongIdAndPlaylist_PlaylistId(songId, playlistId);
        if(!exists){
            PlaylistSong playlistSong =PlaylistSong.builder()
                                .song(song)
                                .playlist(playlist)
                                .build();
            playlistSongRepositoryPort.save(playlistSong);
        }
        
    }

    @Override
    public void saveMultipleSongsToPlaylist(List<Long> songIds, Long playlistId) {
        Playlist playlist = playlistRepositoryPort.findByPlaylistId(playlistId);
        for (Long songId : songIds) {
            Song song = songRepositoryPort.findBySongId(songId);

            boolean exists=playlistSongRepositoryPort.existsBySong_SongIdAndPlaylist_PlaylistId(songId, playlistId);
            if(!exists){
                PlaylistSong playlistSong = PlaylistSong.builder()
                    .song(song)
                    .playlist(playlist)
                    .build();
                playlistSongRepositoryPort.save(playlistSong);
            }
            
        }
    }

    @Override
    @Transactional
    public void deletePlaylist(Long playlistId) {
        playlistRepositoryPort.deleteByPlaylistId(playlistId);
    }
    @Override
    @Transactional
    public void deleteSongFromPlaylist(Long songId, Long playlistId) {
        playlistSongRepositoryPort.deleteBySong_SongIdAndPlaylist_PlaylistId(songId, playlistId);
    }

    @Override
    @Transactional
    public void updatePlaylistName(Long playlistId, String newPlaylistName) {
        playlistRepositoryPort.updatePlaylistName(playlistId, newPlaylistName);
    }
    @Override
    public List<PlaylistSongDTO> findSongIdsByPlaylistId(Long playlistId) {
        List<PlaylistSong> playlistSongs = playlistSongRepositoryPort.findByPlaylist_PlaylistId(playlistId);
        List<PlaylistSongDTO> playlistSongDTOs = playlistSongs.stream()
                .map(playlistSong -> PlaylistSongDTO.builder()
                        .playlistSongId(playlistId)
                        .songId(playlistSong.getSong().getSongId())
                        .playlistId(playlistSong.getPlaylist().getPlaylistId())
                        .build())
                .toList();
        return playlistSongDTOs;
    }
}
