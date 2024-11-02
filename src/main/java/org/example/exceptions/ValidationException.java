package org.example.exceptions;

/**
 * Exception thrown when a validation error occurs.
 *
 * This class extends {@link RuntimeException} and is used to indicate that a validation error has occurred in the
 * application. It provides several methods for creating validation exception with different levels of detail.
 */
public class ValidationException extends RuntimeException {

    /**
     * Constructs a new ValidationException with no detail message.
     */
    public ValidationException() {
        super();
    }

    /**
     * Constructs a new ValidationException with the specified detail message.
     *
     * @param message the detail message
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructs a new ValidationException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause (null value is permitted and indicates that the cause is unknown)
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new ValidationException with the specified cause.
     *
     * @param cause the cause (null value is permitted and indicates that the cause is unknown)
     */
    public ValidationException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new ValidationException with the specified detail message, cause, suppression (enabled or disabled),
     * and writable stack trace (enabled or disabled).
     *
     * @param message the detail message
     * @param cause the cause (null value is permitted and indicates that the cause is unknown)
     * @param enableSuppression  whether or not suppression is enabled or disabled
     * @param writableStackTrace whether or not the stack trace should be writable
     */
    public ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
