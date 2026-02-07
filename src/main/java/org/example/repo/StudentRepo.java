package org.example.repo;

import org.example.model.Student;

import java.util.List;

public interface StudentRepo {
    List<Student> findAll();
}
