package com.example.form_dang_ky.service;

import com.example.form_dang_ky.entity.Song;
import com.example.form_dang_ky.repository.ISongRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService implements ISongService {
    private final ISongRepository songRepository;
    public SongService(ISongRepository songRepository) {
        this.songRepository = songRepository;
    }
    @Override
    public List<Song> findAll() {
        return songRepository.findAll();
    }

    @Override
    public void save(Song song) {
        songRepository.save(song);
    }

    @Override
    public Song findById(Integer id) {
        return songRepository.findById(id).orElse(null);
    }
}
