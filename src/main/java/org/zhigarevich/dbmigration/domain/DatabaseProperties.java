package org.zhigarevich.dbmigration.domain;

public record DatabaseProperties(
        String driver,
        String username,
        String password,
        String url
) {
}
