package org.example.demo2.service;

import org.example.demo2.entity.Student;

import java.util.List;

public interface IStudentService {
    List<Student> findAll();
    boolean add(Student student);
    Student findById(int id);
}
