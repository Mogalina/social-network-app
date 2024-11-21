package org.example.controllers;

import org.example.models.User;
import org.example.middleware.Auth;
import org.example.utils.PopupNotification;
import org.example.utils.SceneUtils;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for handling user login interactions.
 * It manages the login process and switches to the home view upon successful login.
 * It also allows navigation to the signup view for users who don't have an account.
 */
public class LoginViewController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    /**
     * Handles the login button click.
     * It verifies the login credentials, and switches to the Home View.
     */
    @FXML
    public void handleConfirmLoginClick() {
        // Get the email and password entered by the user, stripping any extra spaces
        String email = emailField.getText().strip();
        String password = passwordField.getText().strip();

        // Get the current stage (window)
        Stage stage = (Stage) emailField.getScene().getWindow();

        // Attempt to log in
        User user = Auth.login(email, password);

        if (user != null) {
            UserController.setUser(user);
            SceneUtils.switchScene(stage, "/visuals/views/home-view.fxml");
        } else {
            PopupNotification.showNotification(stage, "Invalid email or password", 4000, "#ef5356");
        }
    }

    /**
     * Handles the signup button click.
     */
    @FXML
    public void handleSignupClick() {
        // Get the current stage (window)
        Stage stage = (Stage) emailField.getScene().getWindow();

        // Switch to the Signup View
        SceneUtils.switchScene(stage, "/visuals/views/signup-view.fxml");
    }
}
