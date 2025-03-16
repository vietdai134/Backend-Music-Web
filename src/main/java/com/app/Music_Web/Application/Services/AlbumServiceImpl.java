package com.app.Music_Web.Application.Services;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.Music_Web.Application.DTO.AlbumDTO;
import com.app.Music_Web.Application.Mapper.AlbumMapper;
import com.app.Music_Web.Application.Ports.In.Album.FindAlbumService;
import com.app.Music_Web.Application.Ports.In.Album.SaveAlbumService;
import com.app.Music_Web.Application.Ports.Out.AlbumRepositoryPort;
import com.app.Music_Web.Application.Ports.Out.UserRepositoryPort;
import com.app.Music_Web.Domain.Entities.Album;
import com.app.Music_Web.Domain.Entities.User;
import com.app.Music_Web.Domain.ValueObjects.Album.AlbumName;

@Service
public class AlbumServiceImpl implements SaveAlbumService, FindAlbumService {
    private final AlbumRepositoryPort albumRepositoryPort;
    private final UserRepositoryPort  userRepositoryPort;
    public AlbumServiceImpl(AlbumRepositoryPort albumRepositoryPort, UserRepositoryPort userRepositoryPort){
        this.userRepositoryPort=userRepositoryPort;
        this.albumRepositoryPort=albumRepositoryPort;
    }
    @Override
    public AlbumDTO saveAlbum(String albumName, String albumImage, Long userId) {
        User user = userRepositoryPort.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
        Album album = Album.builder()
                            .albumName(new AlbumName(albumName))
                            .albumImage(albumImage)
                            .createdDate(new Date())
                            .user(user)
                            .build();
        album = albumRepositoryPort.save(album);
        return AlbumMapper.toAlbumDTO(album);
    }
    @Override
    public Page<AlbumDTO> findByUser_UserId(Pageable pageable, Long userId) {
       Page<Album> albums=albumRepositoryPort.findByUser_UserId(pageable, userId);
       return albums.map(AlbumMapper::toAlbumDTO);
    }


    
}
