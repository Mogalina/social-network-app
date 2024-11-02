package org.example.exceptions;

/**
 * Exception to indicate that an entity does not exist in the system.
 */
public class EntityNotFoundException extends Throwable {

    /**
     * Constructs a new EntityNotFoundException with a default message.
     */
    public EntityNotFoundException() {
        super("Entity does not exist");
    }

    /**
     * Constructs a new EntityNotFoundException with the specified detail message.
     *
     * @param message the detail message of the exception
     */
    public EntityNotFoundException(String message) {
        super(message);
    }
}
