package org.example.model;

import java.util.HashMap;
import java.util.Map;

public class Student {
    private String studentId;
    private String name;
    private Map<String, Double> grades = new HashMap<>();

    public Student() {
    }

    public Student(String studentId, String name) {
        this.studentId = studentId;
        this.name = name;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Double> getGrades() {
        return grades;
    }

    public void setGrades(Map<String, Double> grades) {
        this.grades = grades == null ? new HashMap<>() : grades;
    }

    public void addGrade(String subject, double grade) {
        grades.put(subject, grade);
    }

    public double calculateGPA() {
        if (grades.isEmpty()) return Double.NaN;
        double avg = grades.values().stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
        if (Double.isNaN(avg)) return Double.NaN;
        if (avg >= 90) return 4.0;
        if (avg >= 80) return 3.0;
        if (avg >= 70) return 2.0;
        if (avg >= 60) return 1.0;
        return 0.0;
    }

    @Override
    public String toString() {
        return studentId + ": " + name;
    }
}
