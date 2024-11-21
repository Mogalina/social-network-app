package org.example.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.example.utils.PasswordUtils;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents a User entity in the system network with a unique ID, first name, last name, hashed password, and email.
 * The {@code User} extends the {@code Entity} base class, using {@code String} as the type of its identifier.
 */
public class User extends Entity<String> {

    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty password;
    private final StringProperty email;

    /**
     * Constructs a new {@link User} with the specified first name, last name, password (hashed), and email.
     * The password is hashed with the SHA-256 algorithm using {@link PasswordUtils}.
     *
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param password the plain text password of the user (alternatively hashed)
     * @param email the email of the user
     */
    public User(String firstName, String lastName, String password, String email) {
        setId(UUID.randomUUID().toString());
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.password = new SimpleStringProperty(password);
        this.email = new SimpleStringProperty(email);
    }

    /**
     * Returns the first name of the user as a {@code StringProperty}.
     *
     * @return the first name of the user as a {@code StringProperty}
     */
    public StringProperty firstNameProperty() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName the new first name of the user
     */
    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    /**
     * Returns the last name of the user as a {@code StringProperty}.
     *
     * @return the last name of the user as a {@code StringProperty}
     */
    public StringProperty lastNameProperty() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName the new last name of the user
     */
    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    /**
     * Returns the hashed password of the user as a {@code StringProperty}.
     *
     * @return the hashed password of the user as a {@code StringProperty}
     */
    public StringProperty passwordProperty() {
        return password;
    }

    /**
     * Sets the password of the user and hashes it using {@code PasswordUtils}.
     *
     * @param password the new plain text password of the user (alternatively hashed)
     */
    public void setPassword(String password) {
        this.password.set(password);
    }

    /**
     * Returns the email address of the user as a {@code StringProperty}.
     *
     * @return the email address of the user as a {@code StringProperty}
     */
    public StringProperty emailProperty() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email the new email address of the user
     */
    public void setEmail(String email) {
        this.email.set(email);
    }

    /**
     * Returns the first name of the user.
     *
     * @return the first name of the user
     */
    public String getFirstName() {
        return firstName.get();
    }

    /**
     * Returns the last name of the user.
     *
     * @return the last name of the user
     */
    public String getLastName() {
        return lastName.get();
    }

    /**
     * Returns the hashed password of the user.
     *
     * @return the hashed password of the user
     */
    public String getPassword() {
        return password.get();
    }

    /**
     * Returns the email address of the user.
     *
     * @return the email address of the user
     */
    public String getEmail() {
        return email.get();
    }

    /**
     * Returns a string representation of the user.
     * The string contains the ID, class name, first name, last name, password (hashed), and the email address of the
     * user.
     *
     * @return a string representation of the user
     */
    @Override
    public String toString() {
        return "@USER | " +
                "ID <" + id + ">" +
                "\n      | FIRST_NAME <" + firstName.get() + ">" +
                "\n      | LAST_NAME <" + lastName.get() + ">" +
                "\n      | EMAIL <" + email.get() + ">" +
                "\n      | PASSWORD <" + password.get() + ">";
    }

    /**
     * Compares this user with another object for equality.
     * Two users are considered equal if their email addresses are equal.
     *
     * @param o the object to be compared
     * @return {@code true} if this user is equal to the object, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        return Objects.equals(email.get(), user.getEmail());
    }

    /**
     * Returns the hash code of this user, based on its identifier, first name, last name, and email.
     *
     * @return the hash code value of the user
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, firstName.get(), lastName.get(), email.get());
    }
}
