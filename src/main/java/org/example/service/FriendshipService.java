package org.example.service;

import org.example.models.Friendship;
import org.example.models.User;
import org.example.repository.Repository;

import java.util.Optional;

/**
 * Specialized service class for performing operations on friendship relationships between users via repository.
 */
public class FriendshipService extends AbstractService<String, Friendship> {

    /**
     * Constructs a new FriendshipService with the specified repository.
     *
     * @param repository the repository used to perform operations on persisting data
     */
    public FriendshipService(Repository<String, Friendship> repository) {
        super(repository);
    }
}
