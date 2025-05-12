package org.zhigarevich.dbmigration;

import org.zhigarevich.dbmigration.dao.impl.MigrationExecutorDaoImpl;
import org.zhigarevich.dbmigration.domain.DatabaseProperties;
import org.zhigarevich.dbmigration.exception.MigrationFilesException;
import org.zhigarevich.dbmigration.facade.db.ConnectionManager;
import org.zhigarevich.dbmigration.facade.reader.MigrationPropertyReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MigrationExecutorDaoImplTest {

    private ConnectionManager connectionManager;
    private DatabaseProperties testProperties;
    private Connection connection;

    @BeforeEach
    void setUp() {
        connectionManager = ConnectionManager.getInstance();
        var validProperties = MigrationPropertyReader.loadProperties("application-test");
        testProperties = new DatabaseProperties(
                validProperties.driver(),
                validProperties.username(),
                validProperties.password(),
                validProperties.url()
        );
        connection = ConnectionManager.getInstance().getConnection(validProperties);
    }

    @AfterEach
    void tearDown() {
        try {
            if (connectionManager.getConnection(testProperties) != null && !connectionManager.getConnection(testProperties).isClosed()) {
                connectionManager.getConnection(testProperties).close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testApply_InvalidQueries_ThrowsMigrationFilesException() {
        // Arrange
        var invalidQueries = List.of("abcdef;");

        // Act and Assert
        assertThrows(MigrationFilesException.class, () -> {
            new MigrationExecutorDaoImpl().apply(invalidQueries, connection);
        });
    }
}