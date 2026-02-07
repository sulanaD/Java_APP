package org.example;

import org.example.cli.*;
import org.example.io.FileHandler;
import org.example.model.Student;
import org.example.repo.GradeRepository;
import org.example.repo.JsonPersistenceAdapter;
import org.example.service.GradeService;

import java.io.File;
import java.util.List;

public class Main {
    private static final String DEFAULT_FILE = "students.json";

    public static void main(String[] args) throws Exception {
        FileHandler fh = new FileHandler();
        GradeRepository repo = new GradeRepository(new JsonPersistenceAdapter(fh));
        GradeService gs = new GradeService(repo);
        File dataFile = new File(DEFAULT_FILE);

        // load via repository
        repo.load(dataFile);

        // if empty add demo
        if (repo.findAll().isEmpty()) {
            repo.add(new Student("S1", "Alice Smith"));
            repo.add(new Student("S2", "Bob Johnson"));
        }

        // register CLI commands
        CLIHandler cli = new CLIHandler();
        cli.register("list", new ListStudentsCommand(gs));
        // mutating commands: wrap to save after mutation
        cli.register("add-grade", (args1) -> {
            new AddGradeCommand(gs).execute(args1);
            try { repo.save(dataFile); } catch (Exception e) { System.out.println("Failed to save: " + e.getMessage()); }
        });
        cli.register("update-grade", (args1) -> {
            new UpdateGradeCommand(gs).execute(args1);
            try { repo.save(dataFile); } catch (Exception e) { System.out.println("Failed to save: " + e.getMessage()); }
        });
        cli.register("add-student", (args1) -> {
            new AddStudentCommand(gs).execute(args1);
            try { repo.save(dataFile); } catch (Exception e) { System.out.println("Failed to save: " + e.getMessage()); }
        });
        cli.register("remove-student", (args1) -> {
            new RemoveStudentCommand(gs).execute(args1);
            try { repo.save(dataFile); } catch (Exception e) { System.out.println("Failed to save: " + e.getMessage()); }
        });
        cli.register("transcript", new TranscriptCommand(gs));
        cli.register("update-student", (args1) -> {
            new UpdateStudentCommand(gs).execute(args1);
            try { repo.save(dataFile); } catch (Exception e) { System.out.println("Failed to save: " + e.getMessage()); }
        });
        cli.register("remove-grade", (args1) -> {
            new RemoveGradeCommand(gs).execute(args1);
            try { repo.save(dataFile); } catch (Exception e) { System.out.println("Failed to save: " + e.getMessage()); }
        });
        cli.register("class-report", args1 -> System.out.println(gs.classReport()));
        // keep backward compatibility: provide simple save/load commands
        cli.register("save", args1 -> { try { repo.save(dataFile); System.out.println("Saved"); } catch (Exception e) { System.out.println("Failed to save: " + e.getMessage()); } });
        cli.register("load", args1 -> { try { repo.load(dataFile); System.out.println("Loaded"); } catch (Exception e) { System.out.println("Failed to load: " + e.getMessage()); } });

        if (args != null && args.length > 0) {
            // non-interactive commands now use GradeService and repository consistently
            String cmd = args[0];
            switch (cmd) {
                case "list":
                    repo.findAll().forEach(s -> System.out.println(s));
                    return;
                case "remove-student":
                    if (args.length < 2) { System.err.println("Usage: remove-student <id>"); System.exit(2); }
                    boolean removed = gs.removeStudent(args[1]);
                    if (removed) {
                        repo.save(dataFile);
                        System.out.println("Removed and saved");
                    } else {
                        System.out.println("Not found");
                    }
                    return;
                case "add-student":
                    if (args.length < 3) { System.err.println("Usage: add-student <studentId> <name>"); System.exit(2); }
                    gs.addStudent(new Student(args[1], args[2]));
                    repo.save(dataFile);
                    System.out.println("Added and saved");
                    return;
                case "add-grade":
                    if (args.length < 4) { System.err.println("Usage: add-grade <studentId> <subject> <grade>"); System.exit(2); }
                    try {
                        double g = Double.parseDouble(args[3]);
                        boolean ok = gs.addGrade(args[1], args[2], g);
                        if (ok) {
                            repo.save(dataFile);
                            System.out.println("Grade added and saved");
                        } else System.out.println("Student not found");
                    } catch (NumberFormatException nfe) { System.err.println("Invalid grade"); System.exit(2); }
                    return;
                case "update-grade":
                    if (args.length < 4) { System.err.println("Usage: update-grade <studentId> <subject> <grade>"); System.exit(2); }
                    try {
                        double ug = Double.parseDouble(args[3]);
                        boolean uok = gs.updateGrade(args[1], args[2], ug);
                        if (uok) { repo.save(dataFile); System.out.println("Grade updated and saved"); } else System.out.println("Not found");
                    } catch (NumberFormatException nfe) { System.err.println("Invalid grade"); System.exit(2); }
                    return;
                case "update-student":
                    if (args.length < 3) { System.err.println("Usage: update-student <id> <newName>"); System.exit(2); }
                    boolean uok = gs.updateStudent(args[1], args[2]);
                    if (uok) { repo.save(dataFile); System.out.println("Updated and saved"); } else System.out.println("Not found");
                    return;
                case "remove-grade":
                    if (args.length < 3) { System.err.println("Usage: remove-grade <studentId> <subject>"); System.exit(2); }
                    boolean rg = gs.removeGrade(args[1], args[2]);
                    if (rg) { repo.save(dataFile); System.out.println("Grade removed and saved"); } else System.out.println("Not found");
                    return;
                case "save":
                    repo.save(dataFile);
                    System.out.println("Saved");
                    return;
                case "load":
                    repo.load(dataFile);
                    System.out.println("Loaded");
                    return;
                case "help":
                    printHelp();
                    return;
                case "transcript":
                    if (args.length < 2) { System.err.println("Usage: transcript <id>"); System.exit(2); }
                    gs.findById(args[1]).ifPresentOrElse(st -> System.out.println(st.calculateGPA() + " | Grades: " + st.getGrades()), () -> System.out.println("Not found"));
                    return;
                case "class-report":
                    System.out.println(gs.classReport());
                    return;
                default:
                    System.err.println("Unknown command");
                    printHelp();
                    return;
            }
        }

        // start CLI interactive mode
        cli.start();

        // on exit save
        repo.save(dataFile);
        System.out.println("Bye");
    }

    private static void printHelp() {
        System.out.println("Commands: list, add-student <id> <name>, add-grade <id> <sub> <grade>, update-grade <id> <sub> <grade>, update-student <id> <newName>, remove-grade <id> <sub>, save, load, help");
    }
}
