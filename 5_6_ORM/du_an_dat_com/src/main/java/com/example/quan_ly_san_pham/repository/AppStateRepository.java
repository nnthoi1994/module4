package com.example.quan_ly_san_pham.repository;


import com.example.quan_ly_san_pham.entity.AppState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppStateRepository extends JpaRepository<AppState, Integer> {
}
