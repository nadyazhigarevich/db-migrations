package org.zhigarevich.dbmigration.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * This interface represents a service for executing database migrations.
 */
@FunctionalInterface
public interface MigrationExecutorService {

    /**
     * Apply a list of migration queries on the provided database connection.
     *
     * @param queries    the list of SQL queries representing the migrations
     * @param connection the database connection on which the migrations are to be applied
     * @throws SQLException if an SQL exception occurs while applying the migrations
     */
    void apply(final List<String> queries, final Connection connection) throws SQLException;
}