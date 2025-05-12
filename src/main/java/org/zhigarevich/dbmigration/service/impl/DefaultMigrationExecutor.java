package org.zhigarevich.dbmigration.service.impl;

import org.zhigarevich.dbmigration.dao.MigrationExecutorDao;
import org.zhigarevich.dbmigration.dao.impl.MigrationExecutorDaoImpl;
import org.zhigarevich.dbmigration.service.MigrationExecutorService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DefaultMigrationExecutor implements MigrationExecutorService {

    private final MigrationExecutorDao migrationExecutorDao;

    public DefaultMigrationExecutor() {
        this.migrationExecutorDao = new MigrationExecutorDaoImpl();
    }

    @Override
    public void apply(final List<String> query, final Connection connection) throws SQLException {
        this.migrationExecutorDao.apply(query, connection);
    }
}
