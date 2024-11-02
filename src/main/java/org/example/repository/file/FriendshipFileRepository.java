package org.example.repository.file;

import org.example.models.Friendship;
import org.example.models.validators.Validator;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Repository class for managing Friendship entities in a file-based/in-memory storage.
 */
public class FriendshipFileRepository extends AbstractFileRepository<String, Friendship> {

    /**
     * Constructs a new FriendshipFileRepository with the specified file name and validator.
     *
     * @param fileName the name of the file used for persistence
     * @param validator the validator used to validate Friendship entities
     * @throws IOException if an error occurs while performing reading/writing operations on specified file
     */
    public FriendshipFileRepository(String fileName, Validator<Friendship> validator) throws IOException {
        super(fileName, validator);
    }

    /**
     * Extracts friendship fields from a record and creates a Friendship entity.
     *
     * @param record the string record containing the friendship's fields
     * @return the Friendship entity extracted from the record
     */
    @Override
    protected Friendship extractEntity(@NotNull String record) {
        List<String> fields = Arrays.asList(record.split(","));
        String id = fields.get(0);
        String uid1 = fields.get(1);
        String uid2 = fields.get(2);
        LocalDateTime date = LocalDateTime.parse(fields.get(3));
        boolean pending = Boolean.parseBoolean(fields.get(4));

        Friendship friendship = new Friendship(uid1, uid2);
        friendship.setDate(date);
        friendship.setPending(pending);
        friendship.setId(id);
        return friendship;
    }

    /**
     * Converts the Friendship entity to a string representation format for saving it to the specified file.
     *
     * @param friendship the entity to be converted
     * @return the string representation of the Friendship entity
     */
    @Override
    protected String entityToString(@NotNull Friendship friendship) {
        return friendship.getId() +
                "," + friendship.getSenderId() +
                "," + friendship.getReceiverId() +
                "," + friendship.getDate() +
                "," + friendship.isPending();
    }
}
