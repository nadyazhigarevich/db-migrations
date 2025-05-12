package org.zhigarevich.dbmigration.facade.manager;

import org.apache.logging.log4j.core.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.zhigarevich.dbmigration.exception.MigrationFilesException;
import org.zhigarevich.dbmigration.service.MigrationExecutorService;
import org.zhigarevich.dbmigration.domain.MigrationFile;
import org.zhigarevich.dbmigration.service.MigrationHistoryService;
import org.zhigarevich.dbmigration.util.MigrationUtil;


public class DefaultMigrationManager implements MigrationManager {

    private static final Logger LOGGER = (Logger) LogManager.getLogger();
    private static final int DELAY_TO_RETRIEVE_LOCKED_STATUS = 5000;

    private final MigrationExecutorService migrationExecutor;
    private final MigrationHistoryService migrationHistoryService;

    public DefaultMigrationManager(MigrationExecutorService executor, MigrationHistoryService migrationHistoryService) {
        this.migrationExecutor = executor;
        this.migrationHistoryService = migrationHistoryService;
    }

//    @Override
//    public void migrate(final List<MigrationFile> migrationFiles, final Connection connection) {
//        var isLocked = this.migrationHistoryService.isLocked(connection);
//        while (isLocked) {
//            try {
//                TimeUnit.MILLISECONDS.sleep(DELAY_TO_RETRIEVE_LOCKED_STATUS);
//
//                LOGGER.info("Migration is locked...");
//
//                isLocked = this.migrationHistoryService.isLocked(connection);
//            } catch (InterruptedException e) {
//                LOGGER.error("Something went wrong");
//            }
//        }
//        this.processMigration(migrationFiles, connection);
//    }

    @Override
    public void migrate(List<MigrationFile> migrationFiles, Connection connection) {
        try {
            boolean locked = migrationHistoryService.isLocked(connection);
            if (locked) {
                waitAndRetry(migrationFiles, connection);
                return;
            }
            processMigration(migrationFiles, connection);
        } finally {
            migrationHistoryService.unlock(migrationFiles.stream().map(MigrationFile::getVersion).toList(), connection);
        }
    }

    @Override
    public void printInfo(Connection connection) {
        this.migrationHistoryService.findInfo(connection).forEach(info -> {
            LOGGER.info("Migration: {}", info);
        });
    }

    private void waitAndRetry(List<MigrationFile> migrationFiles, Connection connection) {
        int attempts = 0;
        final int maxAttempts = 5; // Максимальное количество попыток
        final long delayMs = 5000; // Интервал между попытками (5 секунд)

        while (attempts < maxAttempts) {
            try {
                LOGGER.info("Migration is locked. Waiting for {} ms (attempt {}/{})...",
                        delayMs, attempts + 1, maxAttempts);

                TimeUnit.MILLISECONDS.sleep(delayMs);

                if (!migrationHistoryService.isLocked(connection)) {
                    LOGGER.info("Lock released. Attempting migration...");
                    processMigration(migrationFiles, connection);
                    return;
                }

                attempts++;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Восстанавливаем флаг прерывания
                LOGGER.error("Migration wait interrupted", e);
                throw new RuntimeException("Migration interrupted while waiting for lock", e);
            }
        }

        throw new IllegalStateException("Failed to acquire migration lock after " + maxAttempts + " attempts");
    }

    private void processMigration(final List<MigrationFile> migrationFiles, final Connection connection) {
        var latestVersion = this.migrationHistoryService.findCurrentVersion(connection);

        var migrations = MigrationUtil.filterByVersion(migrationFiles, latestVersion);
        var migrationsScript = MigrationUtil.extractMigrationScripts(migrations);

        try {
            this.migrationExecutor.apply(migrationsScript, connection);
            this.migrationHistoryService.saveMigrations(migrations, connection);
        } catch (SQLException | MigrationFilesException e) {
            LOGGER.error("Failed to apply migration: {}", e.getMessage());
        }
    }
}
