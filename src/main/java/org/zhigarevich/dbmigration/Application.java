package org.zhigarevich.dbmigration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.zhigarevich.dbmigration.facade.MigrationFacade;

import java.util.Scanner;

public class Application {

    private static final Logger LOGGER = (Logger) LogManager.getLogger();

    public static void main(String[] args) {
        var migrationFacade = new MigrationFacade();
        var scanner = new Scanner(System.in);

        LOGGER.info("Database Migration Tool");
        LOGGER.info("Available commands: 'migrate', 'status', 'exit'");

        while (true) {
            LOGGER.info("\nEnter a command:");
            var command = scanner.nextLine().trim().toLowerCase();

            if (command.isEmpty()) {
                continue;
            }

            try {
                switch (command) {
                    case "migrate":
                        migrationFacade.migrate();
                        LOGGER.info("Migration completed successfully");
                        break;
                    case "status":
                        migrationFacade.printAppliedMigrations();
                        break;
                    case "exit":
                        LOGGER.info("Exiting migration tool...");
                        scanner.close();
                        return;
                    default:
                        LOGGER.info("Invalid command. Available commands: 'migrate', 'status', 'exit'");
                        break;
                }
            } catch (Exception e) {
                LOGGER.error("Error executing command '{}': {}", command, e.getMessage());
                LOGGER.debug("Stack trace:", e);
            }
        }
    }
}
