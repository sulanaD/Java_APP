package org.example.cli;

import org.example.model.Student;
import org.example.service.GradeService;

public class AddGradeCommand implements Command {
    private final GradeService service;

    public AddGradeCommand(GradeService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: add-grade <studentId> <subject> <grade>");
            return;
        }
        String id = args[0];
        String subject = args[1];
        double grade;
        try {
            grade = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid grade");
            return;
        }
        boolean res = service.addGrade(id, subject, grade);
        System.out.println(res ? "Grade added" : "Student not found");
    }

    @Override
    public String getHelp() {
        return "Add a grade to a student";
    }
}

