/**
 * Leader Election in Asyn Ring O(n^2) Algorithm
 * CS 249 Team #2 Rashmeet Khanuja, Anusha Vijay, Steven Yen
 */

import java.util.Observable;
import java.util.Observer;

public class Processor implements Observer {

    int procId; //identifier associated with this processor
    Buffer inChannel; //channel FROM right neighbor
    Buffer outChannel; //channel TO left neighbor
    Boolean isLeader; //is this processor a leader?

    /**
     * Constructor set inChannel, outChannel, and makes this
     * Processor a observer of its inChannel.
     * Initializes isLeader to false
     * @param id identifier of this processor
     * @param inBuff in channel FROM right neighbor
     * @param outBuff out channel TO left neighbor
     */
    public Processor(int id, Buffer inBuff, Buffer outBuff){
        procId = id;
        inChannel = inBuff;
        outChannel = outBuff;
        isLeader = false;
        inChannel.addObserver(this);
    }

    /**
     * Getter for procId field.
     */
    public int getProcId(){
        return procId;
    }

    /**
     * Getter for isLeader field.
     */
    public boolean getIsLeader(){
        return isLeader;
    }

    /**
     * Send message to this Processor by putting a message
     * in its inChannel. This causes update() of this processor.
     * This is used by other processors to send message to THIS processor.
     * to be called.
     * @param msg
     */
    public void sendMessageToMyInBuffer(Message msg){
        inChannel.setMessage(msg);
    }

    /**
     * Sends a message with this processors id to it's left neighbor.
     * It puts the message in the outChannel, which is the inChannel of the
     * left neighbor. So this triggers the update method of the left neigbhor.
     */
    public void sendMyIdToLeftNeighbor(){
        this.outChannel.setMessage(new Message(MessageType.IDENTIFIER,this.procId));
    }

    /**
     * This method is called when a message is send to the inChannel of this Processor
     * Contains the leader election logic
     */
    @Override
    synchronized public void update(Observable observable, Object o) {
        Message msgReceived = inChannel.getMessage();

        if(msgReceived.getMessageType().equals(MessageType.IDENTIFIER)){
            System.out.printf("P%d received %s message with id=%d %n",this.procId,msgReceived.getMessageType().toString(),msgReceived.getIdNumber());
            int senderId = msgReceived.getIdNumber();
            if(senderId>this.procId){
                //if the identifier is greater than its own id
                //forward the message to the left
                outChannel.setMessage(msgReceived);
            }else if(senderId<this.procId){
                //if senderId less than this processor's id
                //swallow the message and do nothing
            }else{
                System.out.printf("P%d declares self as leader %n",this.procId);
                //msgReceived has same id as this processor
                //declares self a leader by sending terminating msg to left neighbor
                //and terminating as a leader
                this.isLeader = true;
                //We pass the leader id in with the message. In case we want all proc to record leader in some field.
                outChannel.setMessage(new Message(MessageType.TERMINATE,this.procId));
            }
        }else if(msgReceived.getMessageType().equals(MessageType.TERMINATE)){
            System.out.printf("P%d received %s message%n",this.procId,msgReceived.getMessageType().toString());
            if(isLeader){
                //do nothing, as this is just the TERMINATE message it send went full circle.
                System.out.println("TERMINATE message went around entire ring. Algorithm done.");
            }else{
                outChannel.setMessage(msgReceived); //forward termination message
                this.isLeader = false; //terminate as non-leader
            }
        }
    }
}
