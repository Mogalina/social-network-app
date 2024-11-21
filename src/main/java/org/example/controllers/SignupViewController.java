package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.models.User;
import org.example.middleware.Auth;
import org.example.service.Network;
import org.example.utils.PasswordUtils;
import org.example.utils.PopupNotification;
import org.example.utils.SceneUtils;

/**
 * The SignupViewController class handles the logic for user registration (signup) in the application.
 */
public class SignupViewController {

    @FXML
    public TextField firstNameField; // Field for entering the user's first name

    @FXML
    public TextField lastNameField; // Field for entering the user's last name

    @FXML
    public TextField emailField; // Field for entering the user's email address

    @FXML
    public PasswordField passwordField; // Field for entering the user's password

    /**
     * Handles the signup button click event.
     *
     * This method processes the input data from the form, checks if the email is already registered, and attempts to
     * create a new user. If successful, the user is redirected to the login page.
     */
    @FXML
    public void handleConfirmSignupClick() {
        // Strip any leading or trailing spaces from the input fields
        String firstName = firstNameField.getText().strip();
        String lastName = lastNameField.getText().strip();
        String email = emailField.getText().strip();
        String password = passwordField.getText().strip();

        // Get the current stage (window) for showing popups
        Stage stage = (Stage) emailField.getScene().getWindow();

        // Get the network instance to interact with user data
        Network network = GlobalNetwork.getNetwork();

        // Check if the email is already registered using the AuthService
        User searchedUser = Auth.signup(email);

        if (searchedUser == null) {
            try {
                // Attempt to add new user to network
                String hashedPassword = PasswordUtils.hashPassword(password);
                User user = new User(firstName, lastName, hashedPassword, email);
                network.addUser(user);

                // Show a success notification and redirect to the login page
                PopupNotification.showNotification(stage, "Account created successfully", 4000, "#68c96d");

                // Trigger the login page transition
                handleLoginClick();
            } catch (Exception e) {
                PopupNotification.showNotification(stage, e.getMessage(), 4000, "#ef5356");
            }
        } else {
            PopupNotification.showNotification(stage, "Email address already exists", 4000, "#ef5356");
        }
    }

    /**
     * Redirects the user to the login screen.
     */
    @FXML
    public void handleLoginClick() {
        Stage stage = (Stage) emailField.getScene().getWindow();
        SceneUtils.switchScene(stage, "/visuals/views/login-view.fxml");
    }
}
