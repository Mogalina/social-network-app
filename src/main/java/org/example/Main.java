package org.example;

import org.example.models.Friendship;
import org.example.models.User;
import org.example.models.validators.FriendshipValidator;
import org.example.models.validators.UserValidator;
import org.example.models.validators.Validator;
import org.example.repository.database.FriendshipDatabaseRepository;
import org.example.repository.database.UserDatabaseRepository;
import org.example.repository.Repository;
import org.example.service.*;
import org.example.ui.SocialNetworkApplication;
import org.example.utils.Config;
import org.example.repository.file.UserFileRepository;
import org.example.repository.file.FriendshipFileRepository;

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
        Repository<String, Friendship> friendshipRepository = new FriendshipDatabaseRepository(friendshipValidator);
        Service<String, Friendship> friendshipService = new FriendshipService(friendshipRepository);

        Network network = new Network(userService, friendshipService);
        Community community = new Community(network);

        SocialNetworkApplication socialNetwork = new SocialNetworkApplication(network, community);
        socialNetwork.runApplication();
    }
}