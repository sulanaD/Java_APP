package org.example.repo;

import org.example.model.Student;

import java.util.List;

public interface StudentRepo {
    // Legacy repository interface - not used by new GradeManager-based CLI.
    List<Student> findAll();
    // other methods intentionally omitted
}
