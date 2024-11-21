package org.example.controllers;

import org.example.utils.SceneUtils;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

/**
 * Controller class for handling user interactions in the header section of the application.
 */
public class HeaderController {

    @FXML
    private ImageView homeImage;

    @FXML
    private ImageView messagesImage;

    @FXML
    private ImageView profileImage;

    /**
     * Handles the navigation to the Home View when the homeImage button is clicked.
     */
    public void goToHome() {
        // Get the current stage (window)
        Stage stage = (Stage) homeImage.getScene().getWindow();

        // Switch to the Home View
        SceneUtils.switchScene(stage, "/visuals/views/home-view.fxml");
    }

    /**
     * Handles the navigation to the Messages View when the messagesImage button is clicked.
     */
    public void goToMessages() {
        // Get the current stage (window)
        Stage stage = (Stage) messagesImage.getScene().getWindow();

        // Switch to the Messages View
        SceneUtils.switchScene(stage, "/visuals/views/messages-view.fxml");
    }

    /**
     * Handles the navigation to the Profile view when the profileImage button is clicked.
     */
    public void goToProfile() {
        // Get the current stage (window)
        Stage stage = (Stage) profileImage.getScene().getWindow();

        // Switch to the Profile View
        SceneUtils.switchScene(stage, "/visuals/views/profile-view.fxml");
    }
}
