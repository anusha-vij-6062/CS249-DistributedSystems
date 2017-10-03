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
     * Creates buffer with message
     *
     * @param message Message to be stored
     */
    public Buffer(Message message) {
        this.message = message;
    }

    /**
     * @return Message from the buffer
     */
    public Message getMessage() throws InterruptedException {
        return message;
    }

    /**
     * Sets the message and notifies the observers with the sender node's information
     *
     * @param message         Message to be stored in the buffer
     * @param //fromProcessor Node who sent the message
     */
    public void setMessage(Message message, Processor sender) throws InterruptedException {
        this.message = message;
        this.setChanged();
        //System.out.println("Message:: to be sent"+getMessage().messageType+"By"+sender.getId());
//        if(message.messageType==MessageType.RECIEVE){
//            synchronized (sender){
//                sender.notify();
//            }
//        }
        notifyObservers(sender);

    }

}