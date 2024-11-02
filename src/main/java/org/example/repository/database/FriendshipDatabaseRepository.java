package org.example.repository.database;

import org.example.database.DatabaseConnection;
import org.example.models.Friendship;
import org.example.models.validators.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Repository class responsible for managing {@link Friendship} entities in the database.
 */
public class FriendshipDatabaseRepository extends AbstractDatabaseRepository<String, Friendship> {

    // The connection established for accessing the database
    private final Connection databaseConnection;

    /**
     * Constructs a new FriendshipDatabaseRepository with the specified {@link Validator}.
     *
     * @param validator the validator used to validate {@link Friendship} entities
     */
    public FriendshipDatabaseRepository(Validator<Friendship> validator) {
        super(validator);
        databaseConnection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Prepares a SQL statement to find a friendship by ID.
     *
     * @param id the unique identifier of the friendship
     * @return a PreparedStatement configured to find a friendship by ID
     * @throws SQLException if a database access error occurs
     */
    @Override
    protected PreparedStatement findOneQuery(String id) throws SQLException {
        String query = "SELECT * FROM friendships WHERE id = ?";
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(query);
        preparedStatement.setObject(1, java.util.UUID.fromString(id));
        return preparedStatement;
    }

    /**
     * Prepares a SQL statement to find all friendships.
     *
     * @return a PreparedStatement configured to find all friendships
     * @throws SQLException if a database access error occurs
     */
    @Override
    protected PreparedStatement findAllQuery() throws SQLException {
        String query = "SELECT * FROM friendships";
        return databaseConnection.prepareStatement(query);
    }

    /**
     * Prepares a SQL statement to save a new friendship to the database.
     *
     * @param entity the {@link Friendship} entity to be saved
     * @return a PreparedStatement configured to save the friendship
     * @throws SQLException if a database access error occurs
     */
    @Override
    protected PreparedStatement saveQuery(Friendship entity) throws SQLException {
        String query = "INSERT INTO friendships (uid1, uid2, date, pending) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(query);
        preparedStatement.setString(1, entity.getSenderId());
        preparedStatement.setString(2, entity.getReceiverId());
        preparedStatement.setTimestamp(3, java.sql.Timestamp.valueOf(entity.getDate()));
        preparedStatement.setBoolean(4, entity.isPending());
        return preparedStatement;
    }

    /**
     * Prepares a SQL statement to delete a friendship by ID.
     *
     * @param id the unique identifier of the {@link Friendship} to be deleted
     * @return a PreparedStatement configured to delete the friendship
     * @throws SQLException if a database access error occurs
     */
    @Override
    protected PreparedStatement deleteQuery(String id) throws SQLException {
        String query = "DELETE FROM friendships WHERE id = ?";
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(query);
        preparedStatement.setObject(1, java.util.UUID.fromString(id));
        return preparedStatement;
    }

    /**
     * Prepares a SQL statement to update an existing friendship.
     *
     * @param entity the {@link Friendship} entity with updated information
     * @return a PreparedStatement configured to update the friendship
     * @throws SQLException if a database access error occurs
     */
    @Override
    protected PreparedStatement updateQuery(Friendship entity) throws SQLException {
        String query = "UPDATE friendships SET uid1 = ?, uid2 = ?, date = ?, pending = ? WHERE id = ?";
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(query);
        preparedStatement.setString(1, entity.getSenderId());
        preparedStatement.setString(2, entity.getReceiverId());
        preparedStatement.setTimestamp(3, java.sql.Timestamp.valueOf(entity.getDate()));
        preparedStatement.setBoolean(4, entity.isPending());
        preparedStatement.setString(5, entity.getId());
        return preparedStatement;
    }

    /**
     * Builds a {@link Friendship} entity from the provided data retrieved from the database.
     *
     * @param resultSet the set containing friendship data retrieved from the database
     * @return a {@link Friendship} entity populated with data
     * @throws SQLException if a database access error occurs
     */
    @Override
    protected Friendship buildEntity(ResultSet resultSet) throws SQLException {
        String uid1 = resultSet.getString("uid1");
        String uid2 = resultSet.getString("uid2");
        LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
        boolean pending = resultSet.getBoolean("pending");
        String id = resultSet.getObject("id").toString();

        Friendship friendship = new Friendship(uid1, uid2);
        friendship.setDate(date);
        friendship.setPending(pending);
        friendship.setId(id);
        return friendship;
    }
}
