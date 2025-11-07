package com.example.form_dang_ky.validate;
import com.example.form_dang_ky.dto.SongDto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class SongValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        SongDto song = (SongDto) target;
        String nameSong = song.getNameSong();
        if (nameSong == null || nameSong.trim().isEmpty()) {
            errors.rejectValue("nameSong", "name.empty", "Tên bài hát không được để trống");
        } else {
            if (nameSong.length() > 800) {
                errors.rejectValue("nameSong", "name.length", "Tên bài hát không vượt quá 800 ký tự");
            }
            if (!nameSong.matches("^[A-Za-z0-9À-ỹ\\s]+$")) {
                errors.rejectValue("nameSong", "name.format", "Tên bài hát không được chứa ký tự đặc biệt");
            }
        }
        String singer = song.getSinger();
        if (singer == null || singer.trim().isEmpty()) {
            errors.rejectValue("singer", "artist.empty", "Tên nghệ sĩ không được để trống");
        } else {
            if (singer.length() > 300) {
                errors.rejectValue("singer", "artist.length", "Tên nghệ sĩ không vượt quá 300 ký tự");
            }
            if (!singer.matches("^[A-Za-z0-9À-ỹ\\s]+$")) {
                errors.rejectValue("singer", "artist.format", "Tên nghệ sĩ không được chứa ký tự đặc biệt");
            }
        }
        String kindOfMusic = song.getKindOfMusic();
        if (kindOfMusic == null || kindOfMusic.trim().isEmpty()) {
            errors.rejectValue("kindOfMusic", "genre.empty", "Thể loại không được để trống");
        } else {
            if (kindOfMusic.length() > 1000) {
                errors.rejectValue("kindOfMusic", "genre.length", "Thể loại không vượt quá 1000 ký tự");
            }
            if (!kindOfMusic.matches("^[A-Za-z0-9À-ỹ\\s,]+$")) {
                errors.rejectValue("kindOfMusic", "genre.format", "Thể loại chỉ được chứa chữ, số, khoảng trắng và dấu phẩy");
            }
        }
    }
}
