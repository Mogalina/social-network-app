package org.example.service;

import org.example.models.Observable;
import org.example.models.Friendship;
import org.example.models.Observer;
import org.example.models.Tuple;
import org.example.models.User;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Network class that manages users relationships and friendships within a social network.
 */
public class Network implements Observable {

    // Service to handle and perform operations on User entities
    private final Service<String, User> userService;

    // Service to handle and perform operations on Friendship entities
    private final Service<Tuple<String>, Friendship> friendshipService;

    // Store observers to notify when an update occurs
    List<Observer> observers;

    /**
     * Constructs a new Network with the specified {@link UserService} and {@link FriendshipService}.
     *
     * @param userService the service used to perform operations on {@link User} entities
     * @param friendshipService the service used to perform operations on {@link Friendship} entities
     */
    public Network(Service<String, User> userService, Service<Tuple<String>, Friendship> friendshipService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        observers = new ArrayList<>();
    }

    /**
     * Adds an observer to the list of observers.
     * This observer will be notified when the observable's state changes.
     *
     * @param observer the observer to be added
     */
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from the list of observers.
     * This observer will no longer be notified of any state changes in the observable.
     *
     * @param observer the observer to be removed
     */
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all registered observers that the observable's state has changed.
     * This method will trigger the {@code update} method on each observer.
     *
     * @param arg an argument passed by the observable, providing information about the change
     */
    @Override
    public void notifyObservers(Object arg) {
        observers.forEach(o -> o.update(this, arg));
    }

    /**
     * Finds a user by their identifier.
     *
     * @param id the identifier of the user to find
     * @return an {@link Optional} containing the user with the specified ID, or an empty {@code Optional} if no user is
     *         found
     */
    public Optional<User> findUser(String id) {
        return userService.findById(id);
    }

    /**
     * Adds a new user to the network.
     *
     * @param user the {@link User} to be added
     * @return an {@link Optional} containing the saved user, or an empty {@code Optional} if user already exists in the
     *         system
     */
    public Optional<User> addUser(User user) {
        notifyObservers(user);
        return userService.save(user);
    }

    /**
     * Deletes a user from the network and all associated friendships.
     *
     * @param uid the identifier of the user to be deleted
     * @return an {@link Optional} containing the user with the specified ID, or an empty {@code Optional} if no user is
     *         found
     */
    public Optional<User> deleteUser(String uid) {
        StreamSupport.stream(friendshipService.findAll().spliterator(), false)
                .filter(friendship -> friendship.containsUser(uid))
                .forEach(friendship -> friendshipService.deleteById(friendship.getId()));

        notifyObservers(uid);
        return userService.deleteById(uid);
    }

    /**
     * Updates an existing user's information.
     *
     * @param user the user with updated information
     * @return an {@link Optional} containing the updated user, or an empty {@code Optional} if no user is found
     */
    public Optional<User> updateUser(User user) {
        notifyObservers(user);
        return userService.update(user);
    }

    /**
     * Retrieves all users in the network.
     *
     * @return an iterable collection of all {@link User} entities.
     */
    public Iterable<User> getAllUsers() {
        return userService.findAll();
    }

    /**
     * Retrieves all friendships in the network.
     *
     * @return an iterable collection of all {@link Friendship} entities.
     */
    public Iterable<Friendship> getAllFriendships() {
        return friendshipService.findAll();
    }

    /**
     * Gets all friends of a specific user.
     *
     * @param uid the identifier of the user whose friends to retrieve
     * @return an iterable collection of {@link User} objects representing the user's friends
     */
    public Iterable<User> getFriendsOfUser(String uid) {
        return StreamSupport.stream(friendshipService.findAll().spliterator(), false)
                .filter(friendship -> friendship.containsUser(uid) && !friendship.isPending())
                .map(friendship -> friendship.getFriendIdOfUser(uid))
                .map(userService::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Gets all friend requests sent by a specific user.
     *
     * @param uid the identifier of the user whose sent requests to retrieve
     * @return an iterable collection of {@link User} objects representing the users that the specified user sent
     *         requests to
     */
    public Iterable<User> getSentRequestsOfUser(String uid) {
        return StreamSupport.stream(friendshipService.findAll().spliterator(), false)
                .filter(friendship -> Objects.equals(friendship.getSenderId(), uid) && friendship.isPending())
                .map(friendship -> friendship.getFriendIdOfUser(uid))
                .map(userService::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    /**
     * Gets all friend requests received by a specific user.
     *
     * @param uid the identifier of the user whose received requests to retrieve
     * @return an iterable collection of {@link User} objects representing the users that sent requests to the specified
     *         user
     */
    public Iterable<User> getReceivedRequestsOfUser(String uid) {
        return StreamSupport.stream(friendshipService.findAll().spliterator(), false)
                .filter(friendship -> Objects.equals(friendship.getReceiverId(), uid) && friendship.isPending())
                .map(friendship -> friendship.getFriendIdOfUser(uid))
                .map(userService::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    /**
     * Creates a {@link Friendship} between two users.
     *
     * @param uid1 the identifier of the first user
     * @param uid2 the identifier of the second user
     */
    public void makeFriendship(String uid1, String uid2) {
        Friendship senderToReceiver = new Friendship(uid1, uid2);
        senderToReceiver.setPending(false);
        friendshipService.save(senderToReceiver);
    }

    /**
     * Sends a friend request from one user to another.
     *
     * @param senderId the identifier of the user sending the friend request
     * @param receiverId the identifier of the user receiving the friend request
     */
    public void sendFriendRequest(String senderId, String receiverId) {
        StreamSupport.stream(friendshipService.findAll().spliterator(), false)
                .filter(friendship -> friendship.containsUser(senderId) &&
                        friendship.containsUser(receiverId) &&
                        friendship.isPending())
                .findFirst()
                .ifPresentOrElse(
                        pendingFriendship -> {
                            friendshipService.deleteById(pendingFriendship.getId());
                            makeFriendship(senderId, receiverId);
                        },
                        () -> {
                            Friendship friendRequest = new Friendship(senderId, receiverId);
                            friendshipService.save(friendRequest);
                            notifyObservers(friendRequest);
                        }
                );
    }

    /**
     * Deletes a friend request between two users.
     *
     * @param senderId the identifier of the user who sent the request
     * @param receiverId the identifier of the user who received the request
     */
    public void deleteFriendRequest(String senderId, String receiverId) {
        StreamSupport.stream(friendshipService.findAll().spliterator(), false)
                .filter(friendship -> friendship.containsUser(senderId) &&
                        friendship.containsUser(receiverId))
                .forEach(friendship -> {
                    friendshipService.deleteById(friendship.getId());
                    notifyObservers(friendship);
                });
    }

    /**
     * Checks if a friendship exists between two users.
     *
     * @param senderId the identifier of the first user
     * @param receiverId the identifier of the second user
     * @return {@code true} if a friendship exists and is not pending, otherwise {@code false}
     */
    public boolean isFriendship(String senderId, String receiverId) {
        return StreamSupport.stream(friendshipService.findAll().spliterator(), false)
                .anyMatch(friendship -> friendship.containsUser(senderId) &&
                        friendship.containsUser(receiverId) &&
                        !friendship.isPending());
    }

    /**
     * Retrieves a map of user relations, where each user identifier maps to a list of their friends' identifiers.
     *
     * @return a map of user identifiers to their respective lists of friend identifiers
     */
    public Map<String, List<String>> getRelations() {
        return StreamSupport.stream(getAllUsers().spliterator(), false)
                .collect(HashMap::new, (map, user) -> map.put(
                        user.getId(),
                        StreamSupport.stream(getFriendsOfUser(user.getId()).spliterator(), false)
                                .map(User::getId)
                                .toList()
                ), HashMap::putAll);
    }

    /**
     * Finds a user by their email address.
     *
     * @param email the email address of the user to find
     * @return an {@link Optional} containing the user with the specified email address, or an empty {@code Optional} if
     *         no user is found
     */
    public Optional<User> findUserByEmail(String email) {
        return StreamSupport.stream(getAllUsers().spliterator(), false)
                .filter(u -> Objects.equals(u.getEmail(), email))
                .findFirst();
    }
}
