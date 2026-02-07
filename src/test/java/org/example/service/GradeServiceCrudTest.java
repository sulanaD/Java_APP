package org.example.service;

import org.example.io.FileHandler;
import org.example.model.Student;
import org.example.repo.GradeRepository;
import org.example.repo.JsonPersistenceAdapter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class GradeServiceCrudTest {

    private File tmpFile;

    @AfterEach
    void cleanup() {
        if (tmpFile != null && tmpFile.exists()) tmpFile.delete();
    }

    @Test
    void fullCrudFlow() throws Exception {
        tmpFile = File.createTempFile("students-test", ".json");
        tmpFile.deleteOnExit();

        FileHandler fh = new FileHandler();
        GradeRepository repo = new GradeRepository(new JsonPersistenceAdapter(fh));
        GradeService svc = new GradeService(repo);

        // initially empty
        repo.load(tmpFile);
        assertTrue(repo.findAll().isEmpty());

        // add student
        Student s = new Student("T1", "Tester");
        assertTrue(svc.addStudent(s));
        svc.save(tmpFile);
        // reload into fresh repo to validate persistence
        GradeRepository repo2 = new GradeRepository(new JsonPersistenceAdapter(fh));
        repo2.load(tmpFile);
        assertEquals(1, repo2.findAll().size());

        // add grade
        assertTrue(svc.addGrade("T1", "Math", 85.0));
        svc.save(tmpFile);
        Optional<Student> opt = repo2.findById("T1");
        // repo2 is stale; reload
        repo2.load(tmpFile);
        opt = repo2.findById("T1");
        assertTrue(opt.isPresent());
        assertEquals(85.0, opt.get().getGrades().get("Math"));

        // update student name
        assertTrue(svc.updateStudent("T1", "Tester2"));
        svc.save(tmpFile);
        repo2.load(tmpFile);
        assertEquals("Tester2", repo2.findById("T1").get().getName());

        // remove grade
        assertTrue(svc.removeGrade("T1", "Math"));
        svc.save(tmpFile);
        repo2.load(tmpFile);
        assertFalse(repo2.findById("T1").get().getGrades().containsKey("Math"));

        // remove student
        assertTrue(svc.removeStudent("T1"));
        svc.save(tmpFile);
        repo2.load(tmpFile);
        assertTrue(repo2.findAll().isEmpty());
    }
}

