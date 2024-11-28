package org.example.models.validators;

import org.example.exceptions.ValidationException;
import org.example.models.Notification;
import org.example.models.User;
import org.example.repository.Repository;

import java.util.Optional;

/**
 * Validator responsible for validating {@link Notification} entities.
 * This class implement the Validator interface for the Notification type.
 */
public class NotificationValidator implements Validator<Notification> {

    // Repository for retrieving User entities
    private final Repository<String, User> userRepository;

    /**
     * Constructs a new NotificationValidator with a reference to {@link Repository} of {@link User} entities.
     *
     * @param userRepository the repository responsible for managing {@link User} entities
     */
    public NotificationValidator(Repository<String, User> userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Validates the provided {@link Notification} entity.
     *
     * This method is responsible for validating the following conditions:
     * <ul>
     *     <li>User identifier must not be null.</li>
     *     <li>User must exist in the network.</li>
     *     <li>Description must be at leat one character long.</li>
     *     <li>Description must be less than 300 characters long.</li>
     * </ul>
     *
     * @param notification the entity to be validated
     * @throws ValidationException if the notification is null or fails validation criteria
     */
    @Override
    public void validate(Notification notification) throws ValidationException {
        if (notification == null) {
            throw new ValidationException("Notification must not be null");
        }

        // Retrieve user from the user repository
        Optional<User> user = userRepository.findOne(notification.getUserId());

        // Check if the user does not exist in the network
        if (user.isEmpty()) {
            throw new ValidationException("User does not exist in the system");
        }

        // Check if message text is empty
        if (notification.getDescription().isEmpty()) {
            throw new ValidationException("Description must not be empty");
        }

        // Check if description is more than 300 characters long
        if (notification.getDescription().length() >= 300) {
            throw new ValidationException("Description must be less than 300 characters");
        }
    }
}
