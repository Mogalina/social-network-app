package org.example.utils;

import org.example.exceptions.ValidationException;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

/**
 * Utility class for password-related operations.
 * This class provides exclusively static methods accessable accross the application without instantiating objects.
 */
public class PasswordUtils {

    // Regular expression for validating password format (at least 8 chars, 1 letter, 1 number, 1 special char)
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{8,}$";

    /**
     * Hashes the provided password using the SHA-256 algorithm.
     *
     * @param password the plain text password to be hashed
     * @return the hashed password as a hexadecimal string
     */
    @NotNull
    public static String hashPassword(@NotNull String password) {
        try {
            // Create a SHA-256 message digest instance
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convert the hashed bytes to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Checks if the provided password follows the right format.
     *
     * @param password the password to be checked
     * @throws ValidationException if the password is null or fails format criteria
     */
    public static void checkPasswordFormat(@NotNull String password) throws ValidationException {
        if (!Pattern.matches(PASSWORD_REGEX, password)) {
            throw new ValidationException("Invalid password format");
        }
    }
}
