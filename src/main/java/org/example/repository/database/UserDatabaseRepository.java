package org.example.repository.database;

import javafx.util.Pair;
import org.example.database.DatabaseConnection;
import org.example.models.User;
import org.example.models.dtos.UserFilterDTO;
import org.example.models.validators.Validator;
import org.example.repository.paging.PagingRepository;
import org.example.utils.Paging.Page;
import org.example.utils.Paging.Pageable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Repository class responsible for managing {@link User} entities in the database.
 */
public class UserDatabaseRepository extends AbstractDatabaseRepository<String, User> implements PagingRepository<String, User> {

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

    /**
     * Converts the given {@link UserFilterDTO} into an SQL query fragment with its corresponding parameters.
     *
     * @param filter the filter criteria for querying users; can be null
     * @return a pair containing the SQL conditions and a list of query parameters
     */
    private Pair<String, List<Object>> toSQL(UserFilterDTO filter) {
        // Return an empty query if the filter is null
        if (filter == null) {
            return new Pair<>("", Collections.emptyList());
        }

        List<String> conditions = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        // Add conditions and parameters for filtering by first name
        filter.getFirstName().ifPresent(firstNameFilter -> {
            conditions.add("first_name LIKE ?");
            params.add("%" + firstNameFilter + "%");
        });

        // Add conditions and parameters for filtering by last name
        filter.getLastName().ifPresent(lastNameFilter -> {
            conditions.add("last_name LIKE ?");
            params.add("%" + lastNameFilter + "%");
        });

        // Add conditions and parameters for filtering by email
        filter.getEmail().ifPresent(emailFilter -> {
            conditions.add("email LIKE ?");
            params.add("%" + emailFilter + "%");
        });

        // Combine conditions into a single SQL WHERE clause
        String query = String.join(" AND ", conditions);
        return new Pair<>(query, params);
    }

    /**
     * Counts the total number of users in the database that match the given filter criteria.
     *
     * @param filter the filter criteria for counting users
     * @return the total count of matching users
     */
    private int count(UserFilterDTO filter) {
        String query = "SELECT COUNT(*) AS count FROM users";
        Pair<String, List<Object>> SQLFilter = toSQL(filter);

        // Append the filter conditions to the query, if any
        if (!SQLFilter.getKey().isEmpty()) {
            query += " WHERE " + SQLFilter.getKey();
        }

        // Execute the query and fetch the count
        try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
            int paramIndex = 0;
            for (Object param : SQLFilter.getValue()) {
                preparedStatement.setObject(++paramIndex, param);
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("count");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    /**
     * Retrieves a paginated list of users that match the given filter criteria.
     *
     * @param pageable the pagination details
     * @param filter the filter criteria for querying users
     * @return a list of users for the specified page
     */
    private List<User> findAllUsersOnPage(Pageable pageable, UserFilterDTO filter) {
        List<User> usersOnPage = new ArrayList<>();

        String query = "SELECT * FROM users";
        Pair<String, List<Object>> SQLFilter = toSQL(filter);

        // Append the filter conditions to the query, if any
        if (!SQLFilter.getKey().isEmpty()) {
            query += " WHERE " + SQLFilter.getKey();
        }
        query += " LIMIT ? OFFSET ?";

        // Execute the query to fetch the paginated users
        try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(query)) {
            int paramIndex = 0;
            for (Object param : SQLFilter.getValue()) {
                preparedStatement.setObject(++paramIndex, param);
            }

            // Set pagination parameters
            preparedStatement.setInt(++paramIndex, pageable.getPageSize());
            preparedStatement.setInt(++paramIndex, pageable.getPageSize() * pageable.getPageNumber());

            // Parse the result set and build the list of users
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    usersOnPage.add(buildEntity(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return usersOnPage;
    }

    /**
     * Retrieves a paginated list of users along with the total number of matching users.
     *
     * @param pageable the pagination details
     * @param filter the filter criteria for querying users
     * @return a page containing the list of users and the total count of matching users
     */
    public Page<User> findAllOnPage(Pageable pageable, UserFilterDTO filter) {
        int totalNumberOfRows = count(filter);
        List<User> usersOnPage;

        // Fetch users only if there are matching rows
        if (totalNumberOfRows > 0) {
            usersOnPage = findAllUsersOnPage(pageable, filter);
        } else {
            usersOnPage = new ArrayList<>();
        }

        return new Page<>(usersOnPage, totalNumberOfRows);
    }

    /**
     * Retrieves a paginated list of all users without applying any filter criteria.
     *
     * @param pageable the pagination details
     * @return a page containing all users for the specified pagination
     */
    @Override
    public Page<User> findAllOnPage(Pageable pageable) {
        return findAllOnPage(pageable, null);
    }
}
