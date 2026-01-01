package org.example.cli;

public interface Command {
    void execute(String[] args);
    default String getHelp() { return ""; }
}
