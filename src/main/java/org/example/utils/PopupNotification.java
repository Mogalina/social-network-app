package org.example.utils;

import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * A utility class for displaying popup notifications on the screen.
 */
public class PopupNotification {

    /**
     * Displays a notification message as a popup on the specified stage.
     * The popup will appear at the top center of the stage and automatically hide after the specified duration.
     *
     * @param stage the stage on which the popup will appear
     * @param message the message to be displayed in the popup
     * @param durationInMillis the duration in milliseconds for which the popup will remain visible
     * @param backgroundColor the background color of the notification in hex
     */
    public static void showNotification(Stage stage, String message, int durationInMillis, String backgroundColor) {
        // Create a Popup
        Popup popup = new Popup();

        // Create a Label to display the message
        Label label = new Label(message);

        // Apply styles directly to the Label in code
        label.setStyle(
                "-fx-background-color: " + backgroundColor + "; " +
                "-fx-text-fill: #f0f0f0; " +
                "-fx-padding: 10px 20px; " +
                "-fx-font-size: 15px; " +
                "-fx-border-radius: 20px; " +
                "-fx-background-radius: 20px;"
        );

        // Add the Label to the Popup
        popup.getContent().add(label);

        // Calculate the position of the popup at the top center of the window
        double xPosition = stage.getX() + stage.getWidth() / 2 - 100;
        double yPosition = stage.getY() + 20;

        // Show the Popup
        popup.show(stage, xPosition, yPosition);

        // Auto-hide the Popup after the specified duration
        new Thread(() -> {
            try {
                Thread.sleep(durationInMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            javafx.application.Platform.runLater(popup::hide);
        }).start();
    }
}
