package com.example.form_dang_ky.repository;

import com.example.form_dang_ky.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISongRepository extends JpaRepository<Song, Integer> {
}
