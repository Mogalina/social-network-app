package org.example.repository.file;

import org.example.models.User;
import org.example.models.validators.Validator;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Repository class for managing User entities in a file-based/in-memory storage.
 */
public class UserFileRepository extends AbstractFileRepository<String, User> {

    /**
     * Constructs a new UserFileRepository with the specified file name and validator.
     *
     * @param fileName the name of the file used for data persistence
     * @param validator the validator used to validate User entities
     * @throws IOException if an error occurs while performing reading/writing operations on specified file
     */
    public UserFileRepository(String fileName, Validator<User> validator) throws IOException {
        super(fileName, validator);
    }

    /**
     * Extracts user fields from a record and creates a User entity.
     *
     * @param record the string record containing the user fields
     * @return the User entity extracted from the record
     */
    @Override
    protected User extractEntity(@NotNull String record) {
        List<String> fields = Arrays.asList(record.split(","));
        String id = fields.get(0);
        String firstName = fields.get(1);
        String lastName = fields.get(2);
        String password = fields.get(3);
        String email = fields.get(4);

        User user = new User(firstName, lastName, password, email);
        user.setId(id);
        return user;
    }

    /**
     * Converts the User entity to a string representation format for saving it to the specified file.
     *
     * @param user the User entity to be converted
     * @return the string representation of the User entity
     */
    @Override
    protected String entityToString(@NotNull User user) {
        return user.getId() +
                "," + user.getFirstName() +
                "," + user.getLastName() +
                "," + user.getPassword() +
                "," + user.getEmail();
    }
}
