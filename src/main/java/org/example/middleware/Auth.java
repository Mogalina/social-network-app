package org.example.middleware;

import org.example.controllers.GlobalNetwork;
import org.example.models.User;
import org.example.service.Network;
import org.example.utils.PasswordUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * The AuthService class provides methods for user authentication.
 * It includes functionality for logging in users and signing up new users.
 * It interacts with the network to retrieve user data and verifies credentials.
 */
public class Auth {

    /**
     * Logs in a user by checking the provided email and password.
     * It compares the email and hashed password with the existing users in the network.
     *
     * @param email the email address of the user attempting to log in
     * @param password the plaintext password provided by the user
     * @return the {@link User} object if the credentials match, null if no match is found
     */
    public static User login(String email, String password) {
        Network network = GlobalNetwork.getNetwork();

        String hashedPassword = PasswordUtils.hashPassword(password);

        Optional<User> user = StreamSupport
                .stream(network.getAllUsers().spliterator(), false)
                .filter(u -> Objects.equals(u.getEmail(), email) && Objects.equals(u.getPassword(), hashedPassword))
                .findFirst();

        return user.orElse(null);
    }

    /**
     * Signs up a new user by checking if the provided email already exists in the system.
     *
     * @param email the email address of the user attempting to sign up
     * @return the {@link User} object if an existing user with the same email is found, null if no match is found
     */
    public static User signup(String email) {
        Network network = GlobalNetwork.getNetwork();

        Optional<User> user = StreamSupport
                .stream(network.getAllUsers().spliterator(), false)
                .filter(u -> Objects.equals(u.getEmail(), email))
                .findFirst();

        return user.orElse(null);
    }
}
