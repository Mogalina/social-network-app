package org.example.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a {@code Message} entity exchanged between two users.
 *
 * The {@code User} extends the {@code Entity} base class, using {@code String} as the type of its identifier.
 */
public class Message extends Entity<String> {

    private final StringProperty senderId;
    private final StringProperty receiverId;
    private final StringProperty message;
    private final ObjectProperty<LocalDateTime> date;

    /**
     * Constructs a new {@link Message} object with the specified sender ID, receiver ID, and message content.
     * The timestamp is automatically set to the current date and time.
     *
     * @param senderId the ID of the user sending the message
     * @param receiverId the ID of the user receiving the message
     * @param message the content of the message
     */
    public Message(String senderId, String receiverId, String message) {
        setId(UUID.randomUUID().toString());
        this.senderId = new SimpleStringProperty(senderId);
        this.receiverId = new SimpleStringProperty(receiverId);
        this.message = new SimpleStringProperty(message);
        this.date = new SimpleObjectProperty<>(LocalDateTime.now());
    }

    /**
     * Returns the property object for the sender's ID.
     *
     * @return the sender ID property
     */
    public StringProperty senderIdProperty() {
        return senderId;
    }

    /**
     * Returns the sender's ID.
     *
     * @return the sender ID as a String
     */
    public String getSenderId() {
        return senderId.get();
    }

    /**
     * Sets the sender's ID.
     *
     * @param senderId the new sender ID
     */
    public void setSenderId(String senderId) {
        this.senderId.set(senderId);
    }

    /**
     * Returns the property object for the receiver's ID.
     *
     * @return the receiver ID property
     */
    public StringProperty receiverIdProperty() {
        return receiverId;
    }

    /**
     * Returns the receiver's ID.
     *
     * @return the receiver ID
     */
    public String getReceiverId() {
        return receiverId.get();
    }

    /**
     * Sets the receiver's ID.
     *
     * @param receiverId the new receiver ID
     */
    public void setReceiverId(String receiverId) {
        this.receiverId.set(receiverId);
    }

    /**
     * Returns the property object for the message content.
     *
     * @return the message content property
     */
    public StringProperty messageProperty() {
        return message;
    }

    /**
     * Returns the content of the message.
     *
     * @return the message
     */
    public String getMessage() {
        return message.get();
    }

    /**
     * Sets the content of the message.
     *
     * @param message the new message content.
     */
    public void setMessage(String message) {
        this.message.set(message);
    }

    /**
     * Returns the property object for the date.
     *
     * @return the date property.
     */
    public ObjectProperty<LocalDateTime> dateProperty() {
        return date;
    }

    /**
     * Returns the timestamp of the message.
     *
     * @return the timestamp of the message
     */
    public LocalDateTime getDate() {
        return date.get();
    }

    /**
     * Sets the timestamp of the message.
     *
     * @param date the new timestamp
     */
    public void setDate(LocalDateTime date) {
        this.date.set(date);
    }

    /**
     * Returns a string representation of the {@link Message} entity.
     * The string contains the ID, sender id, receiver id, text message and a timestamp indicating when the message was
     * sent.
     *
     * @return a string representation of the friendship
     */
    @Override
    public String toString() {
        return "@MESSAGE | " +
                "ID <" + getId() + ">" +
                "\n         | SENDER <" + getSenderId() + ">" +
                "\n         | RECEIVER <" + getReceiverId() + ">" +
                "\n         | MESSAGE <" + getMessage() + ">" +
                "\n         | TIMESTAMP <" + getDate() + ">";
    }

    /**
     * Compares this friendship with another object for equality.
     * Two friendship requests are considered equal if their IDs and timestamps are equal.
     *
     * @param o the object to be compared
     * @return {@code true} if this friendship is equal to the object, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;
        return Objects.equals(getId(), message.getId()) &&
                Objects.equals(getDate(), message.getDate());
    }

    /**
     * Returns the hash code of this {@link Message} object, based on its IDs, sender ids, receiver ids, text message
     * and a timestamp indicating when the message was sent.
     *
     * @return the hash code value of the message
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSenderId(), getReceiverId(), getDate(), getMessage());
    }
}
