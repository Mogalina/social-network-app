package org.example.service;

import org.example.exceptions.EntityAlreadyExistsException;
import org.example.models.*;
import org.example.models.Observable;
import org.example.models.Observer;
import org.example.models.dtos.UserFilterDTO;
import org.example.repository.database.UserDatabaseRepository;
import org.example.utils.Paging.Page;
import org.example.utils.Paging.Pageable;

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

    // Service to handle and perform operations on Notification entities
    private final Service<String, Notification> notificationService;

    // Store observers to notify when an update occurs
    List<Observer> observers;

    /**
     * Constructs a new Network with the specified services..
     *
     * @param userService the service used to perform operations on {@link User} entities
     * @param friendshipService the service used to perform operations on {@link Friendship} entities
     * @param messageService the service used to perform operations on {@link Message} entities
     * @param notificationService the service used to perform operations on {@link Notification} entities
     */
    public Network(Service<String, User> userService,
                   Service<Tuple<String>, Friendship> friendshipService,
                   Service<String, Message> messageService,
                   Service<String, Notification> notificationService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.messageService = messageService;
        this.notificationService = notificationService;
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

        StreamSupport.stream(notificationService.findAll().spliterator(), false)
                .filter(notification -> Objects.equals(notification.getUserId(), uid))
                .forEach(notification -> deleteNotification(notification.getId()));

        StreamSupport.stream(messageService.findAll().spliterator(), false)
                .filter(message -> Objects.equals(message.getReceiverId(), uid) ||
                        Objects.equals(message.getSenderId(), uid))
                .forEach(message -> deleteMessage(message.getId()));

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

    public Optional<Friendship> findFriendship(String uid1, String uid2) {
        return friendshipService.findById(new Tuple<>(uid1, uid2));
    }

    /**
     * Sends a friend request from one user to another.
     *
     * @param senderId the identifier of the user sending the friend request
     * @param receiverId the identifier of the user receiving the friend request
     * @throws EntityAlreadyExistsException if friend request is already sent or friend already exists
     */
    public void sendFriendRequest(String senderId, String receiverId) throws EntityAlreadyExistsException {
        boolean alreadyExists = findFriendship(senderId, receiverId).isPresent();
        if (alreadyExists) {
            throw new EntityAlreadyExistsException("Request already sent");
        }

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
                            Friendship request = new Friendship(senderId, receiverId);
                            friendshipService.save(request);
                            notifyObservers(request);

                            Optional<User> sender = findUser(senderId);
                            if (sender.isEmpty()) {
                                throw new RuntimeException("Sender does not exist");
                            }

                            String notificationDescription = "Request from " + sender.get().getEmail();
                            Notification notification = new Notification(notificationDescription, receiverId);
                            notificationService.save(notification);
                            notifyObservers(notification);
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

        StreamSupport.stream(messageService.findAll().spliterator(), false)
                .filter(message -> (Objects.equals(message.getSenderId(), senderId) && Objects.equals(message.getReceiverId(), receiverId)) ||
                        (Objects.equals(message.getSenderId(), receiverId) && Objects.equals(message.getReceiverId(), senderId)))
                .forEach(
                        message -> {
                            messageService.deleteById(message.getId());
                            notifyObservers(message);
                        }
                );

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
     * Finds a message by its identifier.
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
        Optional<User> senderUser = findUser(message.getSenderId());
        if (senderUser.isEmpty()) {
            throw new RuntimeException("Sender does not exist");
        }

        String notificationDescription = "Message from " + senderUser.get().getEmail();
        Notification notification = new Notification(notificationDescription, message.getReceiverId());
        notificationService.save(notification);
        notifyObservers(notification);

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

    /**
     * Finds a notification by its identifier.
     *
     * @param id the identifier of the notification to find
     * @return an {@link Optional} containing the notification with the specified ID, or an empty {@code Optional} if no
     *         notification is found
     */
    public Optional<Notification> findNotification(String id) {
        return notificationService.findById(id);
    }

    /**
     * Adds a new notification to the network.
     *
     * @param notification the {@link Notification} to be added
     * @return an {@link Optional} containing the saved notification
     */
    public Optional<Notification> addNotification(Notification notification) {
        notifyObservers(notification);
        return notificationService.save(notification);
    }

    /**
     * Updates an existing notification's information.
     *
     * @param notification the notification with updated information
     * @return an {@link Optional} containing the updated notification, or an empty {@code Optional} if no notification
     *         is found
     */
    public Optional<Notification> updateNotification(Notification notification) {
        notifyObservers(notification);
        return notificationService.update(notification);
    }

    /**
     * Deletes a notification from the network.
     *
     * @param id the identifier of the notification to be deleted
     * @return an {@link Optional} containing the notification with the specified ID, or an empty {@code Optional} if no
     *         notification is found
     */
    public Optional<Notification> deleteNotification(String id) {
        Optional<Notification> deletedNotification = notificationService.deleteById(id);
        notifyObservers(deletedNotification);
        return deletedNotification;
    }

    /**
     * Returns all notifications in the network.
     *
     * @return a list of all notification
     */
    public Iterable<Notification> getAllNotifications() {
        return notificationService.findAll();
    }

    /**
     * Returns all notifications for a specficied user by its identifier.
     *
     * @param uid the unique identifier of the user
     * @return a list of notifications for the user
     */
    public Iterable<Notification> getUserNotifications(String uid) {
        return StreamSupport.stream(getAllNotifications().spliterator(), false)
                .filter(notification -> Objects.equals(notification.getUserId(), uid))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a paginated list of all users without applying any filter criteria.
     *
     * @param pageable the pagination details
     * @return a page containing all users for the specified pagination
     */
    public Page<User> findAllUsersOnPage(Pageable pageable) {
        return ((UserService) userService).findAllOnPage(pageable);
    }

    /**
     * Retrieves a paginated list of users along with the total number of matching users.
     *
     * @param pageable the pagination details
     * @param filter the filter criteria for querying users
     * @return a page containing the list of users and the total count of matching users
     */
    public Page<User> findAllUsersOnPage(Pageable pageable, UserFilterDTO filter) {
        return ((UserService) userService).findAllOnPage(pageable, filter);
    }
}
