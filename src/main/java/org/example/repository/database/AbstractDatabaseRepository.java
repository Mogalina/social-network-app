package org.example.repository.database;

import org.example.models.Entity;
import org.example.models.validators.Validator;
import org.example.repository.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Abstract class for database repositories, providing CRUD (Create, Read, Update, Delete) operations on entities from
 * the database.
 *
 * @param <ID> the type of the entity's identifier
 * @param <E> the type of the entity, which must extend {@link Entity<ID>}
 */
public abstract class AbstractDatabaseRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {

    // The validator used to validate entities
    private final Validator<E> validator;

    /**
     * Constructs a new AbstractDatabaseRepository with the specified {@link Validator}.
     *
     * @param validator the validator used to validate entities
     */
    public AbstractDatabaseRepository(Validator<E> validator) {
        this.validator = validator;
    }

    /**
     * Prepares a SQL statement to find an entity by ID.
     * Subclasses must implement this method to define how to find an entity by its identifier in the database.
     *
     * @param id the unique identifier of the entity
     * @return a PreparedStatement configured to find an entity by ID
     * @throws SQLException if a database access error occurs
     */
    protected abstract PreparedStatement findOneQuery(ID id) throws SQLException;

    /**
     * Prepares a SQL statement to find all entities.
     * Subclasses must implement this method to define how to find all entities in the database.
     *
     * @return a PreparedStatement configured to find all entities
     * @throws SQLException if a database access error occurs
     */
    protected abstract PreparedStatement findAllQuery() throws SQLException;

    /**
     * Prepares a SQL statement to save a new entity to the database.
     * Subclasses must implement this method to define how to save the entity in the database.
     *
     * @param entity the entity to be saved
     * @return a PreparedStatement configured to save the entity
     * @throws SQLException if a database access error occurs
     */
    protected abstract PreparedStatement saveQuery(E entity) throws SQLException;

    /**
     * Prepares a SQL statement to delete an entity by ID.
     * Subclasses must implement this method to define how to delete the entity from the database.
     *
     * @param id the unique identifier of the entity to be deleted
     * @return a PreparedStatement configured to delete the entity
     * @throws SQLException if a database access error occurs
     */
    protected abstract PreparedStatement deleteQuery(ID id) throws SQLException;

    /**
     * Prepares a SQL statement to update an existing entity.
     * Subclasses must implement this method to define how to update the entity from the database.
     *
     * @param entity the entity with updated information
     * @return a PreparedStatement configured to update the entity
     * @throws SQLException if a database access error occurs
     */
    protected abstract PreparedStatement updateQuery(E entity) throws SQLException;

    /**
     * Builds an entity from the provided data retrieved from the database.
     * Subclasses must implement this method to define how to build a specific entity from the retrieved data.
     *
     * @param resultSet the set containing entity data retrieved from the database
     * @return an entity populated with data
     * @throws SQLException if a database access error occurs
     */
    protected abstract E buildEntity(ResultSet resultSet) throws SQLException;

    /**
     * Retrieves an entity from the database by its identifier.
     *
     * @param id the unique identifier of the entity to be retrieved
     * @return an {@link Optional} containing the entity with the specified ID, or an empty {@code Optional} if no
     *         entity is found
     * @throws NullPointerException if the provided identifier is null
     * @throws RuntimeException if an SQL error occurs while trying to execute the query
     */
    @Override
    public Optional<E> findOne(ID id) {
        if (id == null) {
            throw new NullPointerException("ID cannot be null");
        }

        try (PreparedStatement statement = findOneQuery(id)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    E entity = buildEntity(resultSet);
                    return Optional.of(entity);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    /**
     * Retrieves all entities from the database.
     *
     * @return an iterable collection of all entities
     * @throws RuntimeException if an SQL error occurs while trying to execute the query
     */
    @Override
    public Iterable<E> findAll() {
        List<E> entities = new ArrayList<>();

        try (PreparedStatement statement = findAllQuery()) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    E entity = buildEntity(resultSet);
                    entities.add(entity);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return entities;
    }

    /**
     * Saves a new entity in the database.
     *
     * @param entity the entity to be saved
     * @return an {@link Optional} containing the saved entity, or an empty {@code Optional} if the entity already
     *         exists in the system
     * @throws NullPointerException if the provided entity is null
     * @throws RuntimeException if an SQL error occurs while trying to execute the query
     */
    @Override
    public Optional<E> save(E entity) {
        if (entity == null) {
            throw new NullPointerException("Entity must not be null");
        }

        validator.validate(entity);

        try (PreparedStatement statement = saveQuery(entity)) {
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    ID generatedId = (ID) resultSet.getString(1);
                    entity.setId(generatedId);
                }
            }
            return Optional.of(entity);
        } catch (SQLException e) {
            // Check for unique constraint violation
            if ("23505".equals(e.getSQLState())) {
                return Optional.empty();
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes an entity from the database by its identifier.
     *
     * @param id the unique identifier of the entity to be deleted
     * @return an {@link Optional} containing the deleted entity, or an empty {@code Optional} if no entity with the
     *         specified ID exists
     * @throws NullPointerException if the provided identifier is null
     * @throws RuntimeException if an SQL error occurs while trying to execute the query
     */
    @Override
    public Optional<E> delete(ID id) {
        if (id == null) {
            throw new NullPointerException("ID cannot be null");
        }

        Optional<E> entity = findOne(id);
        if (entity.isPresent()) {
            try (PreparedStatement statement = deleteQuery(id)) {
                statement.executeUpdate();
                return entity;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return Optional.empty();
    }

    /**
     * Updates an existing entity in the database.
     *
     * @param entity the entity to be updated
     * @return an {@link Optional} containing the updated entity
     * @throws NullPointerException if the provided entity is null
     * @throws RuntimeException if an SQL error occurs while trying to execute the query
     */
    @Override
    public Optional<E> update(E entity) {
        if (entity == null) {
            throw new NullPointerException("Entity must not be null");
        }

        validator.validate(entity);

        Optional<E> retrievedEntity = findOne(entity.getId());
        if (retrievedEntity.isPresent()) {
            try (PreparedStatement statement = updateQuery(entity)) {
                statement.executeUpdate();
                return Optional.of(entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return Optional.empty();
    }
}
