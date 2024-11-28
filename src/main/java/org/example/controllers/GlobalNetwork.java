package org.example.controllers;

import org.example.models.*;
import org.example.models.validators.*;
import org.example.repository.Repository;
import org.example.repository.database.FriendshipDatabaseRepository;
import org.example.repository.database.MessageDatabaseRepository;
import org.example.repository.database.NotificationDatabaseRepository;
import org.example.repository.database.UserDatabaseRepository;
import org.example.service.*;

/**
 * A singleton-like class to manage a shared {@link Network} instance.
 * This class lazily initializes the `Network` and ensures only one instance exists globally.
 */
public class GlobalNetwork {

    // Static field to hold the single instance of {@link Network}`
    private static Network network = null;

    /**
     * Provides access to the singleton {@link Network} instance.
     * If the instance does not exist, it initializes it with the necessary components.
     *
     * @return the singleton {@link Network} instance
     */
    public static Network getNetwork() {
        if (network == null) {
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

            network = new Network(userService, friendshipService, messageService, notificationService);
        }

        return network;
    }
}
