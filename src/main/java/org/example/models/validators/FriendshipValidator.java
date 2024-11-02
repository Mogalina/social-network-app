package org.example.models.validators;

import org.example.exceptions.ValidationException;
import org.example.models.Friendship;
import org.example.models.User;
import org.jetbrains.annotations.NotNull;
import org.example.repository.Repository;

import java.util.Optional;

/**
 * Validator responsible for validating {@link Friendship} entities.
 * This class implement the Validator interface for the Friendship type.
 */
public class FriendshipValidator implements Validator<Friendship> {

    private final Repository<String, User> userRepository; // Repository for retrieving User entities

    /**
     * Constructs a new FriendshipValidator with a reference to {@link Repository} of {@link User} entities.
     *
     * @param userRepository the repository responsible for managing {@link User} entities
     */
    public FriendshipValidator(Repository<String, User> userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Validates the provided {@link Friendship} entity.
     *
     * This method is responsible for validating the following conditions:
     * <ul>
     *     <li>Both sender and receiver identifiers must not be null.</li>
     *     <li>Both sender and receiver must exist in the network.</li>
     *     <li>Sender and receiver must not be te same user.</li>
     * </ul>
     *
     * @param friendship the entity to be validated
     * @throws ValidationException if any of the validation rules are not satisfied
     */
    @Override
    public void validate(@NotNull Friendship friendship) throws ValidationException {
        // Retrieve sender and receiver from the user repository
        Optional<User> sender = userRepository.findOne(friendship.getSenderId());
        Optional<User> receiver = userRepository.findOne(friendship.getReceiverId());

        // Check if either the sender or receiver does not exist in the network
        if (sender.isEmpty() || receiver.isEmpty()) {
            throw new ValidationException("Sender or Receiver does not exist in the system");
        }

        // Check if neither the sender nor receiver identifier is null
        if (friendship.getSenderId() == null || friendship.getReceiverId() == null) {
            throw new ValidationException("Sender or Receiver id must not be null");
        }

        // Check if sender and receiver are the same user in the network
        if (friendship.getSenderId().equals(friendship.getReceiverId())) {
            throw new ValidationException("Sender and Receiver must not be the same");
        }
    }
}
