package org.example.demo2.service;

import org.example.demo2.entity.Student;
import org.example.demo2.repository.IStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class StudentService implements IStudentService{
    @Autowired
    private IStudentRepository studentRepository;
    @Override
    public List<Student> findAll() {
        return List.of();
    }

    @Override
    public boolean add(Student student) {
        return false;
    }

    @Override
    public Student findById(int id) {
        return null;
    }
}
