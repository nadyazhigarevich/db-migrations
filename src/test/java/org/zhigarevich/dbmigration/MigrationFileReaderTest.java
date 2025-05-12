package org.zhigarevich.dbmigration;

import org.zhigarevich.dbmigration.domain.MigrationFile;
import org.zhigarevich.dbmigration.exception.BrokenValidationException;
import org.zhigarevich.dbmigration.facade.reader.MigrationFileReader;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MigrationFileReaderTest {

    @Test
    void testRead_ValidPathToMigrations_ReturnsListOfMigrationFiles() {
        // Arrange
        String validPath = "src/test/resources/migrations";

        // Act
        List<MigrationFile> migrationFiles = new MigrationFileReader().read(validPath);
        var expectedSize = 2;

        // Assert
        assertNotNull(migrationFiles);
        assertEquals(2, migrationFiles.size());
    }

    @Test
    void testReadThrowsNullPointerException() {
        // Arrange
        String nullPath = null;

        // Act and Assert
        assertThrows(NullPointerException.class, () -> {
            new MigrationFileReader().read(nullPath);
        });
    }

    @Test
    void testReadInvalidMigrationsThrowsIllegalStateException() {
        // Arrange
        String invalidPath = "invalid_migrations";

        // Act and Assert
        assertThrows(BrokenValidationException.class, () -> {
            new MigrationFileReader().read(invalidPath);
        });
    }
}