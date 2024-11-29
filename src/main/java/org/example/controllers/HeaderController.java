package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import org.example.models.Notification;
import org.example.models.Observable;
import org.example.models.Observer;
import org.example.service.Network;
import org.example.utils.SceneUtils;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

/**
 * Controller class for handling user interactions in the header section of the application.
 */
public class HeaderController implements Observer {

    // The Network instance used for communication with the server
    private Network network;

    // Observable list to hold the user notifications (notification description)
    private final ObservableList<String> notifications = FXCollections.observableArrayList();

    @FXML
    private ImageView homeImage;

    @FXML
    private ImageView messagesImage;

    @FXML
    private ImageView profileImage;

    @FXML
    private ImageView notificationsImage;

    @FXML
    public void initialize() {
        // Initialize the network service and register the controller as an observer
        network = GlobalNetwork.getNetwork();
        network.addObserver(this);

        // Fetch all notifications of the user from the network
        fetchAllNotifications();
    }

    /**
     * Called when the observable object notifies observers of an update.
     * Fetches the latest data for notification updates.
     */
    @Override
    public void update(Observable o, Object arg) {
        fetchAllNotifications();
    }

    /**
     * Fetches all notification updates for the logged-in user.
     */
    private void fetchAllNotifications() {
        // Fetch all notifications of logged-in user from the network
        Iterable<Notification> allNotifications = network.getUserNotifications(UserController.getUser().getId());

        // Convert notifications to plain text and update the notifications list
        List<String> plainTextNotifications = StreamSupport.stream(allNotifications.spliterator(), false)
                .map(Notification::getDescription)
                .toList();
        notifications.setAll(plainTextNotifications);

        // Display specific notifications icon
        Image newImage;
        if (plainTextNotifications.isEmpty()) {
            newImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/assets/images/pngs/notifications-icon.png")));
        } else {
            newImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/assets/images/pngs/popup-notifications-icon.png")));
        }
        notificationsImage.setImage(newImage);
    }

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

    /**
     * Handles the navigation to the Notifications View when the notificationsImage button is clicked.
     */
    public void goToNotifications() {
        // Get the current stage (window)
        Stage stage = (Stage) notificationsImage.getScene().getWindow();

        // Switch to the Profile View
        SceneUtils.switchScene(stage, "/visuals/views/notifications-view.fxml");
    }
}
