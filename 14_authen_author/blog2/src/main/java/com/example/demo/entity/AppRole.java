package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "app_role", uniqueConstraints = @UniqueConstraint(name = "APP_ROLE_UK", columnNames = "role_name"))
public class AppRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "role_name", length = 30, nullable = false)
    private String roleName;
}