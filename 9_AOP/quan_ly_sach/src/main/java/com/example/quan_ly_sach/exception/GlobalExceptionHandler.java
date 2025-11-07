package com.example.quan_ly_sach.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoBookAvailableException.class)
    public String handleNoBookAvailable(NoBookAvailableException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(InvalidCodeException.class)
    public String handleInvalidCode(InvalidCodeException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGlobalException(Exception ex, Model model) {
        model.addAttribute("message", "Đã xảy ra lỗi không xác định!");
        return "error";
    }
}