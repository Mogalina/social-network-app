package org.example.models.dtos;

import java.util.Optional;

/**
 * Data Transfer Object (DTO) for filtering users based on specific criteria.
 * This class encapsulates optional filter fields such as first name, last name, and email.
 */
public class UserFilterDTO {

    // Optional filter for the user's first name
    private Optional<String> firstName = Optional.empty();

    // Optional filter for the user's last name
    private Optional<String> lastName = Optional.empty();

    // Optional filter for the user's email
    private Optional<String> email = Optional.empty();

    /**
     * Gets the optional first name filter.
     *
     * @return an {@link Optional} containing the first name filter, or an empty {@link Optional} if not set
     */
    public Optional<String> getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name filter.
     *
     * @param firstName an {@link Optional} containing the first name filter to set, or an empty {@link Optional} to
     *                  clear it
     */
    public void setFirstName(Optional<String> firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the optional last name filter.
     *
     * @return an {@link Optional} containing the last name filter, or an empty {@link Optional} if not set
     */
    public Optional<String> getLastName() {
        return lastName;
    }

    /**
     * Sets the last name filter.
     *
     * @param lastName an {@link Optional} containing the last name filter to set, or an empty {@link Optional} to
     *                 clear it
     */
    public void setLastName(Optional<String> lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the optional email filter.
     *
     * @return an {@link Optional} containing the email filter, or an empty {@link Optional} if not set
     */
    public Optional<String> getEmail() {
        return email;
    }

    /**
     * Sets the email filter.
     *
     * @param email an {@link Optional} containing the email filter to set, or an empty {@link Optional} to clear it
     */
    public void setEmail(Optional<String> email) {
        this.email = email;
    }
}
