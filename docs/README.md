# Student Grade Manager (CLI)

This repository contains a simple Java-based Student Grade Management CLI application.

Repository layout
- src/ — Java source code
- pom.xml — Maven configuration
- docs/ — this documentation

Quick start
1. Build:
   mvn -DskipTests package
2. Run interactively:
   java -jar target/programming_Paradigms-1.0-SNAPSHOT-jar-with-dependencies.jar
3. Non-interactive examples:
   java -jar target/programming_Paradigms-1.0-SNAPSHOT-jar-with-dependencies.jar list
   java -jar target/programming_Paradigms-1.0-SNAPSHOT-jar-with-dependencies.jar add-student S3 "Charlie Brown"
   java -jar target/programming_Paradigms-1.0-SNAPSHOT-jar-with-dependencies.jar add-grade S3 Math 92

Project management (GitHub Projects)
- This repo is set up to be used with GitHub Projects. To enable:
  1. Visit your repository on GitHub.
  2. Click the "Projects" tab and create a new project (Board or Table) to track issues and tasks.
  3. Link issues and pull requests to project cards for progress tracking.

Notes
- The application persists data to `students.json` in the working directory when you save.
- If you prefer GitHub Actions/workflows or issue templates, I can add those next.

