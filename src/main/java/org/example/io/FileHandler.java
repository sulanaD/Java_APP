package org.example.io;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.example.model.Student;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private final ObjectMapper mapper = new ObjectMapper();

    public FileHandler() {
        // tolerate unknown fields from older/other formats
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public void saveToFile(File f, List<Student> data) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(f, data);
    }

    public List<Student> loadFromFile(File f) throws IOException {
        if (!f.exists()) return new ArrayList<>();
        try {
            CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, Student.class);
            return mapper.readValue(f, listType);
        } catch (Exception e) {
            // If file content is incompatible (legacy format), log and return empty list so app can continue
            System.err.println("Warning: failed to read '" + f.getAbsolutePath() + "' - " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
