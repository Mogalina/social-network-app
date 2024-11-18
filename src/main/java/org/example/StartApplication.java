package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

/**
 * Entry point for the JavaFX application.
 * This class initializes the primary stage and sets up the initial scene for the application.
 */
public class StartApplication extends Application {

    /**
     * Starts the JavaFX application by setting up the primary stage and its scene.
     *
     * @param primaryStage the primary stage for the application
     * @throws IOException if the FXML file for the initial scene cannot be loaded
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = getLoader("/visuals/open-view.fxml");

        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        primaryStage.setTitle("Social Network");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    /**
     * Utility method to create an {@link FXMLLoader} for a given FXML file path.
     *
     * @param name the relative path to the FXML file
     * @return an {@link FXMLLoader} instance configured with the specified FXML file
     * @throws IllegalArgumentException if the FXML file cannot be located
     */
    public static FXMLLoader getLoader(String name) {
        FXMLLoader loader = new FXMLLoader(StartApplication.class.getResource(name));
        if (loader.getLocation() == null) {
            throw new IllegalArgumentException("FXML file not found: " + name);
        }
        return loader;
    }

    /**
     * The main entry point of the JavaFX application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        launch(args);
    }
}
