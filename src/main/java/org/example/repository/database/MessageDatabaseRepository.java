package org.example.repository.database;

import org.example.database.DatabaseConnection;
import org.example.models.Message;
import org.example.models.User;
import org.example.models.validators.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class MessageDatabaseRepository extends AbstractDatabaseRepository<String, Message> {

    // The connection established for accessing the database
    private final Connection databaseConnection;

    /**
     * Constructs a new {@code MessageDatabaseRepository} with the specified {@link Validator}.
     *
     * @param validator the validator used to validate {@link User} entities
     */
    public MessageDatabaseRepository(Validator<Message> validator) {
        super(validator);
        databaseConnection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Prepares a SQL statement to find a message by ID.
     *
     * @param id the unique identifier of the message
     * @return a PreparedStatement configured to find a message by ID
     * @throws SQLException if a database access error occurs
     */
    @Override
    protected PreparedStatement findOneQuery(String id) throws SQLException {
        String query = "SELECT * FROM messages WHERE id = ?";
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(query);
        preparedStatement.setObject(1, java.util.UUID.fromString(id));
        return preparedStatement;
    }

    /**
     * Prepares a SQL statement to find all messages.
     *
     * @return a PreparedStatement configured to find all messages
     * @throws SQLException if a database access error occurs
     */
    @Override
    protected PreparedStatement findAllQuery() throws SQLException {
        String query = "SELECT * FROM messages";
        return databaseConnection.prepareStatement(query);
    }

    /**
     * Prepares a SQL statement to save a new message to the database.
     *
     * @param entity the {@link Message} entity to be saved
     * @return a PreparedStatement configured to save the message
     * @throws SQLException if a database access error occurs
     */
    protected PreparedStatement saveQuery(Message entity) throws SQLException {
        String query = "INSERT INTO messages (sid, rid, plain_text, date) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(query);
        preparedStatement.setObject(1, java.util.UUID.fromString(entity.getSenderId()));
        preparedStatement.setObject(2, java.util.UUID.fromString(entity.getReceiverId()));
        preparedStatement.setString(3, entity.getMessage());
        preparedStatement.setTimestamp(3, java.sql.Timestamp.valueOf(entity.getDate()));
        return preparedStatement;
    }

    /**
     * Prepares a SQL statement to delete a message by ID.
     *
     * @param id the unique identifier of the {@link Message} to be deleted
     * @return a PreparedStatement configured to delete the message
     * @throws SQLException if a database access error occurs
     */
    @Override
    protected PreparedStatement deleteQuery(String id) throws SQLException {
        String query = "DELETE FROM messages WHERE id = ?";
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(query);
        preparedStatement.setObject(1, java.util.UUID.fromString(id));
        return preparedStatement;
    }

    /**
     * Prepares a SQL statement to update an existing message.
     *
     * @param entity the {@link Message} entity with updated information
     * @return a PreparedStatement configured to update the message
     * @throws SQLException if a database access error occurs
     */
    protected PreparedStatement updateQuery(Message entity) throws SQLException {
        String query = "UPDATE messages SET sid = ?, rid = ?, plain_text = ?, date = ? WHERE id = ?";
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(query);
        preparedStatement.setObject(1, java.util.UUID.fromString(entity.getSenderId()));
        preparedStatement.setObject(2, java.util.UUID.fromString(entity.getReceiverId()));
        preparedStatement.setString(3, entity.getMessage());
        preparedStatement.setTimestamp(4, java.sql.Timestamp.valueOf(entity.getDate()));
        return preparedStatement;
    }

    /**
     * Builds a {@link Message} entity from the provided data retrieved from the database.
     *
     * @param resultSet the set containing message data retrieved from the database
     * @return a {@link Message} entity populated with data
     * @throws SQLException if a database access error occurs
     */
    protected Message buildEntity(ResultSet resultSet) throws SQLException {
        String id = resultSet.getObject("id").toString();
        String sender_id = resultSet.getObject("sid").toString();
        String receiver_id = resultSet.getObject("rid").toString();
        String plain_text = resultSet.getString("plain_text");
        LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();

        Message message = new Message(sender_id, receiver_id, plain_text);
        message.setDate(date);
        message.setId(id);
        return message;
    }
}
