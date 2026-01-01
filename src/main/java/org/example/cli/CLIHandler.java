package org.example.cli;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class CLIHandler {
    private final Map<String, Command> commands = new HashMap<>();
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public CLIHandler() {
    }

    public void register(String name, Command cmd) {
        commands.put(name, cmd);
    }

    public void start() throws Exception {
        System.out.println("CLI Handler started. Type 'help' for commands.");
        while (true) {
            System.out.print(" > ");
            String line = reader.readLine();
            if (line == null) break;
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split("\\s+");
            String cmd = parts[0];
            if ("exit".equalsIgnoreCase(cmd) || "quit".equalsIgnoreCase(cmd)) {
                System.out.println("Exiting CLI");
                return;
            }
            Command command = commands.get(cmd);
            if (command != null) {
                String[] args = new String[parts.length - 1];
                System.arraycopy(parts, 1, args, 0, args.length);
                command.execute(args);
            } else if ("help".equalsIgnoreCase(cmd)) {
                printHelp();
            } else {
                System.out.println("Unknown command. Type 'help'.");
            }
        }
    }

    private void printHelp() {
        System.out.println("Available commands:");
        commands.forEach((k, v) -> System.out.println("  " + k + " - " + v.getHelp()));
        System.out.println("  help - show this help");
        System.out.println("  exit - quit");
    }
}
