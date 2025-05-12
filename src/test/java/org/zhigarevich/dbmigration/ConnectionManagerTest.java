package org.zhigarevich.dbmigration;

import org.zhigarevich.dbmigration.domain.DatabaseProperties;
import org.zhigarevich.dbmigration.facade.db.ConnectionManager;
import org.zhigarevich.dbmigration.facade.reader.MigrationPropertyReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class ConnectionManagerTest {

    private ConnectionManager connectionManager;
    private DatabaseProperties testProperties;

    @BeforeEach
    void setUp() {
        connectionManager = ConnectionManager.getInstance();
        var properties = MigrationPropertyReader.loadProperties("application-test");
        testProperties = new DatabaseProperties(
                properties.driver(),
                properties.username(),
                properties.password(),
                properties.url()
        );
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
    void testGetInstanceReturnsSameInstance() {
        // Arrange & Act
        ConnectionManager instance1 = ConnectionManager.getInstance();
        ConnectionManager instance2 = ConnectionManager.getInstance();

        // Assert
        assertSame(instance1, instance2);
    }

    @Test
    void testGetConnectionValidPropertiesReturnsConnection() {
        // Act
        Connection connection = connectionManager.getConnection(testProperties);

        // Assert
        assertNotNull(connection);
        assertInstanceOf(Connection.class, connection);
    }
}