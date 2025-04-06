package com.app.Music_Web.Infrastructure.Config.DataSeed;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.app.Music_Web.Application.DTO.SongRedisDTO;
import com.app.Music_Web.Application.Ports.In.Public.FindService;
import com.app.Music_Web.Application.Ports.In.Redis.RedisSaveService;
import com.app.Music_Web.Application.Ports.In.Redis.RedisSearchService;

@Component
public class RedisLoadData implements CommandLineRunner{
    @Autowired
    private FindService findService;

    @Autowired
    private RedisSaveService redisSaveService;
    @Autowired
    private RedisSearchService redisSearchService;

    @Override
    public void run(String... args) throws Exception {
        redisSearchService.createIndex();
         // 2. Lấy toàn bộ dữ liệu từ DB
        List<SongRedisDTO> songs = findService.findAllSong(Pageable.unpaged()).getContent();

        // 3. Nạp dữ liệu vào Redis
        for (SongRedisDTO song : songs) {
            redisSaveService.addSong(
                song.getSongId().toString(),
                song.getTitle(),
                song.getArtist(),
                song.getGenresName(),
                song.getUploadDate().toString(),
                song.isDownloadable(),
                song.getSongImage(),
                song.getFileSongId(),
                song.getUserName()
            );
        }

        System.out.println("Redis index initialized and data loaded.");
    
    }
}
