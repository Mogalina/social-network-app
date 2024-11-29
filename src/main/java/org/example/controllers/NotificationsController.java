package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import org.example.models.Notification;
import org.example.models.Observable;
import org.example.models.Observer;
import org.example.service.Network;

import java.util.List;
import java.util.stream.StreamSupport;

/**
 * Controller class that handles notification updates for the logged-in user.
 */
public class NotificationsController implements Observer {

    // The Network instance used for communication with the server
    private Network network;

    // All notifications of logged-in user
    private Iterable<Notification> allNotifications = List.of();

    // Observable list to hold the user notifications (notification description)
    private final ObservableList<String> notifications = FXCollections.observableArrayList();

    @FXML
    private ListView<String> notificationsList;

    @FXML
    private Text noNotificationsText;

    @FXML
    private Button confirmBtn;

    @FXML
    public void initialize() {
        // Initialize the network service and register the controller as an observer
        network = GlobalNetwork.getNetwork();
        network.addObserver(this);

        // Fetch all notifications of the user from the network
        fetchAllNotifications();

        // Set the notifications observable list to the ListView
        notificationsList.setItems(notifications);
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
        // Fetch notifications from the network
        allNotifications = network.getUserNotifications(UserController.getUser().getId());

        // Convert notifications to plain text and update the notifications list
        List<String> plainTextNotifications = StreamSupport.stream(allNotifications.spliterator(), false)
                .map(Notification::getDescription)
                .toList();
        notifications.setAll(plainTextNotifications);

        // Display information message of no existent notifications if notifications list is empty
        if (plainTextNotifications.isEmpty()) {
            notificationsList.setVisible(false);
            notificationsList.setManaged(false);
            confirmBtn.setVisible(false);
            confirmBtn.setManaged(false);
            noNotificationsText.setVisible(true);
            noNotificationsText.setManaged(true);
        } else {
            notificationsList.setVisible(true);
            notificationsList.setManaged(true);
            confirmBtn.setVisible(true);
            confirmBtn.setManaged(true);
            noNotificationsText.setVisible(false);
            noNotificationsText.setManaged(false);
        }
    }

    /**
     * Mark all notifications as read and remove them.
     */
    public void handleMarkNotifications() {
        StreamSupport.stream(allNotifications.spliterator(), false)
                .forEach(notification -> network.deleteNotification(notification.getId()));
    }
}
