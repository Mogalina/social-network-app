package org.example.repository.database;

import org.example.database.DatabaseConnection;
import org.example.models.Friendship;
import org.example.models.Tuple;
import org.example.models.validators.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class responsible for managing {@link Friendship} entities in the database.
 */
public class FriendshipDatabaseRepository extends AbstractDatabaseRepository<Tuple<String>, Friendship> {

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
     * Retrieves all {@link Friendship} entities from the database.
     *
     * @return an iterable collection of all friendships
     * @throws RuntimeException if an SQL error occurs while trying to execute the query
     */
    @Override
    public Iterable<Friendship> findAll() {
        List<Friendship> friendships = new ArrayList<>();

        try (PreparedStatement statement = findAllQuery()) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String uid1 = resultSet.getString("uid1");
                    String uid2 = resultSet.getString("uid2");
                    Friendship friendship = buildEntity(resultSet);
                    friendship.setId(new Tuple<>(uid1, uid2));
                    friendships.add(friendship);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return friendships;
    }

    /**
     * Prepares a SQL statement to find a friendship by ID.
     *
     * @param id the unique identifier of the friendship
     * @return a PreparedStatement configured to find a friendship by ID
     * @throws SQLException if a database access error occurs
     */
    @Override
    protected PreparedStatement findOneQuery(Tuple<String> id) throws SQLException {
        String query = "SELECT * FROM friendships WHERE uid1 = ? AND uid2 = ?";
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(query);
        preparedStatement.setObject(1, java.util.UUID.fromString(id.getFirst()));
        preparedStatement.setObject(2, java.util.UUID.fromString(id.getSecond()));
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
        String query = "SELECT * FROM friendships ORDER BY date ASC";
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
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        preparedStatement.setObject(1, java.util.UUID.fromString(entity.getSenderId()));
        preparedStatement.setObject(2, java.util.UUID.fromString(entity.getReceiverId()));
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
    protected PreparedStatement deleteQuery(Tuple<String> id) throws SQLException {
        String query = "DELETE FROM friendships WHERE uid1 = ? AND uid2 = ?";
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(query);
        preparedStatement.setObject(1, java.util.UUID.fromString(id.getFirst()));
        preparedStatement.setObject(2, java.util.UUID.fromString(id.getSecond()));
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
        String query = "UPDATE friendships SET date = ?, pending = ? WHERE uid1 = ? AND uid2 = ?";
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(query);
        preparedStatement.setTimestamp(1, java.sql.Timestamp.valueOf(entity.getDate()));
        preparedStatement.setBoolean(2, entity.isPending());
        preparedStatement.setObject(3, java.util.UUID.fromString(entity.getSenderId()));
        preparedStatement.setObject(4, java.util.UUID.fromString(entity.getReceiverId()));
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

        Friendship friendship = new Friendship(uid1, uid2);
        friendship.setDate(date);
        friendship.setPending(pending);
        friendship.setId(new Tuple<>(uid1, uid2));
        return friendship;
    }
}
