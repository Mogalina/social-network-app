package org.example.service;

import org.example.models.Entity;

import java.util.Optional;

/**
 * A generic service interface for managing operations between User Interface (UI) and Repository.
 *
 * @param <ID> the type of the unique identifier for the entity
 * @param <E> the type of the entity that extends {@link Entity} and is managed by the service and repository
 */
public interface Service<ID, E extends Entity<ID>> {

    /**
     * Finds an entity by its unique identifier.
     *
     * @param id the unique identifier of the entity to be retrieved
     * @return an {@link Optional} containing the entity with the specified ID, or an empty {@code Optional} if no
     *         entity is found
     */
    Optional<E> findById(ID id);

    /**
     * Retrieves all entities managed by the repository.
     *
     * @return an iterable collection of all entities in the repository
     */
    Iterable<E> findAll();

    /**
     * Saves a new entity or updates an existing entity in the repository.
     *
     * @param entity the entity to be saved or updated
     * @return an {@link Optional} containing the saved entity, or an empty {@code Optional} if no entity with the
     *         specified ID exists
     */
    Optional<E> save(E entity);

    /**
     * Deletes an entity by its unique identifier.
     *
     * @param id the unique identifier of the entity to be deleted
     * @return an {@link Optional} containing the deleted entity, or an empty {@code Optional} if no entity with the
     *         specified ID exists
     */
    Optional<E> deleteById(ID id);

    /**
     * Updates an existing entity in the repository.
     *
     * @param entity the entity with updated data
     * @return an {@link Optional} containing the updated entity
     */
    Optional<E> update(E entity);
}
