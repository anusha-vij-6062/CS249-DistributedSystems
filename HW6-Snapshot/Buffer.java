

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Observable Buffer of each node
 *
 * @author Sample
 * @version 1.0
 */
//A channel should have a buffer associated with it.
public class Buffer extends Observable {
    String label;
    private List<Message> messages;

    /**
     * Creates empty buffer
     */
    public Buffer() {
        this.messages = new ArrayList<>();
    }

    /**
     * Creates empty buffer
     */
    public Buffer(String label) {
        messages = new ArrayList<>();
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Get message at the specified index of the list.
     * @return Message from the buffer
     */
    public Message getMessage(int index) {
        return messages.get(index);
    }

    /**
     * Sets the message and notifies the observers with the sender node's information
     * Saves the message to the list of messasge
     * @param message Message to be stored in the buffer
     */
    public void saveMessage(Message message) {
        this.messages.add(message);
        setChanged();
        notifyObservers(message);
    }

    /**
     * Returns the number of messages stored in the list.
     * @return length of the list of message
     */
    int getTotalMessageCount() {
        return messages.size();
    }
}

