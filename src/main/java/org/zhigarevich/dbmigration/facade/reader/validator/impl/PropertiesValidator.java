package org.zhigarevich.dbmigration.facade.reader.validator.impl;

import org.zhigarevich.dbmigration.domain.DatabaseProperties;
import org.zhigarevich.dbmigration.facade.reader.validator.Validator;

public class PropertiesValidator implements Validator<DatabaseProperties> {

    @Override
    public void validate(final DatabaseProperties properties) {
        validateProperty( "Username", properties.username());
        validateProperty("URL", properties.url());
        validateProperty("Password", properties.password());
        validateProperty("Driver", properties.driver());
    }
}
