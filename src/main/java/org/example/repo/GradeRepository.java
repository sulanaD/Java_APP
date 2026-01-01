package org.example.repo;

import org.example.model.Student;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GradeRepository {
    private final List<Student> students = new ArrayList<>();
    private final PersistenceAdapter adapter;

    public GradeRepository(PersistenceAdapter adapter) {
        this.adapter = adapter;
    }

    public void load(File f) {
        try {
            List<Student> loaded = adapter.loadAll(f);
            students.clear();
            students.addAll(loaded);
        } catch (IOException e) {
            System.err.println("Warning: failed to load data: " + e.getMessage());
        }
    }

    public void save(File f) {
        try {
            adapter.saveAll(f, new ArrayList<>(students));
        } catch (IOException e) {
            System.err.println("Warning: failed to save data: " + e.getMessage());
        }
    }

    public void add(Student s) { students.add(s); }
    public boolean remove(String id) {
        Optional<Student> opt = students.stream().filter(s -> s.getStudentId().equals(id)).findFirst();
        if (opt.isPresent()) { students.remove(opt.get()); return true; }
        return false;
    }
    public Optional<Student> findById(String id) { return students.stream().filter(s -> s.getStudentId().equals(id)).findFirst(); }
    public List<Student> findAll() { return new ArrayList<>(students); }
}

