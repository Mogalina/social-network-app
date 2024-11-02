package org.example.models.validators;

import org.example.exceptions.ValidationException;

/**
 * A generic functional interface for validating entities.
 *
 * This interface defines a contract for classes that need to validate entities of a specific type before they are
 * processed. Implementations of this interface can perform custom validation logic to ensure that entities meet
 * specific requirements.
 *
 * @param <T> the type of entity to be validated
 */
@FunctionalInterface
public interface Validator<T> {

    /**
     * Validates the given entity.
     *
     * This method performs validation on the specified entity. If the entity does not meet the validation requirements,
     * a {@link ValidationException} is thrown. Implementations can define the specific validation logic.
     *
     * @param entity the entity to be validated
     * @throws ValidationException if the entity fails to meet the validation criteria
     */
    void validate(T entity) throws ValidationException;
}
