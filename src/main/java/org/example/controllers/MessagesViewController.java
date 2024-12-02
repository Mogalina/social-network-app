package org.example.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.models.Message;
import org.example.models.Observable;
import org.example.models.Observer;
import org.example.models.User;
import org.example.service.Network;
import org.example.utils.PopupNotification;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.util.Optional;

public class MessagesViewController implements Observer {

    // Network service for fetching user-related data
    private Network network;

    // All friends of the logged-in user
    private Iterable<User> allUsers = List.of();

    // All chat messages between two users
    private Iterable<Message> allMessages = List.of();

    // Observable lists for search results and conversation messages
    private final ObservableList<String> searchResults = FXCollections.observableArrayList();
    private final ObservableList<String> messages = FXCollections.observableArrayList();

    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> resultsListView;

    @FXML
    private StackPane chatIntroPannel;

    @FXML
    private VBox chatPannel;

    @FXML
    private Text receiverEmail;

    @FXML
    private TextField messageField;

    @FXML
    private VBox chatMessages;

    @FXML
    private ScrollPane messagesScroller;

    /**
     * Initializes the controller, sets up observers, and initializes the UI.
     */
    @FXML
    public void initialize() {
        // Initialize the network service and register the controller as an observer
        network = GlobalNetwork.getNetwork();
        network.addObserver(this);

        // Fetch friends and populate the search results list
        fetchAllFriends();

        // Set up the ListView for search results
        resultsListView.setItems(searchResults);
        resultsListView.setVisible(false);
        resultsListView.setManaged(false);
        chatPannel.setVisible(false);
        chatPannel.setManaged(false);

        // Set up listener for text changes in the search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                // Clear search results if input is empty
                searchResults.clear();
                resultsListView.setVisible(false);
                resultsListView.setManaged(false);
            } else {
                // Filter users based on search input
                filterUsers(newValue.trim());
                resultsListView.setVisible(true);
                resultsListView.setManaged(true);
            }
        });

        // Set up click listener for results list
        resultsListView.setOnMouseClicked(event -> {
            String selectedItem = resultsListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                searchField.setText(selectedItem);
                resultsListView.setVisible(false);
                resultsListView.setManaged(false);
                chatIntroPannel.setVisible(false);
                chatIntroPannel.setManaged(false);
                chatPannel.setVisible(true);
                chatPannel.setManaged(true);
                receiverEmail.setText(selectedItem);

                // Fetch chat messages for the selected user
                fetchAllMessages();

                // Scroll to the bottom of the chat messages pane
                Platform.runLater(this::scrollToBottom);
            }
        });
    }

    /**
     * Called when the observable object notifies observers of an update.
     * Fetches the latest data for friends and chat messages.
     */
    @Override
    public void update(Observable o, Object arg) {
        fetchAllFriends();
        fetchAllMessages();
    }

    /**
     * Fetches all the logged-in user's friends from the network and updates the search results.
     */
    private void fetchAllFriends() {
        // Fetch all friends of current logged-in user
        allUsers = network.getFriendsOfUser(UserController.getUser().getId());

        // Convert users to plain text emails and update search results
        List<String> userEmails = StreamSupport.stream(allUsers.spliterator(), false)
                .map(User::getEmail)
                .collect(Collectors.toList());
        searchResults.setAll(userEmails);
    }

    /**
     * Fetches all chat messages between the logged-in user and the selected recipient.
     */
    private void fetchAllMessages() {
        // Retrieve the receiver user using their email
        Optional<User> receiverUser = network.findUserByEmail(receiverEmail.getText());

        // Fetch chat messages from the network
        allMessages = network.getChat(UserController.getUser().getId(), receiverUser.get().getId());

        // Convert messages to plain text and update the messages list
        List<String> plainTextMessages = StreamSupport.stream(allMessages.spliterator(), false)
                .map(Message::getMessage)
                .toList();
        messages.setAll(plainTextMessages);

        // Display the chat messages in the UI
        displayChatMessages();
    }

    /**
     * Filters the list of users based on the search query.
     *
     * @param query the search query string
     */
    private void filterUsers(String query) {
        List<String> filteredUsers = StreamSupport.stream(allUsers.spliterator(), false)
                .map(User::getEmail)
                .filter(email -> email.toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());

        // Update search results
        searchResults.setAll(filteredUsers);
    }

    /**
     * Handles sending a new message.
     * Retrieves the receiver and sends the message through the network.
     */
    public void handleSendMessage() {
        Stage stage = (Stage) messageField.getScene().getWindow();

        try {
            // Retrieve the receiver user by email
            Optional<User> receiverUser = network.findUserByEmail(receiverEmail.getText());
            if (receiverUser.isEmpty()) {
                PopupNotification.showNotification(stage, "Please select a friend", 4000, "#ef5356");
                return;
            }

            // Create a new message object
            Message message = new Message(UserController.getUser().getId(),
                    receiverUser.get().getId(),
                    messageField.getText());

            // Send the message through the network
            network.addMessage(message);

            // Clear the message field
            messageField.clear();

            // Refresh chat messages
            fetchAllMessages();
        } catch (Exception e) {
            PopupNotification.showNotification(stage, e.getMessage(), 4000, "#ef5356");
        }
    }

    /**
     * Displays chat messages in the VBox.
     * Aligns messages to the left or right based on the sender.
     */
    private void displayChatMessages() {
        // Clear existing messages
        chatMessages.getChildren().clear();

        // Create and add message containers for each sent message between the two users
        List<Node> messageNodes = StreamSupport.stream(allMessages.spliterator(), false)
                .map(message -> {
                    Label messageLabel = new Label(message.getMessage());
                    messageLabel.getStyleClass().add("message-label");

                    HBox messageContainer = new HBox(messageLabel);
                    messageContainer.setMaxWidth(Double.MAX_VALUE);

                    // Align the message based on the sender/receiver
                    if (Objects.equals(message.getSenderId(), UserController.getUser().getId())) {
                        messageContainer.setAlignment(Pos.CENTER_RIGHT);
                        messageLabel.getStyleClass().add("sender-message-label");
                    } else {
                        messageContainer.setAlignment(Pos.CENTER_LEFT);
                        messageLabel.getStyleClass().add("receiver-message-label");
                    }

                    return messageContainer;
                })
                .collect(Collectors.toList());

        // Add all message containers to the VBox
        chatMessages.getChildren().addAll(messageNodes);

        // Scroll to the bottom of the messages
        Platform.runLater(this::scrollToBottom);
    }

    /**
     * Scrolls the message pane to the bottom.
     * This ensures the latest message is visible.
     */
    private void scrollToBottom() {
        messagesScroller.setVvalue(1.0);
    }
}
