package org.example.controllers;

import org.example.models.User;

public class UserController {

    private static User user = null;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        UserController.user = user;
    }
}
