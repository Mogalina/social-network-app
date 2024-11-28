package org.example.models.validators;

import org.example.exceptions.ValidationException;
import org.example.models.Message;
import org.example.models.User;
import org.example.repository.Repository;

import java.util.Optional;

/**
 * Validator responsible for validating {@link Message} entities.
 * This class implement the Validator interface for the User type.
 */
public class MessageValidator implements Validator<Message> {

    private final Repository<String, User> userRepository; // Repository for retrieving User entities

    /**
     * Constructs a new MessageValidator with a reference to {@link Repository} of {@link User} entities.
     *
     * @param userRepository the repository responsible for managing {@link User} entities
     */
    public MessageValidator(Repository<String, User> userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Validates the provided {@link Message} entity.
     *
     * This method is responsible for validating the following conditions:
     * <ul>
     *     <li>Both sender and receiver identifiers must not be null.</li>
     *     <li>Both sender and receiver must exist in the network.</li>
     *     <li>Sender and receiver must not be te same user.</li>
     *     <li>Text message must be at leat one character long.</li>
     *     <li>Text message must be less than 1000 characters long.</li>
     * </ul>
     *
     * @param message the entity to be validated
     * @throws ValidationException if the user is null or fails validation criteria
     */
    @Override
    public void validate(Message message) throws ValidationException {
        if (message == null) {
            throw new ValidationException("Message must not be null");
        }

        // Retrieve sender and receiver from the user repository
        Optional<User> sender = userRepository.findOne(message.getSenderId());
        Optional<User> receiver = userRepository.findOne(message.getReceiverId());

        // Check if either the sender or receiver does not exist in the network
        if (sender.isEmpty() || receiver.isEmpty()) {
            throw new ValidationException("Sender or Receiver does not exist in the system");
        }

        // Check if neither the sender nor receiver identifier is null
        if (message.getSenderId() == null || message.getReceiverId() == null) {
            throw new ValidationException("Sender or Receiver id must not be null");
        }

        // Check if sender and receiver are the same user in the network
        if (message.getSenderId().equals(message.getReceiverId())) {
            throw new ValidationException("Sender and Receiver must not be the same");
        }

        // Check if message text is empty
        if (message.getMessage().isEmpty()) {
            throw new ValidationException("Message must not be empty");
        }

        // Check if message text is more than 1000 characters long
        if (message.getMessage().length() >= 1000) {
            throw new ValidationException("Message must be less than 1000 characters");
        }
    }
}
