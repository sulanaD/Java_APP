package org.example.cli;

import org.example.service.GradeService;

public class RemoveGradeCommand implements Command {
    private final GradeService service;

    public RemoveGradeCommand(GradeService service) { this.service = service; }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) { System.out.println("Usage: remove-grade <studentId> <subject>"); return; }
        boolean ok = service.removeGrade(args[0], args[1]);
        System.out.println(ok ? "Grade removed" : "Not found");
    }

    @Override
    public String getHelp() { return "Remove a grade from a student"; }
}

