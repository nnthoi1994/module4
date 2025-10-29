package org.example.demo2.repository;

import org.example.demo2.entity.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentRepository implements IStudentRepository{
    private static List<Student> studentList = new ArrayList<>();
    static {
        studentList.add(new Student(1, "Dung", true, List.of("Java", "C++"), "C06"));
        studentList.add(new Student(2, "Thu", false, List.of("Python", "C#"), "C07"));
    }
    @Override
    public List<Student> findAll() {
        return studentList;
    }

    @Override
    public boolean add(Student student) {
        return studentList.add(student);
    }

    @Override
    public Student findById(int id) {
        return studentList.stream().filter(s->s.getId()==id)
                .findFirst().orElse(null);
    }
}
