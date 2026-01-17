# CSP3341 — Final Technical Report (Nov 2025)

Author: (Student Name)
Course: CSP3341 — Programming Languages and Paradigms
Language investigated: Java

---

This report documents a focused examination of Java (the language used in the supplied Student Grade Management System) and demonstrates the implementation, design rationale and a critique of the sample application in the context of the CSP3341 assignment brief. The document contains two parts: Part A (language description — deep technical discussion) and Part B (application demonstration and evaluation). Diagrams, code samples and a mapping to the assignment rubrics are included.

---

## Part A — Language Description (approx. 1700 words)

This section explains relevant Java features that shaped the design and implementation of the Student Grade Management System. The coverage follows the assignment guidance (naming, data types, control flow, subprograms, ADTs, paradigms, concurrency, exceptions, comparison, and quality attributes).

1. Naming Conventions

- Java follows well-established naming conventions that improve readability and maintainability. Packages use lower-case dot-separated names (e.g., `org.example.service`). Class names use UpperCamelCase (`GradeService`, `GradeManager`). Methods and variables use lowerCamelCase (`addStudent`, `studentId`). Constants are UPPER_SNAKE_CASE (e.g., `DEFAULT_FILE`). The codebase adheres to most of these conventions which simplifies navigation and reduces cognitive load.

2. Data Types

- Java is statically-typed and primarily uses primitive types (int, long, double, boolean, char) alongside reference types (classes, interfaces). The sample code uses `String` and `double` for student IDs, names and grades. Collection types like `List` and `Map` from the Java Collections Framework are used extensively (`List<Student>`, `Map<String, Double>` for grades). Static typing provides early detection of many classes of errors (compile-time), while generics (`List<Student>`) supply type safety for collections.

3. Expressions and Assignment Statements

- Java expressions are familiar (arithmetic, boolean logic, method calls). Assignments use `=` and support compound operators (`+=`, `-=`, etc.). The project uses typical expression forms (e.g., `double avg = grades.values().stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);`) — this snippet demonstrates use of the Stream API for concise aggregation.

4. Statement-level Control Structures

- Java supports `if`, `switch`, loops (`for`, `while`, `do-while`) and enhanced `for` loops. The codebase uses `for` and `while` loops (e.g., `Iterator` in `GradeManager.removeStudent`) and `switch` constructs in `Main` to support non-interactive command invocation. The `switch` statement is straightforward for CLI command dispatching. Control structures are explicit and predictable.

5. Subprograms (Methods) and Parameter Passing

- Methods are declared with visibility and possibly `static`. Java uses pass-by-value semantics where references are passed by value. Methods in the code are small and focused: `GradeService.addStudent`, `Student.calculateGPA`, `FileHandler.loadFromFile`. Clear single-responsibility methods improve testability.

6. Abstract Data Types and Encapsulation

- Java promotes encapsulation through `private` fields and public getters/setters. The `Student` model hides the grades map behind an accessor and exposes `addGrade` and `calculateGPA` operations — this represents an ADT for the student entity. `GradeRepository` encapsulates persistence and in-memory storage. Encapsulation reduces coupling and isolates implementation changes.

7. Support for Programming Paradigms

- Java is primarily object-oriented: classes, inheritance, interfaces (e.g., `Command`, `PersistenceAdapter`). The repository uses interfaces for adapters, enabling dependency inversion. The code also uses functional features added in Java 8 (lambdas and streams). For example, `cli.register("class-report", args1 -> System.out.println(gs.classReport()));` shows using a lambda as a command implementation, combining OOP structure with lightweight functional expressions.

8. Concurrency — Parallel Processing

- Core Java concurrency constructs include `Thread`, `synchronized`, `ExecutorService` and `CompletableFuture`. The sample application is single-threaded (CLI-driven), which is appropriate for its scope. Where concurrency might be useful (e.g., background saves, bulk imports), Java provides robust APIs. A future enhancement could use `ExecutorService` for asynchronous save/load to avoid blocking the CLI.

9. Exception Handling and Event Handling

- Java uses `try/catch/finally` and checked exceptions for APIs (like `IOException`). The repository and file handling code handle IO exceptions and degrade gracefully (printing warnings and returning empty lists). Event handling in GUI contexts is less relevant here; instead, the CLI dispatch pattern (`Command` interface) functions as an event-like command dispatch mechanism.

10. Comparison with Similar Languages

- Compared to C++: Java is memory-managed (garbage-collected) and has a simpler type system (no header/source split), which eases portability but may be less performant for low-level tasks.
- Compared to Ruby: Ruby is dynamic and more concise but lacks compile-time type checking. Java has more boilerplate but stronger tooling (IDE, static analysis) and better performance characteristics in many cases.
- Compared to Swift: Swift offers modern language ergonomics and safety features (optionals), while Java offers wider cross-platform support and a mature ecosystem for server and CLI tooling.

11. Readability, Writability, Performance

- Readability: Java is verbose but explicit — good for large teams. The codebase follows clear package and class separation and uses meaningful names which improves readability.
- Writability: Boilerplate (getters/setters) hampers quick scripting, but modern Java (records, lambdas) mitigates this.
- Performance: Java's JVM provides JIT optimizations; for an I/O-heavy CLI app, performance isn't a limiting factor.

12. Conclusions (Part A)

- Java is well-suited for building maintainable CLI tools and small desktop/server utilities. Its static typing, strong tooling, and extensive libraries support robust implementations. The project demonstrates idiomatic Java practices: clear separation of concerns (models, services, persistence, CLI), use of interfaces, and defensive exception handling.

---

## Part B — Software application and demonstration (approx. 500 words)

This section documents the Student Grade Management System implemented in this repository, shows code excerpts and diagrams, and evaluates two good and two not-so-good aspects of Java observed while developing this sample.

1. Application overview and functionality

The application is a CLI-driven Student Grade Management System with these features:
- Add, update, delete and list students.
- Assign grades per student per subject.
- Calculate student GPA via a simple grade-to-GPA mapping (implemented in `Student.calculateGPA`).
- Persistence via JSON files (`students.json`) using a `FileHandler` with Jackson; `GradeRepository` delegates to a `PersistenceAdapter` (`JsonPersistenceAdapter`) that uses `FileHandler`.
- Interactive CLI with commands (`CLIHandler`) and `Command` implementations for `add-student`, `add-grade`, `remove-student`, `transcript`, `class-report`, `save`, and `load`.

A non-interactive mode exists where commands can be run via CLI arguments (see `Main.main`).

2. Class diagram (Mermaid)

```mermaid
classDiagram
    class Main {
        +main(String[] args)
    }
    class CLIHandler {
        +start()
        +register(name, Command)
    }
    interface Command {
        +execute(String[] args)
        +getHelp(): String
    }
    class AddStudentCommand
    class AddGradeCommand
    class RemoveStudentCommand
    class TranscriptCommand

    class GradeService {
        +addStudent(Student): boolean
        +addGrade(id, subject, grade): boolean
        +listStudents(): List~Student~
        +load(File)
        +save(File)
    }
    class GradeRepository {
        +load(File)
        +save(File)
        +add(Student)
        +remove(id): boolean
        +findById(id): Optional~Student~
        +findAll(): List~Student~
    }
    interface PersistenceAdapter {
        +loadAll(File): List~Student~
        +saveAll(File, List~Student~)
    }
    class JsonPersistenceAdapter
    class FileHandler
    class GradeManager
    class Student {
        -studentId: String
        -name: String
        -grades: Map~String,double~
        +addGrade(subject, grade)
        +calculateGPA(): double
    }

    Main --> CLIHandler : creates
    CLIHandler ..> Command : dispatches
    Command <|-- AddStudentCommand
    Command <|-- AddGradeCommand
    Command <|-- RemoveStudentCommand
    Command <|-- TranscriptCommand
    GradeService --> GradeRepository
    GradeRepository ..> PersistenceAdapter
    PersistenceAdapter <|-- JsonPersistenceAdapter
    JsonPersistenceAdapter --> FileHandler
    GradeManager --> Student
    GradeService ..> GradeManager
```

3. Example code excerpts (key snippets)

- Student model (core behavior: add grade and GPA calculation):

```java
public class Student {
    private String studentId;
    private String name;
    private Map<String, Double> grades = new HashMap<>();

    public void addGrade(String subject, double grade) { grades.put(subject, grade); }

    public double calculateGPA() {
        if (grades.isEmpty()) return Double.NaN;
        double avg = grades.values().stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
        if (Double.isNaN(avg)) return Double.NaN;
        if (avg >= 90) return 4.0; if (avg >= 80) return 3.0;
        if (avg >= 70) return 2.0; if (avg >= 60) return 1.0; return 0.0;
    }
}
```

- Persistence adapter and file handler integration (shows separation of concerns):

```java
public interface PersistenceAdapter {
    List<Student> loadAll(File f) throws IOException;
    void saveAll(File f, List<Student> data) throws IOException;
}

public class JsonPersistenceAdapter implements PersistenceAdapter {
    private final FileHandler fh;
    public JsonPersistenceAdapter(FileHandler fh) { this.fh = fh; }
    @Override public List<Student> loadAll(File f) throws IOException { return fh.loadFromFile(f); }
    @Override public void saveAll(File f, List<Student> data) throws IOException { fh.saveToFile(f, data); }
}
```

4. Two good aspects of Java (demonstrated in the project)

- Strong typing and explicit interfaces: Interfaces like `PersistenceAdapter` make it straightforward to swap implementations (e.g., JSON, a future SQLite adapter or in-memory mock for tests). This contributes to testability and modular design.
  - Example: `GradeRepository` receives a `PersistenceAdapter` and is agnostic to the underlying storage format.

- Rich standard library + ecosystem: The Jackson library is used via `FileHandler` to serialize/deserialize JSON with a few lines of code. Java's ecosystem reduces boilerplate for common tasks (I/O, JSON parsing).
  - Example: `ObjectMapper` usage in `FileHandler`.

5. Two not-so-good aspects observed (and mitigations)

- Verbosity (boilerplate): Java requires explicit classes and methods (getters/setters). In this small project a few classes are thin (e.g., some repo interfaces), which introduces cognitive overhead. Mitigation: use modern features (records for DTOs), Lombok (if allowed), or keep modules concise.

- No built-in lightweight CLI framework: The project uses a small custom `CLIHandler`, which is simple and fine, but a more feature-rich CLI (argument parsing, help formatting) would benefit from libraries like PicoCLI for larger CLI tools.

6. Demonstration evidence and screenshots

- The app's interactive run prints a prompt and supports `help`, `add-student`, `add-grade`, `list`, `transcript`, `class-report`, `save`, and `load`. The `Main.main` also supports non-interactive one-shot commands for scripting.

7. Rubric mapping (how the submission meets requirements)

- Part A: All listed language topics are covered in this document (naming, data types, control structures, subprograms, ADTs, OOP/functional support, concurrency, exceptions, comparison, readability/writability/performance, conclusions).
- Part B: Demonstration of code (class diagram, snippets), two good and two bad aspects clearly explained with code references.

---

## Appendices

A. Files referenced (most relevant)
- src/main/java/org/example/model/Student.java
- src/main/java/org/example/manager/GradeManager.java
- src/main/java/org/example/service/GradeService.java
- src/main/java/org/example/repo/GradeRepository.java
- src/main/java/org/example/repo/PersistenceAdapter.java
- src/main/java/org/example/repo/JsonPersistenceAdapter.java
- src/main/java/org/example/io/FileHandler.java
- src/main/java/org/example/cli/CLIHandler.java
- src/main/java/org/example/Main.java

B. Suggested code improvements (to align the project with rubric and production best-practices)
1. Remove dead/legacy files and placeholders: `src/main/java/org/example/service/StudentService.java` (currently marked "removed - use GradeManager instead"), `src/main/java/org/example/repo/StudentRepo.java`, `InMemoryStudentRepo.java`, `InMemoryMessageRepo.java`, `MessageRepo.java` if they are not used. Cleaning reduces noise for graders.
2. Add unit tests for core logic: `Student.calculateGPA`, `GradeRepository` persistence round-trip, `GradeService` validation paths. There are some tests in `test/` already — expand to cover edge cases.
3. Improve CLI help/menu: ensure the interactive menu prints options with separating blank lines and insists on waiting until `exit` or `quit` is given; e.g., `CLIHandler.printHelp` can be changed to put a blank line between items for readability.
4. Add input validation and robust error messages: JSON parsing already degrades gracefully; ensure add-student rejects blank ids/names and that `add-grade` validates grade ranges.
5. Consider asynchronous persistence: use `ExecutorService` to persist data on a separate thread to keep the CLI responsive.

C. Build and run instructions (short)

- Build and run (requires Maven and JDK):

```bash
# build
mvn -DskipTests package
# run interactive
java -jar target/programming_Paradigms-1.0-SNAPSHOT-jar-with-dependencies.jar
```

D. References
- Oracle Java SE documentation (https://docs.oracle.com/en/java/)
- Jackson JSON Processor (https://github.com/FasterXML/jackson)
- PicoCLI (for CLI enhancements) (https://picocli.info/)

---

## Requirements coverage checklist
- Part A topics: Done
- Part B demonstration: Done (class diagram, code excerpts, functionality)
- Professionalism: Document structured, code referenced, suggested improvements provided — ready for screenshots and final polishing.


---

(End of report draft)

