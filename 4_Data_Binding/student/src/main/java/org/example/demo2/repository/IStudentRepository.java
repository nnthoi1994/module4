package org.example.demo2.repository;

import org.example.demo2.entity.Student;

import java.util.List;

public interface IStudentRepository {
    List<Student> findAll();
    boolean add(Student student);
    Student findById(int id);
}
