package com.example.quan_ly_sach.service;

import com.example.quan_ly_sach.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBookService {
    Page<Book> listBooks(Pageable pageable);
    Book getBookById(Integer id);
    String borrowBook(Integer bookId);
    void returnBook(String code);
    Book getBookByBorrowCode(String code);
}