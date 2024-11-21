package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.models.User;
import org.example.service.Network;
import org.example.utils.PopupNotification;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Controller class that handles adding friends through search functionality.
 */
public class AddFriendController {

    // The Network instance used for communication with the server
    private Network network;

    // Observable list to hold the search results (user emails)
    private final ObservableList<String> searchResults = FXCollections.observableArrayList();

    // Collection of all users fetched from the network
    private Iterable<User> allUsers = List.of();

    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> resultsListView;

    /**
     * Initializes the controller by setting up the network, fetching all users, and setting listeners for the search
     * field and results list view.
     */
    @FXML
    public void initialize() {
        // Initialize the network connection
        network = GlobalNetwork.getNetwork();

        // Fetch all users from the network asynchronously
        fetchAllUsers();

        // Set the search results observable list to the ListView
        resultsListView.setItems(searchResults);

        // Add listener for the search field to filter users as the user types
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                // Clear search results if the input is empty
                searchResults.clear();
            } else {
                // Filter the users based on the query text
                filterUsers(newValue.trim());
            }
        });

        // Add mouse click listener to set the selected search result into the search field
        resultsListView.setOnMouseClicked(event -> {
            String selectedItem = resultsListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                // Set the selected email from results into the search field
                searchField.setText(selectedItem);
            }
        });
    }

    /**
     * Fetches all users from the network in a separate thread to avoid blocking the UI.
     */
    private void fetchAllUsers() {
        new Thread(() -> {
            // Fetch all users from the network (blocking operation)
            allUsers = network.getAllUsers();

            // Switch to the JavaFX Application thread to update the UI
            javafx.application.Platform.runLater(() -> {
                // Extract emails from the user objects and populate the searchResults list
                List<String> userEmails = StreamSupport.stream(allUsers.spliterator(), false)
                        .map(User::getEmail)
                        .collect(Collectors.toList());

                // Update the search results
                searchResults.setAll(userEmails);
            });
        }).start();
    }

    /**
     * Filters the list of users based on the search query and updates the searchResults list.
     *
     * @param query the search query to filter users by email
     */
    private void filterUsers(String query) {
        // Filter the list of all users based on the query (case-insensitive)
        List<String> filteredUsers = StreamSupport.stream(allUsers.spliterator(), false)
                .map(User::getEmail)
                .filter(email -> email.toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());

        // Update the search results list with the filtered emails
        searchResults.setAll(filteredUsers);
    }

    /**
     * Handles the action of sending a friend request to the user entered in the search field.
     */
    public void handleAddFriend() {
        // Get the current window's stage
        Stage stage = (Stage) searchField.getScene().getWindow();

        // Get the email entered in the search field
        String email = searchField.getText();

        // Check if a user with the entered email exists in the network
        Optional<User> receiver = network.findUserByEmail(email);

        if (receiver.isPresent()) {
            // Send a friend request to the selected user
            network.sendFriendRequest(UserController.getUser().getId(), receiver.get().getId());

            PopupNotification.showNotification(stage, "Request sent successfully", 4000, "#68c96d");
        } else {
            PopupNotification.showNotification(stage, "Invalid email address", 4000, "#ef5356");
        }
    }
}
