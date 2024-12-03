package org.example.controllers;

import javafx.scene.control.*;
import org.example.exceptions.EntityAlreadyExistsException;
import org.example.models.Observable;
import org.example.models.User;
import org.example.models.dtos.UserFilterDTO;
import org.example.service.Network;
import org.example.utils.Paging.Page;
import org.example.utils.Paging.Pageable;
import org.example.utils.PopupNotification;
import org.example.models.Observer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.List;
import java.util.stream.StreamSupport;

/**
 * Controller class that handles adding friends through search functionality.
 */
public class AddFriendController implements Observer {

    // The Network instance used for communication with the server
    private Network network;

    // Observable list to hold the search results (user emails)
    private final ObservableList<User> searchResults = FXCollections.observableArrayList();

    // Strings to store the selected email in table
    private String selectedEmail = null;

    // Filter object for search criteria
    private final UserFilterDTO filter = new UserFilterDTO();

    // Tracks the current page of search results
    private int currentPage = 0;

    @FXML
    private TextField firstNameSearchField;

    @FXML
    private TextField lastNameSearchField;

    @FXML
    private TextField emailSearchField;

    @FXML
    private TableView<User> resultsTableView;

    @FXML
    private TableColumn<User, String> firstNameCol;

    @FXML
    private TableColumn<User, String> lastNameCol;

    @FXML
    private TableColumn<User, String> emailCol;

    @FXML
    private Label currentPageLabel;

    @FXML
    private Button btnPrevious;

    @FXML
    private Button btnNext;

    /**
     * Initializes the controller by setting up the network, fetching all users, and setting listeners for the search
     * field and results list view.
     */
    @FXML
    public void initialize() {
        // Initialize the network connection
        network = GlobalNetwork.getNetwork();
        network.addObserver(this);

        // Fetch all users from the network
        fetchAllUsers();

        // Set up the friends table
        resultsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        firstNameCol.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameCol.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        emailCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        resultsTableView.setItems(searchResults);

        // Set up listener for friend selection
        resultsTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedEmail = newValue.getEmail();
            }
        });

        // Add listeners to the search fields for dynamic filtering
        firstNameSearchField.textProperty().addListener(o -> {
            filter.setFirstName(Optional.ofNullable(firstNameSearchField.getText()));
            currentPage = 0;
            fetchAllUsers();
        });

        lastNameSearchField.textProperty().addListener(o -> {
            filter.setLastName(Optional.ofNullable(lastNameSearchField.getText()));
            currentPage = 0;
            fetchAllUsers();
        });

        emailSearchField.textProperty().addListener(o -> {
            filter.setEmail(Optional.ofNullable(emailSearchField.getText()));
            currentPage = 0;
            fetchAllUsers();
        });
    }

    /**
     * This method is called when the observed object is changed.
     *
     * @param o the observable object that is notifying the observer
     * @param arg an argument passed by the observable, providing information about the change
     */
    @Override
    public void update(Observable o, Object arg) {
        fetchAllUsers();
    }

    /**
     * Fetches all users from the network in a separate thread to avoid blocking the UI.
     */
    private void fetchAllUsers() {
        // Number of users displayed per page
        int pageSize = 5;

        // Fetch paginated users from the network
        Page<User> userPage = network.findAllUsersOnPage(new Pageable(currentPage, pageSize), filter);

        // Calculate total pages and adjust the current page if necessary
        int totalNumberOfPages = (int) Math.ceil((double) userPage.getTotalNumberOfElements() / pageSize) - 1;
        if (totalNumberOfPages <= -1) {
            totalNumberOfPages = 1;
        }
        if (currentPage > totalNumberOfPages) {
            currentPage = totalNumberOfPages;
            userPage = network.findAllUsersOnPage(new Pageable(currentPage, pageSize), filter);
        }

        // Enable/disable navigation buttons based on the current page
        btnPrevious.setDisable(currentPage == 0);
        btnNext.setDisable((currentPage + 1) * pageSize >= userPage.getTotalNumberOfElements());

        // Update the search results and UI
        List<User> users = StreamSupport.stream(userPage.getElementsOnPage().spliterator(), false).toList();
        searchResults.setAll(users);
        currentPageLabel.setText("Page " + (currentPage + 1) + " of " + (totalNumberOfPages + 1));
    }

    /**
     * Handles the action of sending a friend request to the user entered in the search field.
     */
    public void handleAddFriend() {
        // Get the current window's stage
        Stage stage = (Stage) resultsTableView.getScene().getWindow();

        // Check if a user with the entered email exists in the network
        Optional<User> receiver = network.findUserByEmail(selectedEmail);

        // Check if receiver user is same as sender user
        if (receiver.isPresent() && UserController.getUser().getEmail().equals(selectedEmail)) {
            PopupNotification.showNotification(stage, "You cannot be friend with yourself", 4000, "#ef5356");
        }

        if (receiver.isPresent()) {
            // Send a friend request to the selected user
            try {
                network.sendFriendRequest(UserController.getUser().getId(), receiver.get().getId());
                PopupNotification.showNotification(stage, "Request sent successfully", 4000, "#68c96d");
            } catch (EntityAlreadyExistsException e) {
                PopupNotification.showNotification(stage, e.getMessage(), 4000, "#ef5356");
            }

        } else {
            PopupNotification.showNotification(stage, "Please select a user", 4000, "#ef5356");
        }
    }

    /**
     * Handles the "Previous Page" button click event.
     * Fetches the previous page of users.
     */
    public void handlePreviousPage() {
        currentPage--;
        fetchAllUsers();
    }

    /**
     * Handles the "Next Page" button click event.
     * Fetches the next page of users.
     */
    public void handleNextPage() {
        currentPage++;
        fetchAllUsers();
    }
}
