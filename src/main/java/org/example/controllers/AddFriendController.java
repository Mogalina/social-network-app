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

public class AddFriendController {

    private Network network;

    private final ObservableList<String> searchResults = FXCollections.observableArrayList();

    private Iterable<User> allUsers = List.of();

    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> resultsListView;

    @FXML
    public void initialize() {
        network = GlobalNetwork.getNetwork();

        fetchAllUsers();

        resultsListView.setItems(searchResults);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                searchResults.clear();
            } else {
                filterUsers(newValue.trim());
            }
        });

        resultsListView.setOnMouseClicked(event -> {
            String selectedItem = resultsListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                searchField.setText(selectedItem);
            }
        });
    }

    private void fetchAllUsers() {
        new Thread(() -> {
            allUsers = network.getAllUsers();
            javafx.application.Platform.runLater(() -> {
                List<String> userEmails = StreamSupport.stream(allUsers.spliterator(), false)
                        .map(User::getEmail)
                        .collect(Collectors.toList());
                searchResults.setAll(userEmails);
            });
        }).start();
    }

    private void filterUsers(String query) {
        List<String> filteredUsers = StreamSupport.stream(allUsers.spliterator(), false)
                .map(User::getEmail)
                .filter(email -> email.toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());

        searchResults.setAll(filteredUsers);
    }

    public void handleAddFriend() {
        Stage stage = (Stage) searchField.getScene().getWindow();

        String email = searchField.getText();
        Optional<User> receiver = network.findUserByEmail(email);
        if (receiver.isPresent()) {
            network.sendFriendRequest(UserController.getUser().getId(), receiver.get().getId());
            PopupNotification.showNotification(stage, "Request sent successfully", 4000, "#68c96d");
        } else {
            PopupNotification.showNotification(stage, "Invalid email address", 4000, "#ef5356");
        }
    }
}
