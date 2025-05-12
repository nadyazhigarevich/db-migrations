package org.zhigarevich.dbmigration.dao;

import org.zhigarevich.dbmigration.domain.MigrationFile;

import java.sql.Connection;
import java.util.List;

/**
 * This interface defines the contract for managing migration history in a database.
 */
public interface MigrationHistoryDao {

    /**
     * Save the information about a migration file in the database.
     *
     * @param migrationFile the migration file to save
     * @param connection    the database connection
     */
    void saveMigration(final MigrationFile migrationFile, final Connection connection);

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
    Boolean checkIfLocked(final Connection connection);

    /**
     * Unlock a specific version of the migration in the database.
     *
     * @param version    the version of the migration to unlock
     * @param connection the database connection
     */
    void unlock(final Integer version, final Connection connection);

    /**
     * Retrieve information about all migration files stored in the database.
     *
     * @param connection the database connection
     * @return a list of MigrationFile objects representing the migration information
     */
    List<MigrationFile> findInfo(final Connection connection);
}