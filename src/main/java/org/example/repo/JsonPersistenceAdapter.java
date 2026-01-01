package org.example.repo;

import org.example.io.FileHandler;
import org.example.model.Student;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonPersistenceAdapter implements PersistenceAdapter {
    private final FileHandler fh;

    public JsonPersistenceAdapter(FileHandler fh) {
        this.fh = fh;
    }

    @Override
    public List<Student> loadAll(File f) throws IOException {
        return fh.loadFromFile(f);
    }

    @Override
    public void saveAll(File f, List<Student> data) throws IOException {
        fh.saveToFile(f, data);
    }
}

