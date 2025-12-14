package org.example.repo;

import org.example.model.Student;

import java.util.ArrayList;
import java.util.List;

public class InMemoryStudentRepo implements StudentRepo {
    @Override
    public List<Student> findAll() {
        return new ArrayList<>();
    }
}
