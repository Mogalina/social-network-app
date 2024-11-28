package org.example.service;

import org.example.models.*;
import org.example.models.Observable;
import org.example.models.Observer;

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

    // Service to handle and perform operations on Message entities
    private final Service<String, Message> messageService;

    // Store observers to notify when an update occurs
    List<Observer> observers;

    /**
     * Constructs a new Network with the specified {@link UserService} and {@link FriendshipService}.
     *
     * @param userService the service used to perform operations on {@link User} entities
     * @param friendshipService the service used to perform operations on {@link Friendship} entities
     * @param messageService the service used to perform operations on {@link Message} entities
     */
    public Network(Service<String, User> userService,
                   Service<Tuple<String>, Friendship> friendshipService,
                   Service<String, Message> messageService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.messageService = messageService;
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

        Optional<User> deletedUser = userService.deleteById(uid);
        notifyObservers(deletedUser);
        return deletedUser;
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

    /**
     * Retrieves all messages in the network.
     *
     * @return an iterable collection of all {@link Message} entities.
     */
    public Iterable<Message> getAllMessages() {
        return messageService.findAll();
    }

    /**
     * Retrieves all messages from the database that were sent from a user to another.
     *
     * @param senderId the unique identifier of the sender {@link User}
     * @param receiverId the unique identifier of the receiver {@link User}
     * @return an iterable collection of all sent from a user to another
     */
    public Iterable<Message> getSentMessages(String senderId, String receiverId) {
        return StreamSupport.stream(this.getAllMessages().spliterator(), false)
                .filter(message -> Objects.equals(message.getSenderId(), senderId) &&
                        Objects.equals(message.getReceiverId(), receiverId))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all messages from the database between two users, in the order they were sent.
     *
     * @param uid1 the unique identifier of the {@link User}
     * @param uid2 the unique identifier of the {@link User}
     * @return an iterable collection of all messages between two users
     */
    public Iterable<Message> getChat(String uid1, String uid2) {
        List<Message> sentMessages = StreamSupport.stream(this.getSentMessages(uid1, uid2).spliterator(), false)
                .collect(Collectors.toList());
        List<Message> recvMessages = StreamSupport.stream(this.getSentMessages(uid2, uid1).spliterator(), false)
                .toList();

        sentMessages.addAll(recvMessages);
        sentMessages.sort(Comparator.comparing(Message::getDate));
        return sentMessages;
    }

    /**
     * Finds a message by their identifier.
     *
     * @param id the identifier of the message to find
     * @return an {@link Optional} containing the message with the specified ID, or an empty {@code Optional} if no
     *         message is found
     */
    public Optional<Message> findMessage(String id) {
        return messageService.findById(id);
    }

    /**
     * Adds a new message to the network.
     *
     * @param message the {@link Message} to be added
     * @return an {@link Optional} containing the saved message
     */
    public Optional<Message> addMessage(Message message) {
        notifyObservers(message);
        return messageService.save(message);
    }

    /**
     * Updates an existing message's information.
     *
     * @param message the message with updated information
     * @return an {@link Optional} containing the updated message, or an empty {@code Optional} if no message is found
     */
    public Optional<Message> updateMessage(Message message) {
        notifyObservers(message);
        return messageService.update(message);
    }

    /**
     * Deletes a message from the network.
     *
     * @param id the identifier of the message to be deleted
     * @return an {@link Optional} containing the message with the specified ID, or an empty {@code Optional} if no
     *         message is found
     */
    public Optional<Message> deleteMessage(String id) {
        Optional<Message> deletedMessage = messageService.deleteById(id);
        notifyObservers(deletedMessage);
        return deletedMessage;
    }
}
