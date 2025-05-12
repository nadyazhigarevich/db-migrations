package org.zhigarevich.dbmigration.util;

public final class ConfigurationConstant {

    public static final String PATH_TO_RESOURCES = "application";
    public static final String PATH_TO_MIGRATIONS = "src/main/resources/migrations";

    private ConfigurationConstant() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
