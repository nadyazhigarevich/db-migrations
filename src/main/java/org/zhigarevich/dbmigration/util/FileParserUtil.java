package org.zhigarevich.dbmigration.util;

import java.nio.file.Path;
import java.util.regex.Pattern;

public final class FileParserUtil {

    public static final Pattern MIGRATION_VERSION_PATTERN = Pattern.compile("V_(\\d+)");

    private FileParserUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static boolean checkIfSql(final Path path) {
        return path.getFileName().toString().endsWith(".sql");
    }

    public static int extractVersion(final String filename) {
        var matcher = MIGRATION_VERSION_PATTERN.matcher(filename);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : 0;
    }

    public static String extractFilename(final String filename) {
        String[] fileNameParts = filename.split("\\.");
        return (fileNameParts.length > 0) ? fileNameParts[0] : filename;
    }
}
