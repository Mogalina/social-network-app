package org.example.controllers;

import org.example.utils.SceneUtils;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

public class HeaderController {

    @FXML
    private ImageView homeImage;

    @FXML
    private ImageView messagesImage;

    @FXML
    private ImageView profileImage;

    public void goToHome() {
        Stage stage = (Stage) homeImage.getScene().getWindow();
        SceneUtils.switchScene(stage, "/visuals/views/home-view.fxml");
    }

    public void goToMessages() {
        Stage stage = (Stage) messagesImage.getScene().getWindow();
        SceneUtils.switchScene(stage, "/visuals/views/messages-view.fxml");
    }

    public void goToProfile() {
        Stage stage = (Stage) profileImage.getScene().getWindow();
        SceneUtils.switchScene(stage, "/visuals/views/profile-view.fxml");
    }
}
