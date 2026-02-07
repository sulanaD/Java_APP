# Student Grade Manager (CLI)

A small, self-contained Java command-line Student Grade Management System. This repository implements a simple full-CRUD CLI application (students + grades), JSON file persistence, unit tests, and a small, conservative repository cleanup and CI workflow.

This README is the single markdown document for the project (all other markdown files were removed). It covers: quick start, commands, code structure, persistence, testing, CI, development notes, and troubleshooting.

---

## Quick start

Requirements
- JDK 17
- Maven 3.6+ (or a recent Maven distribution)

Build

```bash
# from repo root
mvn -DskipTests package
```

Run (interactive)

```bash
java -jar target/programming_Paradigms-1.0-SNAPSHOT-jar-with-dependencies.jar
```

Run (single command, non-interactive)

```bash
# print all stored students
java -jar target/programming_Paradigms-1.0-SNAPSHOT-jar-with-dependencies.jar list
```

Notes
- The application stores data in `students.json` in the working directory where you run the jar. The file is ignored by version control (see `.gitignore`) to avoid committing local state.
- Use quotes for multi-word student names or subjects (e.g., "John Doe").

---

## CLI commands (available in interactive and non-interactive modes)

- `help` — show available commands
- `list` — list all students and their grades
- `add-student <studentId> <name>` — add a student (persisted)
- `remove-student <studentId>` — remove a student (persisted)
- `add-grade <studentId> <subject> <grade>` — add or update a grade for a student (persisted)
- `update-grade <studentId> <subject> <grade>` — update an existing grade (alias of add-grade)
- `remove-grade <studentId> <subject>` — remove a grade for a student (persisted)
- `update-student <studentId> <newName>` — update student's name (persisted)
- `transcript <studentId>` — print student's GPA and grades
- `class-report` — prints a simple class summary
- `save` — explicitly save to the configured JSON file
- `load` — explicitly load from the configured JSON file
- `exit` / `quit` — exit the interactive CLI (on normal exit the app auto-saves)

Example interactive session

```
$ java -jar target/programming_Paradigms-1.0-SNAPSHOT-jar-with-dependencies.jar
CLI Handler started. Type 'help' for commands.
 > add-student S10 "Jane Doe"
 > add-grade S10 Math 92
 > transcript S10
92.0 | Grades: {Math=92.0}
 > list
S10: Jane Doe
 > exit
Bye
```

---

## Project structure (important files)

- `src/main/java/org/example/` — application code:
  - `Main.java` — entry point, registers CLI commands and wires repository/service
  - `cli/CLIHandler.java` — simple REPL; `Command` interface and concrete command classes live here
  - `service/GradeService.java` — business logic and validation
  - `repo/GradeRepository.java`, `JsonPersistenceAdapter.java`, `PersistenceAdapter.java` — in-memory storage + persistence adapter
  - `io/FileHandler.java` — Jackson usage for reading/writing JSON
  - `model/Student.java` — Student model (holds grades map and GPA logic)
- `src/test/java` — unit tests (exercise CRUD and persistence)
- `pom.xml` — Maven build and dependencies (Jackson, JUnit)
- `.github/workflows/ci.yml` — GitHub Actions workflow (runs `mvn test` on push/PR) — created locally and should be committed & pushed to enable CI
- `.gitignore` — ignores `target/`, IDE files and `students.json`

---

## Persistence

- Data is persisted to a JSON file (`students.json`) by default in the working directory.
- `JsonPersistenceAdapter` delegates to `FileHandler` which uses Jackson `ObjectMapper` to read/write model objects.
- The app attempts to load the file at startup; if the file is missing or corrupted the app logs a warning and continues with an empty store.
- Mutating CLI commands call `repo.save(...)` after successful mutation; the app also saves on normal exit.

---

## Tests

Run the unit tests with Maven:

```bash
mvn test
```

- The test suite exercises core behaviors: adding/removing students, adding/updating/removing grades, persistence round-trip (save/load), and some smoke tests.
- Tests pass in the current workspace (as of the last local run).

---

## Continuous Integration (GitHub Actions)

A workflow file exists at `.github/workflows/ci.yml` and is configured to run `mvn test` on JDK 17 for pushes and PRs targeting `main`, `master`, and `dev`. To activate CI on GitHub:

```bash
# create branch, commit files and push (run locally)
git checkout -b dev
git add .github/workflows/ci.yml .gitignore README.md
git add -A
git commit -m "chore(ci): add GitHub Actions workflow and consolidate docs"
git push -u origin dev
```

Once pushed, open a PR or merge to `main`/`dev` to see workflow runs.

---

## Development notes / conventions

- Java 17 is targeted (see `pom.xml`).
- Keep a clear separation of concerns: CLI/commands -> service -> repository -> persistence adapter.
- When adding features, add unit tests under `src/test/java` and run `mvn test` locally before pushing.
- Use defensive copying when returning collections from model/repository classes to avoid accidental mutation.

---

## Troubleshooting

- If `java -jar` prints nothing or exits immediately, try running with `help` or `list` to confirm command output:

```bash
java -jar target/programming_Paradigms-1.0-SNAPSHOT-jar-with-dependencies.jar help
```

- If tests fail locally, run a single test to get stack traces quickly:

```bash
mvn -Dtest=org.example.service.GradeServiceCrudTest test
```

- If `mvn` reports a different Java version than `java --version`, ensure Maven uses the desired JDK by setting `JAVA_HOME` or using `toolchains.xml`, or run via the `actions/setup-java` pattern on CI.

---

## How this README replaces prior documents

All other markdown files have been consolidated into this README per project request. The original report file `docs/CSP3341_Final_Technical_Report_Nov2025.md` was removed from the repository and its content merged/updated into the README. If you still need a separate PDF or markdown copy of the full report, I can recreate it or export this README to a separate file.

---

## Next steps I can do for you

- Commit & push these changes to `dev` for you (I can attempt to push from this environment, or provide commands to run locally).
- Expand the README to include sample JSON, full class diagrams (exported image), or an exact 2200-word formal report (re-create the removed report file under `docs/` if required by assessment submission rules).
- Integrate `picocli` to improve CLI parsing and auto-generated help.

Tell me which of these you want next and I will proceed.
