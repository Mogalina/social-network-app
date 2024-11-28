package org.example.service;

import org.example.models.Notification;
import org.example.repository.Repository;

/**
 * Specialized service class for performing operations on notifications sent to users via repository.
 */
public class NotificationService extends AbstractService<String, Notification> {

    /**
     * Constructs a new NotificationService with the specified repository.
     *
     * @param repository the repository used to perform operations on persisting data
     */
    public NotificationService(Repository<String, Notification> repository) {
        super(repository);
    }
}
