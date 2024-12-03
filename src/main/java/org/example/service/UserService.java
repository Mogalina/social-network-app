package org.example.service;

import org.example.models.User;
import org.example.models.dtos.UserFilterDTO;
import org.example.repository.Repository;
import org.example.repository.database.UserDatabaseRepository;
import org.example.utils.Paging.Page;
import org.example.utils.Paging.Pageable;

/**
 * Specialized service class for performing operations on users via repository.
 */
public class UserService extends AbstractService<String, User> {

    /**
     * Constructs a new UserService with the specified repository.
     *
     * @param repository the repository used to perform operations on persisting data
     */
    public UserService(Repository<String, User> repository) {
        super(repository);
    }

    /**
     * Retrieves a paginated list of all users without applying any filter criteria.
     *
     * @param pageable the pagination details
     * @return a page containing all users for the specified pagination
     */
    public Page<User> findAllOnPage(Pageable pageable) {
        return ((UserDatabaseRepository) repository).findAllOnPage(pageable);
    }

    /**
     * Retrieves a paginated list of users along with the total number of matching users.
     *
     * @param pageable the pagination details
     * @param filter the filter criteria for querying users
     * @return a page containing the list of users and the total count of matching users
     */
    public Page<User> findAllOnPage(Pageable pageable, UserFilterDTO filter) {
        return ((UserDatabaseRepository) repository).findAllOnPage(pageable, filter);
    }
}
