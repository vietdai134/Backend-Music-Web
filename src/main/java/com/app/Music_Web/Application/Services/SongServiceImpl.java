package com.app.Music_Web.Application.Services;

import com.app.Music_Web.Application.Ports.In.Song.SaveSongService;
import com.app.Music_Web.Application.Ports.In.Song.FindSongsByGenreService;
import com.app.Music_Web.Application.Ports.Out.SongRepositoryPort;
import com.app.Music_Web.Application.DTO.SongDTO;
import com.app.Music_Web.Domain.Entities.Song;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SongServiceImpl implements SaveSongService, FindSongsByGenreService {
    private final SongRepositoryPort songRepositoryPort;

    public SongServiceImpl(SongRepositoryPort songRepositoryPort) {
        this.songRepositoryPort = songRepositoryPort;
    }

    @Override
    public SongDTO saveSong(String title, String artist, String genre) {
        Song song = new Song(title, artist, genre);
        Song savedSong = songRepositoryPort.save(song);
        return new SongDTO(savedSong.getId(), savedSong.getTitle(), savedSong.getArtist(), savedSong.getGenre());
    }

    @Override
    public List<SongDTO> findSongsByGenre(String genre) {
        List<Song> songs = songRepositoryPort.findByGenre(genre);
        return songs.stream()
                    .map(song -> new SongDTO(song.getId(), song.getTitle(), song.getArtist(), song.getGenre()))
                    .collect(Collectors.toList());
    }
}