package org.example.models;

/**
 * Represents a reply message in a messaging system.
 * This class extends the {@link ReplyMessage} class and adds the functionality to associate a reply with an existing
 * message.
 */
public class ReplyMessage extends Message {

    // The original message to which this message is a reply
    private Message repliedMessage;

    /**
     * Constructs a new {@link ReplyMessage} object with the specified sender ID, receiver ID, message content and
     * replied message.
     * The timestamp is automatically set to the current date and time.
     *
     * @param senderId the ID of the user sending the message
     * @param receiverId the ID of the user receiving the message
     * @param message the content of the message
     * @param repliedMessage the message to which this message is a reply
     */
    public ReplyMessage(String senderId, String receiverId, String message, Message repliedMessage) {
        super(senderId, receiverId, message);
        this.repliedMessage = repliedMessage;
    }

    /**
     * Returns the original message to which this message is a reply.
     *
     * @return the replied message
     */
    public Message getRepliedMessage() {
        return repliedMessage;
    }

    /**
     * Sets the original message to which this message is a reply.
     *
     * @param repliedMessage the replied message to associate with this reply
     */
    public void setRepliedMessage(Message repliedMessage) {
        this.repliedMessage = repliedMessage;
    }
}
