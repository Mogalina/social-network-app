package org.example.exceptions;

/**
 * Exception to indicate that an entity already exists in the system.
 */
public class EntityAlreadyExistsException extends Throwable {

    /**
     * Constructs a new EntityAlreadyExistsException with a default message.
     */
    public EntityAlreadyExistsException() {
        super("Entity already exists");
    }

    /**
     * Constructs a new EntityAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message of the exception
     */
    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}
