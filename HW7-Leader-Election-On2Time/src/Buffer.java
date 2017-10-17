/**
 * Leader Election in Asyn Ring O(n^2) Algorithm
 * CS 249 Team #2 Rashmeet Khanuja, Anusha Vijay, Steven Yen
 */

import java.util.Observable;

public class Buffer extends Observable{
    private Message message;
    private String channelLabel; //label/name for channel

    /**
     * Constructor that creates a buffer with the specified label
     */
    public Buffer(String label){
        channelLabel = label;
    }

    /**
     * Getter for the channelLabel field
     */
    public String getLabel(){
        return channelLabel;
    }

    /**
     * Getter for the message field
     */
    public Message getMessage(){
        return message;
    }

    /**
     * Sets the message field and notifies observers of the change
     * so the update method of the observers are called.
     * @param msg message to be set
     */
    public void setMessage(Message msg){
        message = msg;
        setChanged();
        notifyObservers();
    }

}
