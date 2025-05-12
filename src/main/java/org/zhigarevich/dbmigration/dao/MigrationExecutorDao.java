package org.zhigarevich.dbmigration.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * This interface defines the contract for executing migrations on a database.
 */
public interface MigrationExecutorDao {

    /**
     * Apply the list of queries as migrations on the provided database connection.
     *
     * @param queries    the list of SQL queries representing the migrations
     * @param connection the database connection on which the migrations are to be applied
     * @throws SQLException if an SQL exception occurs while applying the migrations
     */
    void apply(final List<String> queries, final Connection connection) throws SQLException;
}
