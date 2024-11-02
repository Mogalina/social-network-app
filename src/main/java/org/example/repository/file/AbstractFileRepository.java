package org.example.repository.file;

import org.example.models.Entity;
import org.example.models.validators.Validator;
import org.example.repository.memory.InMemoryRepository;
import org.example.utils.Config;

import java.io.*;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.nio.file.Paths;

/**
 * Abstract class for file-based repositories (in-memory/locally), providing CRUD (Create, Read, Update, Delete)
 * operations on entities from the specified file, while persisting data in file.
 *
 * @param <ID> the type of the entity's identifier
 * @param <E> the type of the entity, which must extend {@link Entity<ID>}
 */
public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {

    // The full path of the file used for data persistence
    private final String filePath;

    // Predefined Logger for error reporting across application
    private static final Logger LOGGER = Logger.getLogger(AbstractFileRepository.class.getName());

    /**
     * Constructs a new AbstractFileRepository with the specified file name and validator.
     *
     * @param fileName the name of the file used for data persistence
     * @param validator the validator used to validate the entities
     * @throws IOException if an error occurs while performing reading/writing operations on file
     */
    public AbstractFileRepository(String fileName, Validator<E> validator) throws IOException {
        super(validator);
        this.filePath = Paths.get(Config.DEFAULT_LOCAL_STORAGE_PATH, fileName + ".csv").toString();

        // Create new file if it does not exist
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }

        loadDataFromFile();
    }

    /**
     * Loads data from the specified file if exists, otherwise creates it, and populates the repository.
     * This method reads each line/record from the file, converts it to an entity, and stores it in the in-memory/local
     * collection.
     */
    private void loadDataFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                E entity = extractEntity(line);
                super.save(entity);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while loading data from file " + filePath, e);
        }
    }

    /**
     * Saves the current collection from the repository to the specified file.
     * This method iterates the entities collection, converts each to a string format, and writes it on a new line in
     * the specified file.
     */
    protected void saveDataToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (E entity : findAll()) {
                writer.write(entityToString(entity));
                writer.newLine();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while saving data to file " + filePath, e);
        }
    }

    /**
     * Extracts entity's fields from a record and creates an entity.
     * Subclasses must implement this method to define how to create an entity, based on the specific fields.
     *
     * @param record the string record containing the entity's fields
     * @return the entity extracted from the record
     */
    protected abstract E extractEntity(String record);

    /**
     * Converts the entity to a string representation format for saving it to the specified file.
     * Subclasses must implement this method to define how to represent the entity as a string.
     *
     * @param entity the entity to be converted
     * @return the string representation of the entity
     */
    protected abstract String entityToString(E entity);

    /**
     * Saves a new entity in the repository (storage) and updates the specified file.
     *
     * @param entity the entity to be saved
     * @return an {@link Optional} containing the saved entity, or an empty {@code Optional} if the entity already
     *         exists in the system
     * @throws NullPointerException if the provided entity is null
     */
    @Override
    public Optional<E> save(E entity) {
        Optional<E> savedEntity = super.save(entity);
        if (savedEntity.isPresent()) {
            saveDataToFile();
        }
        return savedEntity;
    }

    /**
     * Deletes an entity from the repository (storage) by its identifier and updates the specified file.
     *
     * @param id the unique identifier of the entity to be deleted
     * @return an {@link Optional} containing the deleted entity, or an empty {@code Optional} if no entity with the
     *         specified ID exists
     * @throws NullPointerException if the provided identifier is null
     */
    @Override
    public Optional<E> delete(ID id) {
        Optional<E> deletedEntity = super.delete(id);
        if (deletedEntity.isPresent()) {
            saveDataToFile();
        }
        return deletedEntity;
    }

    /**
     * Updates an existing entity in the repository (storage) and updates the specified file.
     *
     * @param entity the entity to be updated
     * @return an {@link Optional} containing the updated entity
     * @throws NullPointerException if the provided entity is null
     */
    @Override
    public Optional<E> update(E entity) {
        Optional<E> updatedEntity = super.update(entity);
        if (updatedEntity.isPresent()) {
            saveDataToFile();
        }
        return updatedEntity;
    }
}
