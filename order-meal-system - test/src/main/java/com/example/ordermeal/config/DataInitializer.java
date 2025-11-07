package com.example.ordermeal.config;

import com.example.ordermeal.entity.User;
import com.example.ordermeal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        // Tạo tài khoản admin mặc định nếu chưa tồn tại
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin");
            admin.setFullName("Administrator");
            admin.setGender("MALE");
            admin.setEmail("admin@ordermeal.com");
            admin.setRole("ADMIN");

            userRepository.save(admin);
            System.out.println("✅ Tạo tài khoản admin thành công!");
            System.out.println("   Username: admin");
            System.out.println("   Password: admin");
        }
    }
}