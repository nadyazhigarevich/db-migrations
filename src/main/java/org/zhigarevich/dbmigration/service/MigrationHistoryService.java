package org.zhigarevich.dbmigration.service;

import org.zhigarevich.dbmigration.domain.MigrationFile;

import java.sql.Connection;
import java.util.List;

/**
 * This interface defines the contract for a service managing migration history operations.
 */
public interface MigrationHistoryService {

    /**
     * Save a list of migration files and unlock their versions in the database.
     *
     * @param migrations  the list of migration files to save
     * @param connection  the database connection
     */
    void saveMigrations(final List<MigrationFile> migrations, final Connection connection);

    /**
     * Find the current version of the migration in the database.
     *
     * @param connection the database connection
     * @return the current migration version
     */
    Integer findCurrentVersion(final Connection connection);

    /**
     * Check if there are any locked migrations in the database.
     *
     * @param connection the database connection
     * @return true if there are locked migrations, false otherwise
     */
    Boolean isLocked(final Connection connection);

    /**
     * Retrieve information about all migration files stored in the database.
     *
     * @param connection the database connection
     * @return a list of strings containing filename and version information
     */
    List<String> findInfo(final Connection connection);

    void unlock(List<Integer> version, Connection connection);
}