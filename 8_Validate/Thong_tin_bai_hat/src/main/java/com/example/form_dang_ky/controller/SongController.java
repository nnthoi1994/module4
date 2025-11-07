package com.example.form_dang_ky.controller;

import com.example.form_dang_ky.dto.SongDto;

import com.example.form_dang_ky.entity.Song;
import com.example.form_dang_ky.service.ISongService;

import com.example.form_dang_ky.validate.SongValidator;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/songs")
public class SongController {
    private final ISongService songService;
    public SongController(ISongService songService) {
        this.songService = songService;
    }
    @GetMapping("")
    public String showList(Model model) {
        model.addAttribute("songs", songService.findAll());
        return "song/list";
    }
    @GetMapping("/create")
    public String showCreate(Model model) {
        model.addAttribute("songDto", new SongDto());
        return "song/form";
    }
    @PostMapping("/save")
    public String saveSong(@Valid @ModelAttribute SongDto songDto,
                           BindingResult bindingResult) {
        SongValidator songValidator = new SongValidator();
        songValidator.validate(songDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "song/form";
        }
        Song song = new Song();
        BeanUtils.copyProperties(songDto, song);
        songService.save(song);
        return "redirect:/songs";
    }
    @GetMapping("/edit/{id}")
    public String showEdit(@PathVariable Integer id, Model model) {
        Song song = songService.findById(id);
        SongDto dto = new SongDto();
        BeanUtils.copyProperties(song, dto);
        model.addAttribute("songDto", dto);
        model.addAttribute("id", id);
        return "song/form";
    }
    @PostMapping("/update/{id}")
    public String update(@PathVariable Integer id,
                         @Valid @ModelAttribute("songDto") SongDto songDto,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "song/form";
        }
        Song song = new Song();
        BeanUtils.copyProperties(songDto, song);
        song.setId(id);
        songService.save(song);
        return "redirect:/songs";
    }
}
