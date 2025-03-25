package com.app.Music_Web.Application.Services;

import com.app.Music_Web.Application.Ports.In.Song.SaveSongService;
import com.app.Music_Web.Application.Ports.Out.SongRepositoryPort;
import com.app.Music_Web.Domain.Entities.Song;
import com.app.Music_Web.Domain.ValueObjects.Song.SongArtist;
import com.app.Music_Web.Domain.ValueObjects.Song.SongTitle;
import com.app.Music_Web.Application.DTO.SongDTO;
import com.app.Music_Web.Application.Mapper.SongMapper;
import com.app.Music_Web.Application.Ports.In.Song.DeleteSongService;
import com.app.Music_Web.Application.Ports.In.Song.FindSongService;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service
public class SongServiceImpl implements SaveSongService, FindSongService,DeleteSongService {
    private final SongRepositoryPort songRepositoryPort;
    public SongServiceImpl(SongRepositoryPort songRepositoryPort) {
        this.songRepositoryPort = songRepositoryPort;
    }

    @Override
    public Page<SongDTO> findAll(Pageable pageable) {
        Page<Song> songs = songRepositoryPort.findAll(pageable);
        return songs.map(SongMapper::toDTO);
    }

    @Override
    public SongDTO saveSong(String title, String artist, String song_image, String fileSongId, boolean downloadable) {
        Song song = Song.builder()
                        .title(new SongTitle(title))
                        .artist(new SongArtist(artist))
                        .songImage(song_image)
                        .fileSongId(fileSongId)
                        .downloadable(downloadable)
                        .build();
        Song saveSong = songRepositoryPort.save(song);
        return SongMapper.toDTO(saveSong);
    }

    @Override
    public void deleteSong(Long songId) {
        Song song = songRepositoryPort.findBySongId(songId);
        if(song==null){
            throw new RuntimeException("song not found");
        }
        songRepositoryPort.delete(song);
    }

    @Override
    public SongDTO findBySongTitle(String songTitle) {
        Song song=songRepositoryPort.findByTitle_Title(songTitle);
        if(song==null){
            throw new RuntimeException("Song not found");
        }
        return SongMapper.toDTO(song);
    }

    @Override
    public SongDTO findBySongArtist(String songArtist) {
        Song song=songRepositoryPort.findByArtist_Artist(songArtist);
        if(song==null){
            throw new RuntimeException("Song not found");
        }
        return SongMapper.toDTO(song);
    }
 
}