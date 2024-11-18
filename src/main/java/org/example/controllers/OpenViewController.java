package org.example.controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.utils.SceneUtils;

/**
 * Controller for the open view of the application.
 * Handles animations for the title and subtitle text and transitions to the login view after animations complete.
 */
public class OpenViewController {

    @FXML
    private Text titleText;

    @FXML
    public Text subtitleText;

    /**
     * Initializes the controller after the FXML file has been loaded.
     * Sets up fade-in animations for the title and subtitle, transitioning to the next scene upon completion.
     */
    public void initialize() {
        subtitleText.setOpacity(0.0);

        // Create a fade-in transition for the title text
        FadeTransition fadeInTitle = new FadeTransition(Duration.seconds(3), titleText);
        fadeInTitle.setFromValue(0.0);
        fadeInTitle.setToValue(1.0);
        fadeInTitle.setCycleCount(1);
        fadeInTitle.setAutoReverse(false);

        // When the title animation finishes, start the subtitle animation
        fadeInTitle.setOnFinished(event -> {
            FadeTransition fadeInSubtitle = new FadeTransition(Duration.seconds(1), subtitleText);
            fadeInSubtitle.setFromValue(0.0);
            fadeInSubtitle.setToValue(1.0);
            fadeInSubtitle.setCycleCount(1);
            fadeInSubtitle.setAutoReverse(false);

            // When the subtitle animation finishes, switch to the login scene
            fadeInSubtitle.setOnFinished(subEvent -> {
                Stage stage = (Stage) titleText.getScene().getWindow();
                SceneUtils.switchScene(stage, "/visuals/login-view.fxml");
            });

            // Start the subtitle animation
            fadeInSubtitle.play();
        });

        // Start the title animation
        fadeInTitle.play();
    }
}
