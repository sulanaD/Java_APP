package org.example.cli;

import org.example.service.GradeService;

public class UpdateGradeCommand implements Command {
    private final GradeService service;

    public UpdateGradeCommand(GradeService service) { this.service = service; }

    @Override
    public void execute(String[] args) {
        if (args.length < 3) { System.out.println("Usage: update-grade <studentId> <subject> <grade>"); return; }
        String id = args[0];
        String subject = args[1];
        double grade;
        try {
            grade = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid grade");
            return;
        }
        boolean ok = service.updateGrade(id, subject, grade);
        System.out.println(ok ? "Grade updated" : "Student not found");
    }

    @Override
    public String getHelp() { return "Update a student's grade"; }
}

