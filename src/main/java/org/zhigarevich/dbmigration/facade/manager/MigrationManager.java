package org.zhigarevich.dbmigration.facade.manager;

import org.zhigarevich.dbmigration.domain.MigrationFile;

import java.sql.Connection;
import java.util.List;

/**
 * This interface represents a manager for handling database migrations.
 */
public interface MigrationManager {

    /**
     * Perform database migration with the given list of migration files on the provided database connection.
     *
     * @param migrationFiles the list of migration files to apply
     * @param connection     the database connection to use
     * @throws InterruptedException if the migration process is interrupted
     */
    void migrate(final List<MigrationFile> migrationFiles, final Connection connection) throws InterruptedException;

    /**
     * Print information about migrations stored in the database using the provided connection.
     *
     * @param connection the database connection to use
     */
    void printInfo(final Connection connection);
}