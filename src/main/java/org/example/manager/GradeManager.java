package org.example.manager;

import org.example.model.Student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GradeManager {
    private final List<Student> students = new ArrayList<>();

    public void addStudent(Student s) {
        students.add(s);
    }

    public boolean removeStudent(String studentId) {
        Iterator<Student> it = students.iterator();
        while (it.hasNext()) {
            if (it.next().getStudentId().equals(studentId)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public Optional<Student> findById(String studentId) {
        return students.stream().filter(s -> s.getStudentId().equals(studentId)).findFirst();
    }

    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }

    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("Student Report\n");
        for (Student s : students) {
            sb.append(s.getStudentId()).append(" - ").append(s.getName()).append("\n");
            if (s.getGrades().isEmpty()) sb.append("  No grades\n");
            else {
                s.getGrades().forEach((sub, g) -> sb.append("  ").append(sub).append(": ").append(g).append("\n"));
                sb.append("  GPA: ").append(s.calculateGPA()).append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}

