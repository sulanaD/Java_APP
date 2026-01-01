package org.example.service;

import org.example.model.Student;
import org.example.repo.GradeRepository;
import org.example.service.validator.GradeValidator;
import org.example.service.validator.ValidationResult;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class GradeService {
    private final GradeRepository repository;
    private final GradeValidator validator = new GradeValidator();

    public GradeService(GradeRepository repository) {
        this.repository = repository;
    }

    public void load(File f) { repository.load(f); }
    public void save(File f) { repository.save(f); }

    public boolean addStudent(Student s) {
        ValidationResult vr = validator.validate(s);
        if (!vr.isValid()) return false;
        repository.add(s);
        return true;
    }

    public boolean removeStudent(String id) { return repository.remove(id); }
    public Optional<Student> findById(String id) { return repository.findById(id); }

    public boolean addGrade(String studentId, String subject, double grade) {
        Optional<Student> opt = repository.findById(studentId);
        if (opt.isEmpty()) return false;
        Student s = opt.get();
        s.addGrade(subject, grade);
        return true;
    }

    public List<Student> listStudents() { return repository.findAll(); }

    public String classReport() { return repository.findAll().stream().map(Student::toString).reduce("Class Report\n", (a,b) -> a + b + "\n"); }
}
