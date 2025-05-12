# Database Migration Library

This Java library provides functionality for managing database migrations using JDBC. It aims to be feature-rich and easy to integrate with other applications, drawing inspiration from tools like Flyway and Liquibase.

## Functionality

- **Database Migration Management:**
    - Apply migrations defined in SQL files.
    - Manage the sequence of migrations (e.g., based on file naming).

- **Version Control:**
    - Manage database versions.
    - Handle errors during database migration (rollback applied changes within the migration).
  
- **Configuration:**
    - Load all parameters (e.g., database connection details) from an `application.properties` file.

- **Migration File Sources:**
    - Load SQL migration files from application resources.

- **Migration Synchronization:**
    - Prevent simultaneous migration by different users.
    - Implement a locking mechanism to prevent conflicts.

- **Logging:**
    - Log migration execution process (start, successful execution, errors).
    - Store migration history in a dedicated database table.

## Usage Instructions

1. **Integration:**
    - Add the library to your Java project's.
    - Import the necessary classes in your code.

2. **Configuration:**
    - Ensure that the `application.properties` file contains the required database connection parameters.

3. **Defining Migrations:**
    - Create SQL migration files with the desired schema changes.
    - Name the migration files appropriately to ensure the correct sequence.

4. **Initializing Migration:**
    - Instantiate the migration service from the library.
    - Call the migration method to apply the migrations to the database.

5. **Handling Errors:**
    - Implement error handling for database migration failures.

6. **Logging and History:**
    - Check the logs for migration process details.
    - Verify the migration history in the designated database table.

For detailed usage and method documentation, refer to the JavaDoc comments in the library classes.