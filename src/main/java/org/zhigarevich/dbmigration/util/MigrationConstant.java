package org.zhigarevich.dbmigration.util;

public final class MigrationConstant {
    public static final String DRIVER_PROPERTY = "migrations.db.driver";
    public static final String USERNAME_PROPERTY = "migrations.db.username";
    public static final String PASSWORD_PROPERTY = "migrations.db.password";
    public static final String URL_PROPERTY = "migrations.db.url";

    private MigrationConstant() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
