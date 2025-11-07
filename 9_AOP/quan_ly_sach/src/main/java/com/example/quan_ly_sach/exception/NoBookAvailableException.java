package com.example.quan_ly_sach.exception;

public class NoBookAvailableException extends RuntimeException {
    public NoBookAvailableException(String message) {
        super(message);
    }
}