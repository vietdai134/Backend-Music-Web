package com.app.Music_Web.Application.Services;

import com.app.Music_Web.Application.DTO.SongRedisDTO;
import com.app.Music_Web.Application.Ports.In.Redis.RedisDeleteService;
import com.app.Music_Web.Application.Ports.In.Redis.RedisSaveService;
import com.app.Music_Web.Application.Ports.In.Redis.RedisSearchService;
import com.app.Music_Web.Application.Ports.In.Redis.RedisUpdateService;
import com.app.Music_Web.Application.Ports.In.Song.FindSongService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.search.IndexOptions;
import redis.clients.jedis.search.IndexDefinition;
import redis.clients.jedis.search.Query;
import redis.clients.jedis.search.SearchResult;
import redis.clients.jedis.search.Schema;
import redis.clients.jedis.search.Document;

@Service
public class RedisServiceImpl implements RedisSearchService,RedisUpdateService,RedisDeleteService,RedisSaveService{
    private final JedisPooled jedis;
    private final FindSongService findSongService;

    @Autowired
    public RedisServiceImpl(JedisPooled jedis, FindSongService findSongService) {
        // Khớp với host:port Redis của bạn
        this.jedis = jedis;
        this.findSongService = findSongService;
    }

    public void createIndex() {
        try {
            try {
                jedis.ftInfo("songIdx");
                return; // Index đã tồn tại
            } catch (Exception ex) {
                // Nếu lỗi là index chưa tồn tại thì tiếp tục tạo index
                System.out.println("Index chưa tồn tại, tiếp tục tạo...");
            }

            // Define the schema
            Schema schema = new Schema()
                    .addTagField("songId")
                    .addSortableTextField("title", 1.0)
                    .addSortableTextField("artist", 1.0)
                    .addTagField("genre", ",")
                    .addSortableTextField("uploadDate", 1.0)
                    .addTagField("downloadable")
                    // .addTextField("songImage", 1.0)     // Thêm nếu cần tìm kiếm
                    // .addTextField("fileSongId", 1.0)    // Thêm nếu cần tìm kiếm
                    .addSortableTextField("userName", 1.0);     // Thêm nếu cần tìm kiếm

            // Create index options
                IndexOptions options = IndexOptions.defaultOptions()
                    .setDefinition(new IndexDefinition(IndexDefinition.Type.HASH)
                        .setPrefixes(new String[]{"song:"}));

            // Create the index
            jedis.ftCreate("songIdx", options, schema);
            System.out.println("Index đã được tạo thành công.");
        } catch (Exception e) {
            System.out.println("Index already exists or error: " + e.getMessage());
        }
    }

    public String searchByTitle(String keyword) {
        try {
            // Create a query
            Query query = new Query("@title:" + keyword);
            SearchResult result = jedis.ftSearch("songIdx", query);
            return result.toString();
        } catch (Exception e) {
            return "Search error: " + e.getMessage();
        }
    }

    public void addSong(String id, String title, String artist, 
                        String genre, String uploadDate, boolean downloadable,
                        String songImage, String fileSongId, String userName) {
        String key = "song:" + id;
        // jedis.hset(key, Map.of(
        //         "title", title,
        //         "artist", artist,
        //         "genre", genre,
        //         "uploadDate", uploadDate,
        //         "downloadable", String.valueOf(downloadable),
        //         "songImage", songImage,
        //         "fileSongId", fileSongId,
        //         "userName", userName
        // ));
        Map<String, String> map = new HashMap<>();
        if(id != null) map.put("songId", id);
        if (title != null) map.put("title", title);
        if (artist != null) map.put("artist", artist);
        if (genre != null) map.put("genre", genre);
        if (uploadDate != null) map.put("uploadDate", uploadDate);
        map.put("downloadable", String.valueOf(downloadable));
        if (songImage != null) map.put("songImage", songImage);
        if (fileSongId != null) map.put("fileSongId", fileSongId);
        if (userName != null) map.put("userName", userName);

        jedis.hset(key, map);
    }

    @Override
    public Page<SongRedisDTO> searchSongs(List<String> songIds,String title, String artist, 
                List<String> genres, String username, Pageable pageable) {
        try {
            StringBuilder queryBuilder = new StringBuilder();
            if (songIds != null && !songIds.isEmpty()) {
                String query = songIds.stream()
                                .map(id -> id) // Nếu cần escape thì xử lý ở đây
                                .collect(Collectors.joining("|"));

                String redisquery = String.format("@songId:{%s}", query);
                queryBuilder.append(redisquery).append(" ");
            }
            
            if (title != null && !title.isEmpty()) {
                queryBuilder.append("@title:(").append(title).append(") ");
            }

            if (artist != null && !artist.isEmpty()) {
                queryBuilder.append("@artist:(").append(artist).append(") ");
            }

            if (genres != null && !genres.isEmpty()) {
                for (String genre : genres) {
                    queryBuilder.append("@genre:{").append(genre).append("} ");
                }
            }

            if (username != null && !username.isEmpty()) {
                queryBuilder.append("@userName:(").append(username).append(") ");
            }

            // Tạo truy vấn RediSearch
            Query query = new Query(queryBuilder.toString().trim())
                    .limit((int) pageable.getOffset(), pageable.getPageSize());

            // Xử lý sắp xếp từ Pageable
            if (pageable.getSort().isSorted()) {
                pageable.getSort().forEach(order -> {
                    query.setSortBy(order.getProperty(), order.getDirection().isAscending());
                });
            }

            // Thực hiện tìm kiếm
            SearchResult results = jedis.ftSearch("songIdx", query);
            List<SongRedisDTO> songs = new ArrayList<>();

            for (Document doc : results.getDocuments()) {
                Map<String, Object> props = StreamSupport.stream(doc.getProperties().spliterator(), false)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                String genreString = props.getOrDefault("genre", "").toString();
                songs.add(SongRedisDTO.builder()
                        .songId(Long.valueOf(doc.getId().replace("song:", "")))
                        .title((String) props.get("title"))
                        .artist((String) props.get("artist"))
                        .songImage((String) props.get("songImage"))
                        .fileSongId((String) props.get("fileSongId"))
                        .downloadable(Boolean.parseBoolean((String) props.get("downloadable")))
                        .uploadDate(new Date())
                        .userName((String) props.get("userName"))
                        .genresName(genreString)
                        .build());
            }

            // Trả về Page
            return new PageImpl<>(songs, pageable, results.getTotalResults());

        } catch (Exception e) {
            e.printStackTrace();
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
    }

    @Override
    public void updateSong(String id, String title, String artist, String genre, String uploadDate, boolean downloadable,
                           String songImage, String fileSongId, String userName) {
        // Thực chất update giống như addSong vì Redis sẽ ghi đè
        addSong(id, title, artist, genre, uploadDate, downloadable, songImage, fileSongId, userName);
    }

    @Override
    public void deleteSong(Long id) {
        jedis.del("song:" + id);
    }

    

    @Override
    public void syncAddSong(Long songId){
        SongRedisDTO songRedisDTO = findSongService.findSongWithApproved(songId);
        if (songRedisDTO != null) {
            addSong(String.valueOf(songRedisDTO.getSongId()), songRedisDTO.getTitle(), songRedisDTO.getArtist(), 
                songRedisDTO.getGenresName(), String.valueOf(songRedisDTO.getUploadDate()), songRedisDTO.isDownloadable(),
                songRedisDTO.getSongImage(), songRedisDTO.getFileSongId(), songRedisDTO.getUserName());
        } else {
            System.out.println("Song not found with fileSongId: " + songId);
        }
    }

    @Override
    public void syncUpdateSong(Long songId) {
        SongRedisDTO songRedisDTO = findSongService.findSongWithApproved(songId);
        if (songRedisDTO != null) {
            updateSong(String.valueOf(songRedisDTO.getSongId()), songRedisDTO.getTitle(), songRedisDTO.getArtist(), 
                songRedisDTO.getGenresName(), String.valueOf(songRedisDTO.getUploadDate()), songRedisDTO.isDownloadable(),
                songRedisDTO.getSongImage(), songRedisDTO.getFileSongId(), songRedisDTO.getUserName());
        } else {
            System.out.println("Song not found with fileSongId: " + songId);
        }
    }

}
