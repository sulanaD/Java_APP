package org.example.service.validator;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
    private final List<String> errors = new ArrayList<>();

    public void addError(String e) { errors.add(e); }
    public boolean isValid() { return errors.isEmpty(); }
    public List<String> getErrors() { return new ArrayList<>(errors); }
}

