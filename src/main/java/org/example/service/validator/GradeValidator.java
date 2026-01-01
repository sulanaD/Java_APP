package org.example.service.validator;

import org.example.model.Student;

public class GradeValidator {
    public ValidationResult validate(Student s) {
        ValidationResult vr = new ValidationResult();
        if (s.getStudentId() == null || s.getStudentId().trim().isEmpty()) vr.addError("studentId required");
        if (s.getName() == null || s.getName().trim().isEmpty()) vr.addError("name required");
        return vr;
    }
}
