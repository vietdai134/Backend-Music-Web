package com.app.Music_Web.Infrastructure.Persistence.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.Music_Web.Application.Ports.Out.GenreRepositoryPort;
import com.app.Music_Web.Domain.Entities.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre,Long>,GenreRepositoryPort{

}
