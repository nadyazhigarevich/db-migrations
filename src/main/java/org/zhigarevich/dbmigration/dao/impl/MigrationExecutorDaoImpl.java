package org.zhigarevich.dbmigration.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.zhigarevich.dbmigration.dao.MigrationExecutorDao;
import org.zhigarevich.dbmigration.exception.MigrationFilesException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MigrationExecutorDaoImpl implements MigrationExecutorDao {

    private static final Logger LOGGER = (Logger) LogManager.getLogger();

    @Override
    public void apply(final List<String> queries, final Connection connection) throws SQLException {
        try (var statement = connection.createStatement()) {
            LOGGER.debug("Attempting to apply actual migrations");

            connection.setAutoCommit(false);
            for (var query : queries) {
                statement.addBatch(query);
            }

            int[] rowsAffected = statement.executeBatch();
            connection.commit();

            LOGGER.info("Actual migrations have been applied. Rows affected: {}", rowsAffected.length);
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.error("Failed to apply migration: {}", e.getMessage());
            throw new MigrationFilesException("Failed to apply migration: %s".formatted(queries));
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
