package com.sjsu.vector;


import java.util.Observable;

/**
 * Observable Buffer of each node
 * @author Sample
 * @version 1.0
 */
public class Buffer extends Observable {
    private Message message;

    /**
     * Creates empty buffer
     */
    public Buffer() {
        this.message = null;
    }
    /**
     * @return Message from the buffer
     */
    synchronized public Message getMessage() throws InterruptedException {
        return message;
    }

    /**
     * Sets message to null after the update method is called.
     * No need to notifyObserver() in this case.
     */

    synchronized public void setMessage(Message message) throws InterruptedException {
        this.message = message;
        this.setChanged();
        notifyObservers();

    }

}
