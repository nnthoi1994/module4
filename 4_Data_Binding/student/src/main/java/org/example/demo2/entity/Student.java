package org.example.demo2.entity;

import java.util.List;

public class Student {
    private int id;
    private String name;
    private boolean gender;
    private List<String> subjects;
    private String className;

    public Student() {
    }

    public Student(int id, String name, boolean gender, List<String> subjects, String className) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.subjects = subjects;
        this.className = className;
    }

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
