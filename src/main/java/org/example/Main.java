package org.example;

import org.example.models.*;
import org.example.models.validators.*;
import org.example.repository.database.FriendshipDatabaseRepository;
import org.example.repository.database.MessageDatabaseRepository;
import org.example.repository.database.NotificationDatabaseRepository;
import org.example.repository.database.UserDatabaseRepository;
import org.example.repository.Repository;
import org.example.service.*;
import org.example.ui.SocialNetworkApplication;

import java.io.IOException;

/**
 * The Main class serves as the entry point for the Social Network application.
 */
public class Main {

    /**
     * The main method where the application starts.
     *
     * @param args command line arguments (not used in this application)
     * @throws IOException if there are issues performing reading/writing operations
     */
    public static void main(String[] args) throws IOException {
        Validator<User> userValidator = new UserValidator();
        Repository<String, User> userRepository = new UserDatabaseRepository(userValidator);
        Service<String, User> userService = new UserService(userRepository);

        Validator<Friendship> friendshipValidator = new FriendshipValidator(userRepository);
        Repository<Tuple<String>, Friendship> friendshipRepository = new FriendshipDatabaseRepository(friendshipValidator);
        Service<Tuple<String>, Friendship> friendshipService = new FriendshipService(friendshipRepository);

        Validator<Message> messageValidator = new MessageValidator(userRepository);
        Repository<String, Message> messageRepository = new MessageDatabaseRepository(messageValidator);
        Service<String, Message> messageService = new MessageService(messageRepository);

        Validator<Notification> notificationValidator = new NotificationValidator(userRepository);
        Repository<String, Notification> notificationRepository = new NotificationDatabaseRepository(notificationValidator);
        Service<String, Notification> notificationService = new NotificationService(notificationRepository);

        Network network = new Network(userService, friendshipService, messageService, notificationService);
        Community community = new Community(network);

        SocialNetworkApplication socialNetwork = new SocialNetworkApplication(network, community);
        socialNetwork.runApplication();
    }
}