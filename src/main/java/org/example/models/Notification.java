package org.example.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a {@code Notification} entity for notifying users about updates.
 *
 * The {@code Notification} extends the {@code Entity} base class, using {@code String} as the type of its identifier.
 */
public class Notification extends Entity<String> {

    private final StringProperty description;
    private final ObjectProperty<LocalDateTime> date;
    private final StringProperty userId;

    /**
     * Constructs a new {@link Notification} object with the specificed description and user identifier.
     * The timestamp is marked as current as default.
     *
     * @param description the description of the notification
     */
    public Notification(String description, String userId) {
        this.description = new SimpleStringProperty(description);
        this.date = new SimpleObjectProperty<>(LocalDateTime.now());
        this.userId = new SimpleStringProperty(userId);
    }

    /**
     * Returns the property object for the notification user identifier.
     *
     * @return the notification user ifentifier property
     */
    public StringProperty userIdProperty() {
        return userId;
    }

    /**
     * Returns the property object for the notification description.
     *
     * @return the notification description property
     */
    public StringProperty descriptionProperty() {
        return description;
    }

    /**
     * Returns the property object for the notification timestamp.
     *
     * @return the notification timestamp property
     */
    public ObjectProperty<LocalDateTime> dateProperty() {
        return date;
    }

    /**
     * Returns the user identifier of the notification.
     *
     * @return the user identifier of the notification
     */
    public String getUserId() {
        return userId.get();
    }

    /**
     * Returns the description of the notification.
     *
     * @return the description of the notification
     */
    public String getDescription() {
        return description.get();
    }

    /**
     * Sets the description of the notification.
     *
     * @param description the new description of the notification
     */
    public void setDescription(String description) {
        this.description.set(description);
    }

    /**
     * Returns the timestamp of the notification.
     *
     * @return the timestamp of the notification
     */
    public LocalDateTime getDate() {
        return date.get();
    }

    /**
     * Sets the timestamp of the notification.
     *
     * @param date the new timestamp of the notification
     */
    public void setDate(LocalDateTime date) {
        this.date.set(date);
    }

    /**
     * Returns a string representation of the {@link Notification} entity.
     * The string contains the ID, description and timestamp of the notification.
     *
     * @return the string representation of the notification
     */
    @Override
    public String toString() {
        return "@NOTIFICATION | " +
                "ID <" + getId() + ">" +
                "\n              | DESCRIPTION <" + getDescription() + ">" +
                "\n              | DATE <" + getDate() + ">" +
                "\n              | USER ID <" + getUserId() + ">";
    }

    /**
     * Compares this {@link Notification} entity with another object for equality.
     * Two notifications are considered equal if thrir IDs are equal.
     *
     * @param o the object to be compared
     * @return {@code true} if this notification is equal to the object, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Notification notification = (Notification) o;
        return Objects.equals(getId(), notification.getId());
    }

    /**
     * Returns the hash code of this {@link Notification} object, based on its IDs, description, timestamp and user
     * identifier.
     *
     * @return the hash code value of the notification
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription(), getDate(), getUserId());
    }
}
