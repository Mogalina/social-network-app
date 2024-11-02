package org.example.exceptions;

/**
 * Exception to indicate that the parameters are not suitable for the function.
 */
public class InvalidParametersException extends Throwable {

    /**
     * Constructs a new InvalidParametersException with a default message.
     */
    public InvalidParametersException() {
        super("Invalid number of parameters. Check documentation (command: 'docs') for more information.");
    }

    /**
     * Constructs a new InvalidParametersException with the specified detail message.
     *
     * @param message the detail message of the exception
     */
    public InvalidParametersException(String message) {
        super(message);
    }
}
