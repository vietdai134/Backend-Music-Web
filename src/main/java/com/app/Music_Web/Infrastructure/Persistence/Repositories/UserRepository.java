package com.app.Music_Web.Infrastructure.Persistence.Repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.Music_Web.Application.Ports.Out.UserRepositoryPort;
import com.app.Music_Web.Domain.Entities.User;

public interface UserRepository extends JpaRepository<User,Long>,UserRepositoryPort {
    @Override
    @Query("SELECT DISTINCT u FROM User u " +
           "LEFT JOIN FETCH u.userRoles ur " +
           "LEFT JOIN FETCH ur.role")
    Page<User> findAllWithRoles(Pageable pageable);
    

    // Sửa truy vấn tìm kiếm gần đúng
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userRoles " +
           "WHERE LOWER(u.userName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<User> searchByUserNameOrEmail(@Param("keyword") String keyword, Pageable pageable);

    @Override
    Optional<User> findById(Long userId);
}
