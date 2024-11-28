package org.example.repository.database;

import org.example.database.DatabaseConnection;
import org.example.models.Notification;
import org.example.models.validators.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class NotificationDatabaseRepository extends AbstractDatabaseRepository<String, Notification> {

    // The connection established for accessing the database
    private final Connection databaseConnection;

    /**
     * Constructs a new {@code NotificationDatabaseRepository} with the specified {@link Validator}.
     *
     * @param validator the validator used to validate {@link Notification} entities
     */
    public NotificationDatabaseRepository(Validator<Notification> validator) {
        super(validator);
        databaseConnection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Prepares a SQL statement to find a notification by ID.
     *
     * @param id the unique identifier of the notification
     * @return a PreparedStatement configured to find a notification by ID
     * @throws SQLException if a database access error occurs
     */
    @Override
    protected PreparedStatement findOneQuery(String id) throws SQLException {
        String query = "SELECT * FROM notifications WHERE id = ?";
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(query);
        preparedStatement.setObject(1, java.util.UUID.fromString(id));
        return preparedStatement;
    }

    /**
     * Prepares a SQL statement to find all notifications.
     *
     * @return a PreparedStatement configured to find all notifications
     * @throws SQLException if a database access error occurs
     */
    @Override
    protected PreparedStatement findAllQuery() throws SQLException {
        String query = "SELECT * FROM notifications";
        return databaseConnection.prepareStatement(query);
    }

    /**
     * Prepares a SQL statement to save a new notification to the database.
     *
     * @param entity the {@link Notification} entity to be saved
     * @return a PreparedStatement configured to save the notification
     * @throws SQLException if a database access error occurs
     */
    protected PreparedStatement saveQuery(Notification entity) throws SQLException {
        String query = "INSERT INTO notifications (description, date, uid) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(query);
        preparedStatement.setString(1, entity.getDescription());
        preparedStatement.setTimestamp(2, java.sql.Timestamp.valueOf(entity.getDate()));
        preparedStatement.setObject(3, java.util.UUID.fromString(entity.getUserId()));
        return preparedStatement;
    }

    /**
     * Prepares a SQL statement to delete a notification by ID.
     *
     * @param id the unique identifier of the {@link Notification} to be deleted
     * @return a PreparedStatement configured to delete the notification
     * @throws SQLException if a database access error occurs
     */
    @Override
    protected PreparedStatement deleteQuery(String id) throws SQLException {
        String query = "DELETE FROM notifications WHERE id = ?";
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(query);
        preparedStatement.setObject(1, java.util.UUID.fromString(id));
        return preparedStatement;
    }

    /**
     * Prepares a SQL statement to update an existing notification.
     *
     * @param entity the {@link Notification} entity with updated information
     * @return a PreparedStatement configured to update the notification
     * @throws SQLException if a database access error occurs
     */
    protected PreparedStatement updateQuery(Notification entity) throws SQLException {
        String query = "UPDATE messages SET description = ?, date = ?, uid = ? WHERE id = ?";
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(query);
        preparedStatement.setString(1, entity.getDescription());
        preparedStatement.setTimestamp(2, java.sql.Timestamp.valueOf(entity.getDate()));
        preparedStatement.setObject(3, java.util.UUID.fromString(entity.getUserId()));
        preparedStatement.setObject(4, java.util.UUID.fromString(entity.getId()));
        return preparedStatement;
    }

    /**
     * Builds a {@link Notification} entity from the provided data retrieved from the database.
     *
     * @param resultSet the set containing notification data retrieved from the database
     * @return a {@link Notification} entity populated with data
     * @throws SQLException if a database access error occurs
     */
    protected Notification buildEntity(ResultSet resultSet) throws SQLException {
        String id = resultSet.getObject("id").toString();
        String description = resultSet.getString("description");
        LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
        String userId = resultSet.getObject("uid").toString();

        Notification notification = new Notification(description, userId);
        notification.setDate(date);
        notification.setId(id);
        return notification;
    }
}
