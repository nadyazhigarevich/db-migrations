package org.zhigarevich.dbmigration.facade.reader.validator;

import org.zhigarevich.dbmigration.exception.BrokenValidationException;

import java.nio.file.Files;
import java.nio.file.Path;


/**
 * Functional interface for validating objects of type T.
 *
 * @param <T> the type of object to validate
 */
@FunctionalInterface
public interface Validator<T> {

    /**
     * Validate the given object of type T.
     *
     * @param value the object to validate
     */
    void validate(final T value);

    /**
     * Validate a property with a given key and value. Throws a BrokenValidationException if the value is empty.
     *
     * @param key   the key of the property
     * @param value the value of the property
     */
    default void validateProperty(final String key, final String value) {
        if (value.isBlank()) {
            throw new BrokenValidationException(String.format("'%s' must not be empty.", key));
        }
    }

    /**
     * Validate a Path object, checking if it exists and is a directory. Throws a BrokenValidationException if not.
     *
     * @param folder the Path to validate
     */
    default void validatePath(final Path folder) {
        if (!Files.exists(folder) || !Files.isDirectory(folder)) {
            throw new BrokenValidationException(String.format("No such directory '%s'.", folder.getFileName().toString()));
        }
    }
}