package org.example.repository.database;

import org.example.database.DatabaseConnection;
import org.example.models.User;
import org.example.models.validators.Validator;
import org.example.utils.PasswordUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Repository class responsible for managing {@link User} entities in the database.
 */
public class UserDatabaseRepository extends AbstractDatabaseRepository<String, User> {

    // The connection established for accessing the database
    private final Connection databaseConnection;

    /**
     * Constructs a new UserDatabaseRepository with the specified {@link Validator}.
     *
     * @param validator the validator used to validate {@link User} entities
     */
    public UserDatabaseRepository(Validator<User> validator) {
        super(validator);
        databaseConnection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Prepares a SQL statement to find a user by ID.
     *
     * @param id the unique identifier of the user
     * @return a PreparedStatement configured to find a user by ID
     * @throws SQLException if a database access error occurs
     */
    @Override
    protected PreparedStatement findOneQuery(String id) throws SQLException {
        String query = "SELECT * FROM users WHERE id = ?";
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(query);
        preparedStatement.setObject(1, java.util.UUID.fromString(id));
        return preparedStatement;
    }

    /**
     * Prepares a SQL statement to find all users.
     *
     * @return a PreparedStatement configured to find all users
     * @throws SQLException if a database access error occurs
     */
    @Override
    protected PreparedStatement findAllQuery() throws SQLException {
        String query = "SELECT * FROM users";
        return databaseConnection.prepareStatement(query);
    }

    /**
     * Prepares a SQL statement to save a new user to the database.
     *
     * @param entity the {@link User} entity to be saved
     * @return a PreparedStatement configured to save the user
     * @throws SQLException if a database access error occurs
     */
    @Override
    protected PreparedStatement saveQuery(User entity) throws SQLException {
        String query = "INSERT INTO users (first_name, last_name, email, password) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, entity.getFirstName());
        preparedStatement.setString(2, entity.getLastName());
        preparedStatement.setString(3, entity.getEmail());
        preparedStatement.setString(4, entity.getPassword());
        return preparedStatement;
    }

    /**
     * Prepares a SQL statement to delete a user by ID.
     *
     * @param id the unique identifier of the {@link User} to be deleted
     * @return a PreparedStatement configured to delete the user
     * @throws SQLException if a database access error occurs
     */
    @Override
    protected PreparedStatement deleteQuery(String id) throws SQLException {
        String query = "DELETE FROM users WHERE id = ?";
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(query);
        preparedStatement.setObject(1, java.util.UUID.fromString(id));
        return preparedStatement;
    }

    /**
     * Prepares a SQL statement to update an existing user.
     *
     * @param entity the {@link User} entity with updated information
     * @return a PreparedStatement configured to update the user
     * @throws SQLException if a database access error occurs
     */
    @Override
    protected PreparedStatement updateQuery(User entity) throws SQLException {
        String query = "UPDATE users SET first_name = ?, last_name = ?, email = ?, password = ? WHERE id = ?";
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(query);
        preparedStatement.setString(1, entity.getFirstName());
        preparedStatement.setString(2, entity.getLastName());
        preparedStatement.setString(3, entity.getEmail());
        preparedStatement.setString(4, entity.getPassword());
        preparedStatement.setObject(5, java.util.UUID.fromString(entity.getId()));
        return preparedStatement;
    }

    /**
     * Builds a {@link User} entity from the provided data retrieved from the database.
     *
     * @param resultSet the set containing user data retrieved from the database
     * @return a {@link User} entity populated with data
     * @throws SQLException if a database access error occurs
     */
    @Override
    protected User buildEntity(ResultSet resultSet) throws SQLException {
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        String id = resultSet.getObject("id").toString();

        User user = new User(firstName, lastName, password, email);
        user.setId(id);
        return user;
    }
}
