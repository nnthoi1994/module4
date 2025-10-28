package org.example.demo2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class DictionaryController {
    private static final Map<String, String> dictionary = new HashMap<>();

    static {
        dictionary.put("water", "nước");
        dictionary.put("phone", "điện thoại");
        dictionary.put("school", "trường học");
        dictionary.put("teacher", "giáo viên");
        dictionary.put("student", "học sinh");
        dictionary.put("tree", "cái cây");
        dictionary.put("city", "thành phố");
        dictionary.put("music", "âm nhạc");
        dictionary.put("food", "thức ăn");
        dictionary.put("friend", "bạn bè");
        dictionary.put("love", "tình yêu");
        dictionary.put("rain", "cơn mưa");
        dictionary.put("sun", "mặt trời");
        dictionary.put("moon", "mặt trăng");
        dictionary.put("star", "ngôi sao");
        dictionary.put("family", "gia đình");
        dictionary.put("money", "tiền");
        dictionary.put("heart", "trái tim");
        dictionary.put("dream", "giấc mơ");
        dictionary.put("coffee", "cà phê");

    }
    @GetMapping("/home")
    public String showForm() {
        return "home";
    }
    @PostMapping("/search")
    public String search(@RequestParam("word") String word, Model model) {
        String result = dictionary.get(word.toLowerCase());
        if (result != null) {
            model.addAttribute("word", word);
            model.addAttribute("result", result);
        } else {
            model.addAttribute("word", word);
            model.addAttribute("result", "Không tìm thấy từ này trong từ điển.");
        }
        return "result";
    }
}