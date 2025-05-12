package org.zhigarevich.dbmigration.util;

import org.zhigarevich.dbmigration.domain.MigrationFile;

import java.util.List;

public final class MigrationUtil {

    private MigrationUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static List<MigrationFile> filterByVersion(final List<MigrationFile> migrations, final Integer version) {
        return migrations.stream()
                .filter(migration -> migration.getVersion() > version)
                .toList();
    }

    public static List<String> extractMigrationScripts(final List<MigrationFile> migrationFiles) {
        return migrationFiles.stream()
                .map(MigrationFile::getContent)
                .toList();
    }
}
