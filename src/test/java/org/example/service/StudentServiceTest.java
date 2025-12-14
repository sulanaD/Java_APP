package org.example.service;

import org.example.Main;
import org.junit.jupiter.api.Test;

class MainSmokeTest {
    @Test
    void runHelp() throws Exception {
        // ensure main runs in non-interactive mode with help command
        Main.main(new String[]{"help"});
    }
}
