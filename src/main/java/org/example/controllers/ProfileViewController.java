package org.example.controllers;

import org.example.models.*;
import org.example.service.Network;
import org.example.utils.PopupNotification;
import org.example.utils.SceneUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class ProfileViewController {

    private ObservableList<User> friendsData = FXCollections.observableArrayList();

    private ObservableList<User> sentRequestsData = FXCollections.observableArrayList();

    private ObservableList<User> receivedResuestsData = FXCollections.observableArrayList();

    private Network network;

    private User user;

    private String selectedExistingFriendEmail = null;

    private String selectedExistingReceivedRequestEmail = null;

    private String selectedExistingSentRequestEmail = null;

    @FXML
    private Label profileName;

    @FXML
    private Label profileEmail;

    @FXML
    private TableView<User> friendsTable;

    @FXML
    private TableView<User> sentRequestsTable;

    @FXML
    private TableView<User> receivedRequestsTable;

    @FXML
    private TableColumn<User, String> firstNameColumn;

    @FXML
    private TableColumn<User, String> lastNameColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableColumn<User, String> requestesFirstNameColumn;

    @FXML
    private TableColumn<User, String> requestesLastNameColumn;

    @FXML
    private TableColumn<User, String> requestesEmailColumn;

    @FXML
    private TableColumn<User, String> requestedFirstNameColumn;

    @FXML
    private TableColumn<User, String> requestedLastNameColumn;

    @FXML
    private TableColumn<User, String> requestedEmailColumn;

    public void initialize() {
        network = GlobalNetwork.getNetwork();

        user = UserController.getUser();
        profileName.setText(user.getFirstName() + " " + user.getLastName());
        profileEmail.setText(user.getEmail());

        friendsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        friendsData = setFriendsData();
        friendsTable.setItems(friendsData);

        friendsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedExistingFriendEmail = newValue.getEmail();
            }
        });

        sentRequestsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        requestesFirstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        requestesLastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        requestesEmailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        sentRequestsData = setSentRequestsData();
        sentRequestsTable.setItems(sentRequestsData);

        sentRequestsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedExistingSentRequestEmail = newValue.getEmail();
            }
        });

        receivedRequestsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        requestedFirstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        requestedLastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        requestedEmailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        receivedResuestsData = setReceivedRequestsData();
        receivedRequestsTable.setItems(receivedResuestsData);

        receivedRequestsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedExistingReceivedRequestEmail = newValue.getEmail();
            }
        });
    }

    private ObservableList<User> setFriendsData() {
        List<User> friendsOfUser = (List<User>) network.getFriendsOfUser(user.getId());
        return FXCollections.observableArrayList(friendsOfUser);
    }

    private ObservableList<User> setSentRequestsData() {
        List<User> sentRequestsOfUserOfUser = (List<User>) network.getSentRequestsOfUser(user.getId());
        return FXCollections.observableArrayList(sentRequestsOfUserOfUser);
    }

    private ObservableList<User> setReceivedRequestsData() {
        List<User> receivedRequestsOfUser = (List<User>) network.getReceivedRequestsOfUser(user.getId());
        return FXCollections.observableArrayList(receivedRequestsOfUser);
    }

    public void addNewFriend() {
        Stage stage = (Stage) profileName.getScene().getWindow();
        SceneUtils.switchScene(stage, "/visuals/views/add-friend-view.fxml");
    }

    public void handleUserLogOut() {
        Stage stage = (Stage) profileName.getScene().getWindow();
        SceneUtils.switchScene(stage, "/visuals/views/login-view.fxml");
    }

    public void removeExistingFriend() {
        Stage stage = (Stage) profileName.getScene().getWindow();

        if (selectedExistingFriendEmail == null) {
            PopupNotification.showNotification(stage, "Please select a friend", 4000, "#ef5356");
        } else {
            Optional<User> selectedUser = network.findUserByEmail(selectedExistingFriendEmail);
            network.deleteFriendRequest(UserController.getUser().getId(), selectedUser.get().getId());
            PopupNotification.showNotification(stage, "Friend removed successfully", 4000, "#68c96d");
            selectedExistingFriendEmail = null;
        }
    }

    public void acceptFriendRequest() {
        Stage stage = (Stage) profileName.getScene().getWindow();

        if (selectedExistingReceivedRequestEmail == null) {
            PopupNotification.showNotification(stage, "Please select a request", 4000, "#ef5356");
        } else {
            Optional<User> selectedUser = network.findUserByEmail(selectedExistingReceivedRequestEmail);
            network.sendFriendRequest(UserController.getUser().getId(), selectedUser.get().getId());
            PopupNotification.showNotification(stage, "Request accepted successfully", 4000, "#68c96d");
            selectedExistingReceivedRequestEmail = null;
        }
    }

    public void declineFriendRequest() {
        Stage stage = (Stage) profileName.getScene().getWindow();

        if (selectedExistingReceivedRequestEmail == null) {
            PopupNotification.showNotification(stage, "Please select a request", 4000, "#ef5356");
        } else {
            Optional<User> selectedUser = network.findUserByEmail(selectedExistingReceivedRequestEmail);
            network.deleteFriendRequest(selectedUser.get().getId(), UserController.getUser().getId());
            PopupNotification.showNotification(stage, "Request declined successfully", 4000, "#68c96d");
            selectedExistingReceivedRequestEmail = null;
        }
    }

    public void unsendFriendRequest() {
        Stage stage = (Stage) profileName.getScene().getWindow();

        if (selectedExistingSentRequestEmail == null) {
            PopupNotification.showNotification(stage, "Please select a request", 4000, "#ef5356");
        } else {
            Optional<User> selectedUser = network.findUserByEmail(selectedExistingSentRequestEmail);
            network.deleteFriendRequest(UserController.getUser().getId(), selectedUser.get().getId());
            PopupNotification.showNotification(stage, "Request unsent successfully", 4000, "#68c96d");
            selectedExistingSentRequestEmail = null;
        }
    }
}
