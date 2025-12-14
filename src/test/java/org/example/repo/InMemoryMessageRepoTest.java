package org.example.repo;

import org.example.manager.GradeManager;
import org.example.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GradeManagerTest {
    private GradeManager gm;

    @BeforeEach
    void setUp() {
        gm = new GradeManager();
    }

    @Test
    void addAndRemoveStudent() {
        Student s = new Student("X1", "Test One");
        gm.addStudent(s);
        assertEquals(1, gm.getStudents().size());
        assertTrue(gm.removeStudent("X1"));
        assertEquals(0, gm.getStudents().size());
    }
}
