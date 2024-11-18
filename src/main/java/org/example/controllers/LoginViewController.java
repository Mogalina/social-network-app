package org.example.controllers;

import org.example.models.User;
import org.example.middleware.Auth;
import org.example.utils.PopupNotification;
import org.example.utils.SceneUtils;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginViewController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    public void handleConfirmLoginClick() {
        String email = emailField.getText().strip();
        String password = passwordField.getText().strip();

        Stage stage = (Stage) emailField.getScene().getWindow();

        User user = Auth.login(email, password);
        if (user != null) {
            SceneUtils.switchScene(stage, "/visuals/home-view.fxml");
        } else {
            PopupNotification.showNotification(stage, "Invalid email or password", 5000, "#ef5356");
        }
    }

    @FXML
    public void handleSignupClick() {
        Stage stage = (Stage) emailField.getScene().getWindow();
        SceneUtils.switchScene(stage, "/visuals/signup-view.fxml");
    }
}
