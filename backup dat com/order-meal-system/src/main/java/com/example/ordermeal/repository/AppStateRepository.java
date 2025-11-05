package com.example.ordermeal.repository;

import com.example.ordermeal.entity.AppState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppStateRepository extends JpaRepository<AppState, Integer> {
}