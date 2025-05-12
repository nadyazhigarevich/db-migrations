package org.zhigarevich.dbmigration.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.zhigarevich.dbmigration.domain.MigrationFile;
import org.zhigarevich.dbmigration.dao.MigrationHistoryDao;
import org.zhigarevich.dbmigration.service.MigrationHistoryService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DefaultMigrationHistoryService implements MigrationHistoryService {

    private static final Logger LOGGER = (Logger) LogManager.getLogger();

    private final MigrationHistoryDao migrationHistoryDao;

    public DefaultMigrationHistoryService(MigrationHistoryDao migrationHistoryDao) {
        this.migrationHistoryDao = migrationHistoryDao;
    }

    @Override
    public void saveMigrations(final List<MigrationFile> migrations, final Connection connection) {
        migrations.forEach(migration -> {
            this.migrationHistoryDao.saveMigration(migration, connection);
            this.migrationHistoryDao.unlock(migration.getVersion(), connection);
            LOGGER.info("Applied migration: {}", migration.getFilename());
        });
    }

    @Override
    public Integer findCurrentVersion(final Connection connection) {
        return this.migrationHistoryDao.findCurrentVersion(connection);
    }

    @Override
    public Boolean isLocked(final Connection connection) {
        return this.migrationHistoryDao.checkIfLocked(connection);
    }

    @Override
    public List<String> findInfo(final Connection connection) {
        return this.migrationHistoryDao.findInfo(connection).stream()
                .map(migration -> {
                    var builder = new StringBuilder();
                    builder.append(migration.getFilename());
                    builder.append(" ");
                    builder.append(migration.getVersion());
                    return builder.toString();
                })
                .toList();
    }

    @Override
    public void unlock(List<Integer> versions, Connection connection) {
        try {
            connection.setAutoCommit(false);

            versions.forEach(v -> {
                this.migrationHistoryDao.unlock(v, connection);
            });
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
