package org.zhigarevich.dbmigration.facade.reader.validator.impl;

import org.zhigarevich.dbmigration.domain.MigrationFile;
import org.zhigarevich.dbmigration.facade.reader.validator.Validator;

public class MigrationFileValidator implements Validator<MigrationFile> {

    @Override
    public void validate(final MigrationFile migrationFile) {
        validateProperty("Migration name", migrationFile.getFilename());
        validateProperty("Migration content", migrationFile.getContent());
        validateProperty("Migration version", migrationFile.getVersion().toString());
    }
}
