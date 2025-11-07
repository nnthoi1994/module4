package com.example.form_dang_ky.service;

import com.example.form_dang_ky.entity.Song;

import java.util.List;

public interface ISongService {
    List<Song> findAll();
    void save(Song song);
    Song findById(Integer id);
}
