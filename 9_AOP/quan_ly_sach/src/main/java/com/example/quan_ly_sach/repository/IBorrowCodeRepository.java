package com.example.quan_ly_sach.repository;

import com.example.quan_ly_sach.entity.BorrowCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBorrowCodeRepository extends JpaRepository<BorrowCode,Integer> {
    BorrowCode findByCode(String code);
}