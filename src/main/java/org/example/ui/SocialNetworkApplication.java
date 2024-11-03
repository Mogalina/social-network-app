package org.example.ui;

import org.example.models.Friendship;
import org.example.models.User;
import org.example.service.Community;
import org.example.service.Network;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * SocialNetworkApplication class provides the user interface for the Social Network application.
 */
public class SocialNetworkApplication {

    // Network to manage internal data
    private final Network network;

    // Community to manage interactions between users in the network
    private final Community community;

    // Maps command strings to their corresponding methods
    private final Map<String, Consumer<List<String>>> commands;

    /**
     * Constructs a new SocialNetworkApplication with the specified {@link Network} and {@link Community}.
     *
     * @param network the network used to manage internal data
     * @param community the community used to manage interactions between users in the network
     */
    public SocialNetworkApplication(Network network, Community community) {
        this.network = network;
        this.community = community;

        // Initialize command mappings to their corresponding methods
        commands = new HashMap<>();
        commands.put("docs", this::displayDocumentation);
        commands.put("exit", this::exitApplication);
        commands.put("users", this::displayUsers);
        commands.put("add_user", this::addUser);
        commands.put("del_user", this::deleteUser);
        commands.put("update_user", this::updateUser);
        commands.put("user", this::findUser);
        commands.put("friends", this::displayFriendsOfUser);
        commands.put("send_req", this::sendFriendRequest);
        commands.put("rels", this::displayFriendships);
        commands.put("del_req", this::deleteFriendRequest);
        commands.put("groups", this::displayCommunityGroups);
        commands.put("fire_group", this::displayMostSocialCommunity);
        commands.put("sent_reqs", this::displaySentRequestsOfUser);
        commands.put("recv_reqs", this::displayReceivedRequestsOfUser);
    }

    private void displayApplicationIntro() {
        System.out.println("""
                -----------------------------------------------------------------------------------------------------------------------------------
                |                                                                                                                                 |
                |                                     ███████╗ ██████╗  ██████╗ ██╗ █████╗ ██╗     ███╗   ██╗                                     |
                |                                     ██╔════╝██╔═══██╗██╔════╝ ██║██╔══██╗██║     ████╗  ██║                                     |
                |                                     ███████╗██║   ██║██║      ██║███████║██║     ██╔██╗ ██║                                     |
                |                                     ╚════██║██║   ██║██║      ██║██╔══██║██║     ██║╚██╗██║                                     |
                |                                     ███████║╚██████╔╝╚██████╗ ██║██║  ██║███████╗██║ ╚████║                                     |
                |                                     ╚══════╝ ╚═════╝  ╚═════╝ ╚═╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═══╝                                     |
                |                                                                                                                                 |
                |                                            Welcome to your Social Network Platform!                                             |
                |                                           © 2024 Moghioros Eric. All rights reserved.                                           |
                |                                                                                                                                 |
                -----------------------------------------------------------------------------------------------------------------------------------""");
    }

    private void displayDocumentation(List<String> params) {
        if (!params.isEmpty()) {
            System.out.println("[ERROR] Invalid number of parameters. Check documentation (command: 'docs') for more " +
                    "information.");
            return;
        }

        System.out.println("+---------------------+----------------------------------------------------+------------------------------------------------------+");
        System.out.printf("| %-19s | %-50s | %-52s |%n", "COMMAND", "PARAMETERS", "DESCRIPTION");
        System.out.println("+---------------------+----------------------------------------------------+------------------------------------------------------+");
        System.out.printf("| %-19s | %-50s | %-52s |%n", "docs", "-", "Display information about application usage");
        System.out.printf("| %-19s | %-50s | %-52s |%n", "exit", "-", "Exit the application");
        System.out.printf("| %-19s | %-50s | %-52s |%n", "users", "-", "Display available users in network");
        System.out.printf("| %-19s | %-50s | %-52s |%n", "add_user", "<FIRST_NAME> <LAST_NAME> <EMAIL> <PASSWORD>", "Add new user to network");
        System.out.printf("| %-19s | %-50s | %-52s |%n", "update_user", "<ID> <FIRST_NAME> <LAST_NAME> <EMAIL> <PASSWORD>", "Updates user from network");
        System.out.printf("| %-19s | %-50s | %-52s |%n", "del_user", "<ID>", "Delete user from network");
        System.out.printf("| %-19s | %-50s | %-52s |%n", "user", "<ID>", "Display user information from network");
        System.out.printf("| %-19s | %-50s | %-52s |%n", "friends", "<ID>", "Display friends of specific user");
        System.out.printf("| %-19s | %-50s | %-52s |%n", "send_req", "<SENDER_ID> <RECEIVER_ID>", "Send friend request to user");
        System.out.printf("| %-19s | %-50s | %-52s |%n", "rels", "-", "Display available friendship relations between users");
        System.out.printf("| %-19s | %-50s | %-52s |%n", "del_req", "<SENDER_ID> <RECEIVER_ID>", "Delete friendship relation or request between users");
        System.out.printf("| %-19s | %-50s | %-52s |%n", "groups", "-", "Gets the friends communities in network");
        System.out.printf("| %-19s | %-50s | %-52s |%n", "fire_group", "-", "Gets the most social community of friends in network");
        System.out.printf("| %-19s | %-50s | %-52s |%n", "sent_reqs", "<ID>", "Gets the users requested by specified user");
        System.out.printf("| %-19s | %-50s | %-52s |%n", "recv_reqs", "<ID>", "Gets the users that requested specified user");
        System.out.println("+---------------------+----------------------------------------------------+------------------------------------------------------+");
    }

    private void exitApplication(List<String> params) {
        if (!params.isEmpty()) {
            System.out.println("[ERROR] Invalid number of parameters. Check documentation (command: 'docs') for more " +
                    "information about application usage.");
            return;
        }

        System.out.println("Closing Social Network Application...");
        System.exit(0);
    }

    private void displaySentRequestsOfUser(List<String> params) {
        if (params.size() != 1) {
            System.out.println("[ERROR] Invalid number of parameters. Check documentation (command: 'docs') for more " +
                    "information about application usage.");
            return;
        }

        String uid = params.getFirst();

        try {
            List<User> users = (List<User>) network.getSentRequestsOfUser(uid);
            String result = users.stream()
                    .map(User::toString)
                    .collect(Collectors.joining("\n\n"));
            if (result.isEmpty()) {
                System.out.println("[INFO] No sent friend requests found for user");
            } else {
                System.out.println(result);
            }
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private void displayReceivedRequestsOfUser(List<String> params) {
        if (params.size() != 1) {
            System.out.println("[ERROR] Invalid number of parameters. Check documentation (command: 'docs') for more " +
                    "information about application usage.");
            return;
        }

        String uid = params.getFirst();

        try {
            List<User> users = (List<User>) network.getReceivedRequestsOfUser(uid);
            String result = users.stream()
                    .map(User::toString)
                    .collect(Collectors.joining("\n\n"));
            if (result.isEmpty()) {
                System.out.println("[INFO] No received friend requests found for user");
            } else {
                System.out.println(result);
            }
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private void displayMostSocialCommunity(List<String> params) {
        if (!params.isEmpty()) {
            System.out.println("[ERROR] Invalid number of parameters. Check documentation (command: 'docs') for more " +
                    "information about application usage.");
            return;
        }

        try {
            List<User> communityUsers = (List<User>) community.getMostSocialCommunity();
            String result = communityUsers.stream()
                    .map(User::toString)
                    .collect(Collectors.joining("\n\n"));
            if (result.isEmpty()) {
                System.out.println("[INFO] No communities of users found in network");
            } else {
                System.out.println(result);
            }
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private void displayCommunityGroups(List<String> params) {
        if (!params.isEmpty()) {
            System.out.println("[ERROR] Invalid number of parameters. Check documentation (command: 'docs') for more " +
                    "information about application usage.");
            return;
        }

        try {
            int communitiesCount = community.countCommunities();
            System.out.println("Number of user communities in network -> " + communitiesCount);
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private void deleteFriendRequest(List<String> params) {
        if (params.size() != 2) {
            System.out.println("[ERROR] Invalid number of parameters. Check documentation (command: 'docs') for more " +
                    "information about application usage.");
            return;
        }

        String senderId = params.get(0);
        String receiverId = params.get(1);

        try {
            network.deleteFriendRequest(senderId, receiverId);
            System.out.println("[INFO] Friendship request successfully deleted");
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private void sendFriendRequest(List<String> params) {
        if (params.size() != 2) {
            System.out.println("[ERROR] Invalid number of parameters. Check documentation (command: 'docs') for more " +
                    "information about application usage.");
            return;
        }

        String senderId = params.get(0);
        String receiverId = params.get(1);

        try {
            network.sendFriendRequest(senderId, receiverId);
            System.out.println("[INFO] Friend request sent successfully");
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private void displayFriendsOfUser(List<String> params) {
        if (params.size() != 1) {
            System.out.println("[ERROR] Invalid number of parameters. Check documentation (command: 'docs') for more " +
                    "information about application usage.");
            return;
        }

        String uid = params.getFirst();

        try {
            String result = StreamSupport.stream(network.getFriendsOfUser(uid).spliterator(), false)
                    .map(User::toString)
                    .collect(Collectors.joining("\n\n"));
            if (result.isEmpty()) {
                System.out.println("[INFO] User does not have any friends");
            } else {
                System.out.println(result);
            }
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private void findUser(List<String> params) {
        if (params.size() != 1) {
            System.out.println("[ERROR] Invalid number of parameters. Check documentation (command: 'docs') for more " +
                    "information about application usage.");
            return;
        }

        String uid = params.getFirst();

        try {
            Optional<User> user = network.findUser(uid);
            if (user.isPresent()) {
                System.out.println(user.get());
            } else {
                System.out.println("[ERROR] User not found");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private void addUser(List<String> params) {
        if (params.size() != 4) {
            System.out.println("[ERROR] Invalid number of parameters. Check documentation (command: 'docs') for more " +
                    "information about application usage.");
            return;
        }

        String firstName = params.get(0);
        String lastName = params.get(1);
        String email = params.get(2);
        String password = params.get(3);

        try {
            User user = new User(firstName, lastName, password, email);
            Optional<User> addedUser = network.addUser(user);
            if (addedUser.isPresent()) {
                System.out.println("[INFO] User added successfully");
            } else {
                System.out.println("[ERROR] User already exists");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private void updateUser(List<String> params) {
        if (params.size() != 5) {
            System.out.println("[ERROR] Invalid number of parameters. Check documentation (command: 'docs') for more " +
                    "information about application usage.");
            return;
        }

        String id = params.get(0);
        String firstName = params.get(1);
        String lastName = params.get(2);
        String email = params.get(3);
        String password = params.get(4);

        try {
            User user = new User(firstName, lastName, password, email);
            user.setId(id);
            Optional<User> updatedUser = network.updateUser(user);
            if (updatedUser.isPresent()) {
                System.out.println("[INFO] User updated successfully");
            } else {
                System.out.println("[ERROR] User not found");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private void deleteUser(List<String> params) {
        if (params.size() != 1) {
            System.out.println("[ERROR] Invalid number of parameters. Check documentation (command: 'docs') for more " +
                    "information about application usage.");
            return;
        }

        String uid = params.getFirst();

        try {
            Optional<User> user = network.deleteUser(uid);
            if (user.isPresent()) {
                System.out.println("[INFO] User deleted successfully");
            } else {
                System.out.println("[ERROR] User not found");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private void displayFriendships(List<String> params) {
        if (!params.isEmpty()) {
            System.out.println("[ERROR] Invalid number of parameters. Check documentation (command: 'docs') for more " +
                    "information about application usage.");
            return;
        }

        String result = StreamSupport.stream(network.getAllFriendships().spliterator(), false)
                .map(Friendship::toString)
                .collect(Collectors.joining("\n\n"));
        if (result.isEmpty()) {
            System.out.println("[INFO] There are no friendship relations between users");
        } else {
            System.out.println(result);
        }
    }

    private void displayUsers(List<String> params) {
        if (!params.isEmpty()) {
            System.out.println("[ERROR] Invalid number of parameters. Check documentation (command: 'docs') for more " +
                    "information about application usage.");
            return;
        }

        String result = StreamSupport.stream(network.getAllUsers().spliterator(), false)
                .map(User::toString)
                .collect(Collectors.joining("\n\n"));
        if (result.isEmpty()) {
            System.out.println("[INFO] No users found");
        } else {
            System.out.println(result);
        }
    }

    /**
     * Starts the user interface for the application.
     * Displays the application intro and continuously listens for user commands.
     */
    public void runApplication() {
        displayApplicationIntro();
        System.out.println();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print(">>> ");
            String input = scanner.nextLine();

            List<String> tokens = Arrays.asList(input.split(" "));
            String command = tokens.getFirst();
            List<String> params = tokens.subList(1, tokens.size());

            if (!commands.containsKey(command)) {
                System.out.println("[ERROR] Invalid command. Check documentation (command: 'docs') for " +
                        "more information about application usage.");
            } else {
                try {
                    commands.get(command).accept(params);
                } catch (Exception e) {
                    System.out.println("[ERROR] " + e.getMessage());
                }
            }

            System.out.println();
        }
    }
}
