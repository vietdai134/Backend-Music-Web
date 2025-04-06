package com.app.Music_Web.Application.Services;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.Music_Web.Application.DTO.SongRedisDTO;
import com.app.Music_Web.Application.Ports.In.Public.FindService;
import com.app.Music_Web.Application.Ports.Out.SongRepositoryPort;

@Service
public class PublicServiceImpl implements FindService{
    private final SongRepositoryPort songRepositoryPort;

    public PublicServiceImpl (SongRepositoryPort songRepositoryPort) {
        this.songRepositoryPort = songRepositoryPort;
    }

    @Override
    public Page<SongRedisDTO> findAllSong(Pageable pageable) {
        Page<Object[]> result=songRepositoryPort.findAllSongWithApproved(pageable);

        return result.map(row->{
            return SongRedisDTO.builder()
                .songId((Long) row[0])
                .title((String) row[1])
                .artist((String) row[2])
                .songImage((String) row[3])
                .fileSongId((String) row[4])
                .downloadable((Boolean) row[5])
                .uploadDate((Date) row[6])
                .userName((String) row[7])
                // .genres(new ArrayList<>(List.of(GenreDTO.builder().genreName((String) row[8]).build())))
                .genresName((String)row[8])
                .build();
        });
    }
    
}
