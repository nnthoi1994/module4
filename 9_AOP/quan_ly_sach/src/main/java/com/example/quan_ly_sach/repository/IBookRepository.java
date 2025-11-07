package com.example.quan_ly_sach.repository;

import com.example.quan_ly_sach.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;



public interface IBookRepository extends JpaRepository<Book,Integer> {
}