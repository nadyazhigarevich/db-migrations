package org.zhigarevich.dbmigration;

import org.zhigarevich.dbmigration.domain.DatabaseProperties;
import org.zhigarevich.dbmigration.facade.reader.MigrationPropertyReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MigrationPropertyReaderTest {

    @Test
    void testLoadPropertiesValidProperty() {
        // Arrange
        String validFilename = "application-test";

        // Act
        DatabaseProperties properties = MigrationPropertyReader.loadProperties(validFilename);

        var expectedDriver = "org.h2.Driver";
        var expectedUsername = "admin";
        var expectedPassword = "admin";
        var expectedUrl = "jdbc:h2:mem:test";

        // Assert
        assertEquals(expectedUsername, properties.username());
        assertEquals(expectedDriver, properties.driver());
        assertEquals(expectedUrl, properties.url());
        assertEquals(expectedPassword, properties.password());
    }

    @Test
    void testLoadPropertiesInvalidPropertiesThrowsIllegalStateException() {
        // Arrange
        String invalidFilename = "invalid";

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> {
            MigrationPropertyReader.loadProperties(invalidFilename);
        });
    }

    @Test
    void testLoadPropertiesMissingResourceThrowsIllegalStateException() {
        // Arrange
        String missingResourceFilename = "invalid";

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> {
            MigrationPropertyReader.loadProperties(missingResourceFilename);
        });
    }
}