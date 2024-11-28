package org.example.service;

import org.example.models.Message;
import org.example.repository.Repository;

/**
 * Specialized service class for performing operations on messages between users via repository.
 */
public class MessageService extends AbstractService<String, Message> {

    /**
     * Constructs a new MessageService with the specified repository.
     *
     * @param repository the repository used to perform operations on persisting data
     */
    public MessageService(Repository<String, Message> repository) {
        super(repository);
    }
}
