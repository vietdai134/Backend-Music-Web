package com.app.Music_Web.Application.DTO;
import java.util.Date;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionDTO {
    private Long permissionId;
    private String permissionName;
    private String description;

    private Date assignedDate;
}
