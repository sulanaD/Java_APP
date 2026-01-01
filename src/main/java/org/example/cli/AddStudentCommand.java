package org.example.cli;

import org.example.model.Student;
import org.example.service.GradeService;

public class AddStudentCommand implements Command {
    private final GradeService service;

    public AddStudentCommand(GradeService service) { this.service = service; }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) { System.out.println("Usage: add-student <id> <name>"); return; }
        Student s = new Student(args[0], args[1]);
        boolean ok = service.addStudent(s);
        System.out.println(ok ? "Added" : "Invalid student data");
    }

    @Override
    public String getHelp() { return "Add a new student"; }
}

