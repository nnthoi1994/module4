package com.example.quan_ly_san_pham.service;

import com.example.quan_ly_san_pham.entity.User;

import java.util.Optional;

public interface IUserService {
    User register(User user);
    Optional<User> login(String username, String password);
    Optional<User> findById(Long id);
    boolean isUsernameExists(String username);
    boolean isEmailExists(String email);
}
