package org.example.repo;

import org.example.io.FileHandler;
import org.example.manager.GradeManager;
import org.example.model.Student;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileHandlerTest {

    @Test
    void saveAndLoad() throws Exception {
        GradeManager gm = new GradeManager();
        gm.addStudent(new Student("A1", "Alice"));
        FileHandler fh = new FileHandler();
        File tmp = File.createTempFile("students", ".json");
        tmp.deleteOnExit();
        fh.saveToFile(tmp, gm.getStudents());
        List<Student> loaded = fh.loadFromFile(tmp);
        assertEquals(1, loaded.size());
        assertEquals("A1", loaded.get(0).getStudentId());
    }
}
