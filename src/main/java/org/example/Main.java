package org.example;

import org.example.cli.AddGradeCommand;
import org.example.cli.CLIHandler;
import org.example.cli.ListStudentsCommand;
import org.example.cli.AddStudentCommand;
import org.example.cli.RemoveStudentCommand;
import org.example.cli.TranscriptCommand;
import org.example.io.FileHandler;
import org.example.manager.GradeManager;
import org.example.model.Student;
import org.example.repo.GradeRepository;
import org.example.repo.JsonPersistenceAdapter;
import org.example.service.GradeService;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String DEFAULT_FILE = "students.json";

    public static void main(String[] args) throws Exception {
        GradeManager gm = new GradeManager();
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
        cli.register("add-grade", new AddGradeCommand(gs));
        cli.register("add-student", new AddStudentCommand(gs));
        cli.register("remove-student", new RemoveStudentCommand(gs));
        cli.register("transcript", new TranscriptCommand(gs));
        cli.register("class-report", args1 -> System.out.println(gs.classReport()));
        // keep backward compatibility: provide simple save/load commands
        cli.register("save", args1 -> { try { repo.save(dataFile); System.out.println("Saved"); } catch (Exception e) { System.out.println("Failed to save: " + e.getMessage()); } });
        cli.register("load", args1 -> { try { repo.load(dataFile); System.out.println("Loaded"); } catch (Exception e) { System.out.println("Failed to load: " + e.getMessage()); } });

        if (args != null && args.length > 0) {
            // existing non-interactive commands preserved
            String cmd = args[0];
            switch (cmd) {
                case "list":
                    repo.findAll().forEach(s -> System.out.println(s));
                    return;
                case "remove-student":
                    if (args.length < 2) { System.err.println("Usage: remove-student <id>"); System.exit(2); }
                    boolean removed = gm.removeStudent(args[1]);
                    if (removed) {
                        fh.saveToFile(dataFile, gm.getStudents());
                        System.out.println("Removed and saved");
                    } else {
                        System.out.println("Not found");
                    }
                    return;
                case "add-student":
                    if (args.length < 3) { System.err.println("Usage: add-student <studentId> <name>"); System.exit(2); }
                    gm.addStudent(new Student(args[1], args[2]));
                    fh.saveToFile(dataFile, gm.getStudents());
                    System.out.println("Added and saved");
                    return;
                case "add-grade":
                    if (args.length < 4) { System.err.println("Usage: add-grade <studentId> <subject> <grade>"); System.exit(2); }
                    gm.findById(args[1]).ifPresentOrElse(st -> { st.addGrade(args[2], Double.parseDouble(args[3])); System.out.println("Grade added"); }, () -> System.out.println("Student not found"));
                    // persist change
                    fh.saveToFile(dataFile, gm.getStudents());
                    return;
                case "save":
                    fh.saveToFile(dataFile, gm.getStudents());
                    System.out.println("Saved");
                    return;
                case "load":
                    List<Student> list = fh.loadFromFile(dataFile);
                    // clear current and add
                    list.forEach(gm::addStudent);
                    System.out.println("Loaded");
                    return;
                case "help":
                    printHelp();
                    return;
                case "transcript":
                    if (args.length < 2) { System.err.println("Usage: transcript <id>"); System.exit(2); }
                    gm.findById(args[1]).ifPresentOrElse(st -> System.out.println(st.calculateGPA() + " | Grades: " + st.getGrades()), () -> System.out.println("Not found"));
                    return;
                case "class-report":
                    System.out.println(gm.generateReport());
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
        System.out.println("Commands: list, add-student <id> <name>, add-grade <id> <sub> <grade>, save, load, help");
    }
}
