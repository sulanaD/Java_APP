package org.example;

import org.example.io.FileHandler;
import org.example.manager.GradeManager;
import org.example.model.Student;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String DEFAULT_FILE = "students.json";

    public static void main(String[] args) throws Exception {
        GradeManager gm = new GradeManager();
        FileHandler fh = new FileHandler();
        File dataFile = new File(DEFAULT_FILE);

        // load
        List<Student> loaded = fh.loadFromFile(dataFile);
        loaded.forEach(gm::addStudent);

        // if empty add demo
        if (gm.getStudents().isEmpty()) {
            gm.addStudent(new Student("S1", "Alice Smith"));
            gm.addStudent(new Student("S2", "Bob Johnson"));
        }

        if (args != null && args.length > 0) {
            // non-interactive commands remain supported
            String cmd = args[0];
            switch (cmd) {
                case "list":
                    gm.getStudents().forEach(s -> System.out.println(s));
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

        // interactive loop - will continue until exit chosen
        System.out.println("Simple Student & Grade Manager (CLI)");
        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("Choose an option:");
            System.out.println("  1) List students");
            System.out.println("  2) Add student");
            System.out.println("  3) Remove student");
            System.out.println("  4) Add grade to student");
            System.out.println("  5) Student transcript (GPA and grades)");
            System.out.println("  6) Class report (all students)");
            System.out.println("  7) Save to file");
            System.out.println("  8) Load from file");
            System.out.println("  9) Exit (save and quit)");
            System.out.print("Enter choice (1-9): ");
            String cmd = sc.nextLine().trim();
            switch (cmd) {
                case "1":
                    gm.getStudents().forEach(s -> System.out.println(s));
                    break;
                case "2":
                    System.out.print("Student ID: "); String id = sc.nextLine().trim();
                    System.out.print("Name: "); String name = sc.nextLine().trim();
                    gm.addStudent(new Student(id, name));
                    System.out.println("Added");
                    break;
                case "3":
                    System.out.print("Student ID to remove: "); String rid = sc.nextLine().trim();
                    System.out.println(gm.removeStudent(rid)?"Removed":"Not found");
                    break;
                case "4":
                    System.out.print("Student ID: "); String sid = sc.nextLine().trim();
                    System.out.print("Subject: "); String subj = sc.nextLine().trim();
                    System.out.print("Grade: "); double gr = Double.parseDouble(sc.nextLine().trim());
                    gm.findById(sid).ifPresentOrElse(st -> { st.addGrade(subj, gr); System.out.println("Added"); }, () -> System.out.println("Not found"));
                    break;
                case "5":
                    System.out.print("Student ID: "); String tsid = sc.nextLine().trim();
                    gm.findById(tsid).ifPresentOrElse(st -> System.out.println(st.calculateGPA() + " | Grades: " + st.getGrades()), () -> System.out.println("Not found"));
                    break;
                case "6":
                    System.out.println(gm.generateReport());
                    break;
                case "7":
                    fh.saveToFile(dataFile, gm.getStudents());
                    System.out.println("Saved");
                    break;
                case "8":
                    List<Student> arr = fh.loadFromFile(dataFile);
                    arr.forEach(gm::addStudent);
                    System.out.println("Loaded");
                    break;
                case "9":
                    fh.saveToFile(dataFile, gm.getStudents());
                    System.out.println("Bye");
                    running = false;
                    break;
                default:
                    System.out.println("Unknown");
            }
        }
    }

    private static void printHelp() {
        System.out.println("Commands: list, add-student <id> <name>, add-grade <id> <sub> <grade>, save, load, help");
    }
}
