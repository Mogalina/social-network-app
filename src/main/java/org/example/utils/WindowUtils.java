package org.example.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Utility class for handling window (stage) transitions in a JavaFX application.
 * Provides methods to open a new window without closing the current one.
 */
public class WindowUtils {

    /**
     * Opens a new window (stage) with the scene specified by the FXML file path.
     *
     * @param fxmlPath the path to the FXML file for the new scene
     * @param title the title for the new window
     */
    public static void openNewWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(WindowUtils.class.getResource(fxmlPath));
            Parent newRoot = loader.load();

            Scene newScene = new Scene(newRoot);
            Stage newStage = new Stage();
            newStage.setScene(newScene);
            newStage.setTitle(title);
            newStage.centerOnScreen();
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
