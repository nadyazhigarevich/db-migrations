package org.zhigarevich.dbmigration.facade;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.zhigarevich.dbmigration.facade.db.ConnectionManager;
import org.zhigarevich.dbmigration.service.impl.DefaultMigrationExecutor;
import org.zhigarevich.dbmigration.exception.BrokenValidationException;
import org.zhigarevich.dbmigration.facade.manager.DefaultMigrationManager;
import org.zhigarevich.dbmigration.facade.reader.MigrationPropertyReader;
import org.zhigarevich.dbmigration.domain.DatabaseProperties;
import org.zhigarevich.dbmigration.dao.impl.MigrationHistoryDaoImpl;
import org.zhigarevich.dbmigration.service.impl.DefaultMigrationHistoryService;
import org.zhigarevich.dbmigration.facade.reader.MigrationFileReader;

import java.sql.SQLException;

import static org.zhigarevich.dbmigration.util.ConfigurationConstant.*;


public class MigrationFacade {

    private static final Logger LOGGER = (Logger) LogManager.getLogger();

    private static final DatabaseProperties properties;

    private final MigrationFileReader fileReader;
    private final DefaultMigrationManager migrationManager;
    private final ConnectionManager connectionManager;

    static {
        properties = MigrationPropertyReader.loadProperties(PATH_TO_RESOURCES);
    }

    public MigrationFacade() {
        this.fileReader = new MigrationFileReader();
        this.connectionManager = ConnectionManager.getInstance();

        var historyService = new DefaultMigrationHistoryService(new MigrationHistoryDaoImpl());
        var executor = new DefaultMigrationExecutor();
        this.migrationManager = new DefaultMigrationManager(executor, historyService);
    }

    public void migrate() {
        try {
            var migrations = this.fileReader.read(PATH_TO_MIGRATIONS);

            try (var connection = this.connectionManager.getConnection(properties)) {
                this.migrationManager.migrate(migrations, connection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (BrokenValidationException e) {
            LOGGER.error("Failed to validate migrations files: {}", e.getMessage());
        } catch (RuntimeException e) {
            LOGGER.error("Unable to parse migrations files: {}", e.getMessage());
        }
    }

    public void printAppliedMigrations() {
        try(var connection = this.connectionManager.getConnection(properties) ) {
            this.migrationManager.printInfo(connection);
        }catch (SQLException e) {
            LOGGER.error("Failed to print migrations info: {}", e.getMessage());
        }
    }
}
