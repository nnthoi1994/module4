package com.example.form_dang_ky.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SongDto {
    private String nameSong;
    private String singer;
    private String kindOfMusic;
}