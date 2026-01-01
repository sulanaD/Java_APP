package org.example.cli;

import org.example.model.Student;
import org.example.service.GradeService;

public class TranscriptCommand implements Command {
    private final GradeService service;

    public TranscriptCommand(GradeService service) { this.service = service; }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) { System.out.println("Usage: transcript <id>"); return; }
        service.findById(args[0]).ifPresentOrElse(st -> System.out.println("GPA: " + st.calculateGPA() + "\nGrades: " + st.getGrades()), () -> System.out.println("Not found"));
    }

    @Override
    public String getHelp() { return "Show a student's transcript"; }
}

