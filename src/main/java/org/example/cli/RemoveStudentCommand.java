package org.example.cli;

import org.example.service.GradeService;

public class RemoveStudentCommand implements Command {
    private final GradeService service;

    public RemoveStudentCommand(GradeService service) { this.service = service; }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) { System.out.println("Usage: remove-student <id>"); return; }
        boolean ok = service.removeStudent(args[0]);
        System.out.println(ok ? "Removed" : "Not found");
    }

    @Override
    public String getHelp() { return "Remove a student by id"; }
}

