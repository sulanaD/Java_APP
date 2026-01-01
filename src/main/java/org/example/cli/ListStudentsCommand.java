package org.example.cli;

import org.example.model.Student;
import org.example.service.GradeService;

import java.util.List;

public class ListStudentsCommand implements Command {
    private final GradeService service;

    public ListStudentsCommand(GradeService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args) {
        List<Student> list = service.listStudents();
        list.forEach(s -> System.out.println(s));
    }

    @Override
    public String getHelp() {
        return "List all students";
    }
}

