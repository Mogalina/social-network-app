package org.example.controllers;

import org.example.models.User;

/**
 * Controller for managing the current logged-in user.
 */
public class UserController {

    // Store the currently logged-in user
    private static User user = null;

    /**
     * Retrieves the currently logged-in user.
     *
     * @return the {@link User} object representing the currently logged-in user
     */
    public static User getUser() {
        return user;
    }

    /**
     * Sets the currently logged-in user.
     *
     * @param user the {@link User} object representing the user to be logged in
     */
    public static void setUser(User user) {
        UserController.user = user;
    }
}
