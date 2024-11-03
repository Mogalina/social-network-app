package org.example.service;

import org.example.models.Friendship;
import org.example.models.Tuple;
import org.example.repository.Repository;

/**
 * Specialized service class for performing operations on friendship relationships between users via repository.
 */
public class FriendshipService extends AbstractService<Tuple<String>, Friendship> {

    /**
     * Constructs a new FriendshipService with the specified repository.
     *
     * @param repository the repository used to perform operations on persisting data
     */
    public FriendshipService(Repository<Tuple<String>, Friendship> repository) {
        super(repository);
    }
}
