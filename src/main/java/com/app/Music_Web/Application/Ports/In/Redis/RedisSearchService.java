package com.app.Music_Web.Application.Ports.In.Redis;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.Music_Web.Application.DTO.SongRedisDTO;

public interface RedisSearchService {
    void createIndex();
    String searchByTitle(String title);
    
    // List<SongRedisDTO> searchSongs(String title, String artist, List<String> genres,String username,
    //                         int offset, int limit,String sortBy, String sortDirection);

    
    Page<SongRedisDTO> searchSongs(List<String> songId,String title, String artist, 
                List<String> genres, String username, Pageable pageable);
    
} 
