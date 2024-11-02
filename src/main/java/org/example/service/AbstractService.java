package org.example.service;

import org.example.models.Entity;
import org.example.repository.Repository;

import java.util.Optional;

/**
 * Abstract class for specialized services, providing base functionalities for managing communication between User
 * Interface (UI) and Repository.
 * This class represents a base class for specialized services.
 *
 * @param <ID> the type of the unique identifier for the entity
 * @param <E> the type of the entity that extends {@link Entity} and is managed by the service and repository
 */
public abstract class AbstractService<ID, E extends Entity<ID>> implements Service<ID, E> {

    // The repository used to perform operations on persisting data
    Repository<ID, E> repository;

    /**
     * Constructs a new AbstractService with the specified repository.
     *
     * @param repository the repository used to perform operations on persisting data
     */
    public AbstractService(Repository<ID, E> repository) {
        this.repository = repository;
    }

    /**
     * Finds an entity by its unique identifier.
     *
     * @param id the unique identifier of the entity to be retrieved
     * @return an {@link Optional} containing the entity with the specified ID, or an empty {@code Optional} if no
     *         entity is found
     */
    @Override
    public Optional<E> findById(ID id) {
        return repository.findOne(id);
    }

    /**
     * Retrieves all entities managed by the repository.
     *
     * @return an iterable collection of all entities in the repository
     */
    @Override
    public Iterable<E> findAll() {
        return repository.findAll();
    }

    /**
     * Saves a new entity or updates an existing entity in the repository.
     *
     * @param entity the entity to be saved or updated
     * @return an {@link Optional} containing the saved entity, or an empty {@code Optional} if no entity with the
     *         specified ID exists
     */
    @Override
    public Optional<E> save(E entity) {
        return repository.save(entity);
    }

    /**
     * Deletes an entity by its unique identifier.
     *
     * @param id the unique identifier of the entity to be deleted
     * @return an {@link Optional} containing the deleted entity, or an empty {@code Optional} if no entity with the
     *         specified ID exists
     */
    @Override
    public Optional<E> deleteById(ID id) {
        return repository.delete(id);
    }

    /**
     * Updates an existing entity in the repository.
     *
     * @param entity the entity with updated data
     * @return an {@link Optional} containing the updated entity
     */
    @Override
    public Optional<E> update(E entity) {
        return repository.update(entity);
    }
}
