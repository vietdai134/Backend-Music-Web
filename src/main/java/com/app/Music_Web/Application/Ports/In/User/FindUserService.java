package com.app.Music_Web.Application.Ports.In.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.Music_Web.Application.DTO.UserDTO;
import com.app.Music_Web.Domain.ValueObjects.User.UserEmail;

public interface FindUserService {
    Page<UserDTO> findAllWithRoles(Pageable pageable);
    // Thêm phương thức tìm kiếm gần đúng với phân trang
    Page<UserDTO> searchByUserNameOrEmail(String keyword, Pageable pageable);

    UserDTO findUserEmail (UserEmail userEmail);
    UserDTO findUserById (Long userId);
}
