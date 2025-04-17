package com.app.Music_Web.Application.Services;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.Music_Web.Application.DTO.ListenHistoryDTO;
import com.app.Music_Web.Application.Mapper.ListenHistoryMapper;
import com.app.Music_Web.Application.Ports.In.ListenHistory.DeleteListenHistoryService;
import com.app.Music_Web.Application.Ports.In.ListenHistory.FindListenHistoryService;
import com.app.Music_Web.Application.Ports.In.ListenHistory.SaveListenHistoryService;
import com.app.Music_Web.Application.Ports.Out.ListenHistoryRepositoryPort;
import com.app.Music_Web.Application.Ports.Out.SongRepositoryPort;
import com.app.Music_Web.Application.Ports.Out.UserRepositoryPort;
import com.app.Music_Web.Domain.Entities.ListenHistory;
import com.app.Music_Web.Domain.Entities.Song;
import com.app.Music_Web.Domain.Entities.User;
import com.app.Music_Web.Domain.ValueObjects.User.UserEmail;


@Service
public class ListenHistoryServiceImpl implements SaveListenHistoryService,
                                                    DeleteListenHistoryService,
                                                    FindListenHistoryService {       
    private final ListenHistoryRepositoryPort listenHistoryRepositoryPort;
    private final SongRepositoryPort songRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    public ListenHistoryServiceImpl(
        ListenHistoryRepositoryPort listenHistoryRepositoryPort,
        SongRepositoryPort songRepositoryPort,
        UserRepositoryPort userRepositoryPort) {
        this.listenHistoryRepositoryPort = listenHistoryRepositoryPort;
        this.songRepositoryPort = songRepositoryPort;
        this.userRepositoryPort = userRepositoryPort;
    }
    @Override
    public void saveListenHistory(Long songId, String email) {
        Song song = songRepositoryPort.findBySongId(songId);
        UserEmail userEmail = new UserEmail(email);
        User user = userRepositoryPort.findByEmail(userEmail);
        ListenHistory listenHistory = listenHistoryRepositoryPort.findBySong_SongIdAndUser_UserId(songId, user.getUserId());
        if(listenHistory != null){
            listenHistory.setListenedDate(new Date());
        }else{
            listenHistory = ListenHistory.builder()
                .song(song)
                .user(user)
                .listenedDate(new Date())
                .build();
        }
        listenHistoryRepositoryPort.save(listenHistory);
    }


    @Override
    @Transactional
    public void deleteSongFromListenHistory(Long songId, String email) {
        UserEmail userEmail = new UserEmail(email);
        User user = userRepositoryPort.findByEmail(userEmail);
        listenHistoryRepositoryPort.deleteBySong_SongIdAndUser_UserId(songId, user.getUserId());
    }


    @Override
    public List<ListenHistoryDTO> findListenHistoryByUser(String email) {
        UserEmail userEmail = new UserEmail(email);
        User user = userRepositoryPort.findByEmail(userEmail);
        List<ListenHistory> listenHistories= listenHistoryRepositoryPort.findAllByUser_UserId(user.getUserId());
        if(listenHistories.isEmpty()){
            return null;
        }
        return listenHistories.stream()
            .map(ListenHistoryMapper::toListenHistoryDTO)
            .toList();
        
    }

}
