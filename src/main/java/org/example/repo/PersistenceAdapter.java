package org.example.repo;

import org.example.model.Student;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface PersistenceAdapter {
    List<Student> loadAll(File f) throws IOException;
    void saveAll(File f, List<Student> data) throws IOException;
}

