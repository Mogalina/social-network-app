package org.example.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a Friendship entity in the system network with a unique ID, sender ID, receiver ID, request date,
 * and a request pending state.
 *
 * The {@code Friendship} extends the {@code Entity} base class, using {@code Tuple<String>} as the type of its
 * identifier.
 */
public class Friendship extends Entity<Tuple<String>> {

    private final StringProperty uid1;
    private final StringProperty uid2;
    private final ObjectProperty<LocalDateTime> date;
    private final BooleanProperty pending;

    /**
     * Constructs a new {@code Friendship} with the specified sender ID, receiver ID, and request date.
     * The request is marked as pending by default.
     *
     * @param uid1 the identifier of the sender user
     * @param uid2 the identifier of the receiver user
     */
    public Friendship(String uid1, String uid2) {
        setId(new Tuple<>(uid1, uid2));
        this.uid1 = new SimpleStringProperty(uid1);
        this.uid2 = new SimpleStringProperty(uid2);
        this.date = new SimpleObjectProperty<>(LocalDateTime.now());
        this.pending = new SimpleBooleanProperty(true);
    }

    /**
     * Returns the sender's ID property.
     *
     * @return the sender ID property
     */
    public StringProperty senderIdProperty() {
        return uid1;
    }

    /**
     * Returns the sender's ID.
     *
     * @return the sender ID
     */
    public String getSenderId() {
        return uid1.get();
    }

    /**
     * Sets the sender's ID.
     *
     * @param senderId the new sender ID
     */
    public void setSenderId(String senderId) {
        this.uid1.set(senderId);
    }

    /**
     * Returns the receiver's ID property.
     *
     * @return the receiver ID property
     */
    public StringProperty receiverIdProperty() {
        return uid2;
    }

    /**
     * Returns the receiver's ID.
     *
     * @return the receiver ID
     */
    public String getReceiverId() {
        return uid2.get();
    }

    /**
     * Sets the receiver's ID.
     *
     * @param receiverId the new receiver ID
     */
    public void setReceiverId(String receiverId) {
        this.uid2.set(receiverId);
    }

    /**
     * Returns the request date property.
     *
     * @return the date property
     */
    public ObjectProperty<LocalDateTime> dateProperty() {
        return date;
    }

    /**
     * Returns the request date of the friendship.
     *
     * @return the request date
     */
    public LocalDateTime getDate() {
        return date.get();
    }

    /**
     * Sets the request date of the friendship.
     *
     * @param date the new request date
     */
    public void setDate(LocalDateTime date) {
        this.date.set(date);
    }

    /**
     * Returns the pending state property.
     *
     * @return the pending state property
     */
    public BooleanProperty pendingProperty() {
        return pending;
    }

    /**
     * Returns whether the friendship request is pending.
     *
     * @return {@code true} if the request is pending, {@code false} otherwise
     */
    public boolean isPending() {
        return pending.get();
    }

    /**
     * Sets the pending state of the friendship request.
     *
     * @param pending the new pending state
     */
    public void setPending(boolean pending) {
        this.pending.set(pending);
    }

    /**
     * Returns the identifiers of the users involved in the friendship request.
     *
     * @return an array containing the sender ID and receiver ID
     */
    public String[] getUsers() {
        return new String[]{uid1.get(), uid2.get()};
    }

    /**
     * Checks if the provided user identifier is involved in the friendship request.
     *
     * @param uid the user identifier to check
     * @return {@code true} if the user is involved, {@code false} otherwise
     */
    public boolean containsUser(String uid) {
        return uid1.get().equals(uid) || uid2.get().equals(uid);
    }

    /**
     * Returns the identifier of the user's friend in the friendship request.
     *
     * @param uid the identifier of the user
     * @return the friend's identifier, or {@code null} if the user is not part of the friendship
     */
    public String getFriendIdOfUser(String uid) {
        return Objects.equals(uid, uid1.get()) ? uid2.get() : Objects.equals(uid, uid2.get()) ? uid1.get() : null;
    }

    /**
     * Returns a string representation of the friendship.
     *
     * @return a string containing the sender ID, receiver ID, request date, and pending state
     */
    @Override
    public String toString() {
        return "@FRIENDSHIP | " +
                "USER <" + uid1.get() + ">" +
                "\n            | USER <" + uid2.get() + ">" +
                "\n            | DATE <" + date.get() + ">" +
                "\n            | PENDING <" + pending.get() + ">";
    }

    /**
     * Compares this friendship with another object for equality.
     *
     * @param o the object to compare with
     * @return {@code true} if the two friendships are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Friendship that = (Friendship) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(uid1.get(), that.uid1.get()) &&
                Objects.equals(uid2.get(), that.uid2.get()) &&
                Objects.equals(date.get(), that.date.get()) &&
                Objects.equals(pending.get(), that.pending.get());
    }

    /**
     * Returns the hash code of this friendship.
     *
     * @return the hash code based on the sender ID, receiver ID, request date, and pending state
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), uid1.get(), uid2.get(), date.get(), pending.get());
    }
}
