package org.example.utils;

import org.example.exceptions.ValidationException;

import java.util.regex.Pattern;

/**
 * Utility class for email-related operations.
 * This class provides exclusively static methods accessable accross the application without instantiating objects.
 */
public class EmailUtils {

    // Regular expression for validating email address format
    private static final String EMAIL_REGEX = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    /**
     * Checks if the provided email address follows the right format.
     *
     * @param email the email address to be checked
     * @throws ValidationException if the email address is null or fails format criteria
     */
    public static void checkEmailFormat(String email) throws ValidationException {
        if (email == null) {
            throw new ValidationException("Email must not be null");
        }
        if (!Pattern.matches(EMAIL_REGEX, email)) {
            throw new ValidationException("Invalid email address format");
        }
    }
}
