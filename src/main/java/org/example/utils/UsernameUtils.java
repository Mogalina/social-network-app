package org.example.utils;

import org.example.exceptions.ValidationException;

import java.util.regex.Pattern;

/**
 * Utility class for username-related operations.
 * This class provides exclusively static methods accessable accross the application without instantiating objects.
 */
public class UsernameUtils {

    // Regular expression for validating username format (3 to 20 characters in [a-zA-z0-9._-])
    private static final String NAME_REGEX = "^[a-zA-Z0-9._-]{3,20}$";

    /**
     * Checks if the provided name follows the right format.
     *
     * @param name the name to be checked
     * @throws ValidationException if the name is null or fails format criteria
     */
    public static void checkNameFormat(String name) throws ValidationException {
        if (name == null) {
            throw new ValidationException("Name must not be null");
        }
        if (!Pattern.matches(NAME_REGEX, name)) {
            throw new ValidationException("Invalid name format");
        }
    }
}
