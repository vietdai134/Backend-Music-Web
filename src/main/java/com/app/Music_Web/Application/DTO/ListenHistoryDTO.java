package com.app.Music_Web.Application.DTO;

import java.util.Date;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListenHistoryDTO {
    private Long historyId;
    private Date listenedDate;
    private Long songId;
    private Long userId;
}
