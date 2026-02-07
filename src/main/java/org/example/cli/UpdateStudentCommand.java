package org.example.cli;

import org.example.service.GradeService;

public class UpdateStudentCommand implements Command {
    private final GradeService service;

    public UpdateStudentCommand(GradeService service) { this.service = service; }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) { System.out.println("Usage: update-student <id> <newName>"); return; }
        boolean ok = service.updateStudent(args[0], args[1]);
        System.out.println(ok ? "Updated" : "Not found");
    }

    @Override
    public String getHelp() { return "Update a student's name"; }
}

