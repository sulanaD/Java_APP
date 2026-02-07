# Student Grade Manager (CLI)

A small full-Java command-line Student Grade Manager.

Structure
- `src/` — Java source
- `pom.xml` — Maven build config
- `docs/` — documentation

Build & run
- Build: `mvn -DskipTests package`
- Run (interactive): `java -jar target/programming_Paradigms-1.0-SNAPSHOT-jar-with-dependencies.jar`
- Run (non-interactive): `java -jar target/programming_Paradigms-1.0-SNAPSHOT-jar-with-dependencies.jar list`

I can
- Add and remove students
- Add, update, remove grades per student
- Calculate GPA and print transcripts
- Persist data to `students.json` (JSON file)

Contributing
- Create issues, use GitHub Projects for tracking. Issue templates and a CI workflow are included in `.github/`.
- See `CONTRIBUTING.md` for contribution steps.
