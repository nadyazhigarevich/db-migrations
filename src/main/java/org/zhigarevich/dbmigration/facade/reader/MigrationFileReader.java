package org.zhigarevich.dbmigration.facade.reader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zhigarevich.dbmigration.domain.MigrationFile;
import org.zhigarevich.dbmigration.facade.reader.validator.impl.MigrationFileValidator;
import org.zhigarevich.dbmigration.util.FileParserUtil;
import org.zhigarevich.dbmigration.facade.reader.validator.Validator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MigrationFileReader {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Validator<MigrationFile> validator = new MigrationFileValidator();

    public List<MigrationFile> read(final String pathToMigrations) {
        Objects.requireNonNull(pathToMigrations, "The path to migrations cannot be null");

        var folder = Paths.get(pathToMigrations);
        validator.validatePath(folder);

        try (var paths = Files.list(folder)) {
            return paths.filter(Files::isRegularFile)
                    .filter(FileParserUtil::checkIfSql)
                    .map(file -> {
                        var migration = this.parseMigration(file);
                        validator.validate(migration);
                        return migration;
                    })
                    .sorted(Comparator.comparing(MigrationFile::getVersion))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalStateException("Error during reading migration files", e);
        }
    }

    private MigrationFile parseMigration(final Path path) {
        var migrationFile = new MigrationFile();
        try {
            var name = path.getFileName().toString();

            var content = Files.readString(path);
            var version = FileParserUtil.extractVersion(name);
            var filename = FileParserUtil.extractFilename(name);

            migrationFile.setFilename(filename);
            migrationFile.setContent(content);
            migrationFile.setVersion(version);
        } catch (IOException e) {
            LOGGER.error("Error during reading files: {}", e.getMessage());
        }
        return migrationFile;
    }
}
