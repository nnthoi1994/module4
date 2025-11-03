package com.lunchorder.service;

import com.lunchorder.model.User;
import com.lunchorder.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Hash password
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    public Optional<User> login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (BCrypt.checkpw(password, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findCustomers() {
        return userRepository.findByRole(User.Role.CUSTOMER);
    }

    public List<User> findMaleCustomers() {
        return userRepository.findByGenderAndRole(User.Gender.MALE, User.Role.CUSTOMER);
    }

    public void initializeAdminAccount() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setFullName("Administrator");
            admin.setUsername("admin");
            admin.setPassword(BCrypt.hashpw("admin", BCrypt.gensalt()));
            admin.setGender(User.Gender.MALE);
            admin.setEmail("admin@lunchorder.com");
            admin.setRole(User.Role.ADMIN);
            userRepository.save(admin);
        }
    }
}