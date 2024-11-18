package org.example.controllers;

import org.example.models.Friendship;
import org.example.models.Tuple;
import org.example.models.User;
import org.example.models.validators.FriendshipValidator;
import org.example.models.validators.UserValidator;
import org.example.models.validators.Validator;
import org.example.repository.Repository;
import org.example.repository.database.FriendshipDatabaseRepository;
import org.example.repository.database.UserDatabaseRepository;
import org.example.service.FriendshipService;
import org.example.service.Network;
import org.example.service.Service;
import org.example.service.UserService;

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

            network = new Network(userService, friendshipService);
        }

        return network;
    }
}
