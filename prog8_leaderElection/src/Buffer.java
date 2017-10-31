/**
 * Leader Election in Asyn Ring O(nlogn) Algorithm
 * CS 249 Team #2 Rashmeet Khanuja, Anusha Vijay, Steven Yen
 */

import java.util.Observable;

public class Buffer extends Observable{

    private Message message;


    public Buffer() {
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

    synchronized public void setMessage(Message msg,Processor sender){
        message = msg;
        setChanged();
        notifyObservers(sender);
    }

}
