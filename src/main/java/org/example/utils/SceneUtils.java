package org.example.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Utility class for handling scene transitions in a JavaFX application.
 * Provides methods to switch the current scene to a new scene based on the specified FXML file.
 */
public class SceneUtils {

    /**
     * Switches the current scene to a new scene specified by the FXML file path.
     *
     * @param stage the current stage on which to set the new scene
     * @param fxmlPath the path to the FXML file for the new scene
     */
    public static void switchScene(Stage stage, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneUtils.class.getResource(fxmlPath));
            Parent newRoot = loader.load();

            stage.setScene(new Scene(newRoot));
            stage.setFullScreen(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
