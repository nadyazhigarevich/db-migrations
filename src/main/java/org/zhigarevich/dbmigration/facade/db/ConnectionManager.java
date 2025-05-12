package org.zhigarevich.dbmigration.facade.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.zhigarevich.dbmigration.domain.DatabaseProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static final Logger LOGGER = (Logger) LogManager.getLogger();
    private static ConnectionManager instance;
    private Connection connection;

    private ConnectionManager() {
    }

    public static ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    public Connection getConnection(final DatabaseProperties properties) {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName(properties.driver());
                connection = DriverManager.getConnection(
                        properties.url(),
                        properties.username(),
                        properties.password()
                );
                LOGGER.info("Connected to db.");
            }
        } catch (ClassNotFoundException | SQLException exception) {
            LOGGER.error("Failed connect to database: {}", exception.getMessage());
        }
        return connection;
    }
}
