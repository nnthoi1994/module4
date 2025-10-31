package org.example.demo2.config;

import org.example.demo2.repository.StudentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public StudentRepository studentRepository(){
        return new StudentRepository();
    }
}
