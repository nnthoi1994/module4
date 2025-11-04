package com.example.ordermeal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_state")
public class AppState {
    @Id
    private int id;

    @Column(nullable = false)
    private boolean isOrderingLocked;
}