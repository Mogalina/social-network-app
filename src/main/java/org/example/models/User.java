package org.example.models;

import org.example.utils.PasswordUtils;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents a User entity in the system network with a unique ID, first name, last name, hashed password and email.
 * The {@code User} extends the {@code Entity} base class, using {@code Long} as the type of its identifier.
 */
public class User extends Entity<String> {

    private String firstName;
    private String lastName;
    private String password;
    private String email;

    /**
     * Constructs a new {@code User} with the specified first name, last name, password (hashed), and email.
     * The password is hashed with the SHA-256 algorithm using {@code PasswordUtils}.
     *
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param password the plain text password of the user (alternatively hashed)
     * @param email the email of the user
     */
    public User(String firstName, String lastName, String password, String email) {
        setId(UUID.randomUUID().toString());
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = PasswordUtils.hashPassword(password);
        this.email = email;
    }

    /**
     * Returns the first name of the user.
     *
     * @return the first name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName the new first name of the user
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the user.
     *
     * @return the last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName the new last name of the user
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the hashed password of the user.
     *
     * @return the hashed password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user and hashes it using {@code PasswordUtils}.
     *
     * @param password the new plain text password of the user (alternatively hashed)
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the email address of the user.
     *
     * @return the email address of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email the new email address of the user
     */
    public void setEmail(String email) {
        this.email = email;
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
                "\n      | FIRST_NAME <" + firstName + ">" +
                "\n      | LAST_NAME <" + lastName + ">" +
                "\n      | EMAIL <" + email + ">" +
                "\n      | PASSWORD <" + password + ">";
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
        return Objects.equals(email, user.getEmail());
    }

    /**
     * Returns the hash code of this user, based on its identifier, first name, last name, and email.
     *
     * @return the hash code value of the user
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email);
    }
}
