package com.example.quan_ly_sach.entity;

import jakarta.persistence.*;

@Entity
public class BorrowCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String code;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

}
