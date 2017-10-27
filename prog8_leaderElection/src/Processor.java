/**
 * Leader Election in Asyn Ring O(n^2) Algorithm
 * CS 249 Team #2 Rashmeet Khanuja, Anusha Vijay, Steven Yen
 */

import java.util.Observable;
import java.util.Observer;
import java.util.*;

public class Processor implements Observer {

    private int procId; //identifier associated with this processor
    private Processor leftProcessor;
    private Processor rightProcessor;
    Boolean isLeader; //is this processor a leader?
    Boolean asleep;
    Buffer messageBuffer;
    Boolean [] recievedReply;


    /**
     * Constructor set inChannel, outChannel, and makes this
     * Processor a observer of its inChannel.
     * Initializes isLeader to false
     * @param id identifier of this processor
     */
    public Processor(int id){
        procId = id;
        asleep = true;
        isLeader = false;
        this.messageBuffer=new Buffer();
        this.messageBuffer.addObserver(this);
        this.recievedReply=new Boolean[2];
        recievedReply[0]=false; //left
        recievedReply[1]=false; //right
    }

    public void setNeighbours(Processor left,Processor right){
        this.leftProcessor=left;
        this.rightProcessor=right;
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
    public void sendMessageToMyBuffer(Message msg,Processor sender){
        this.messageBuffer.setMessage(msg,sender);
    }

//    public void start() throws InterruptedException {
//        if(this.asleep){
//            this.asleep=false;
//            System.out.println("Sending Start message from"+this.getProcId());
//            Message startMessage=new Message(MessageType.PROBE,this.getProcId(),0,0);
//            this.getLeftProcessor().sendMessageToMyBuffer(startMessage,this);
//            this.getRightProcessor().sendMessageToMyBuffer(startMessage,this);
//        }
//    }

    /**
     * Sends a message with this processors id to it's left neighbor.
     * It puts the message in the outChannel, which is the inChannel of the
     * left neighbor. So this triggers the update method of the left neigbhor.
     */
    public void sendMyIdToLeftNeighbor(){
        //this.outChannel.setMessage(new Message(MessageType.IDENTIFIER,this.procId));
    }

    public Processor getLeftProcessor() {
        return leftProcessor;
    }

    public Processor getRightProcessor() {
        return rightProcessor;
    }



    /**
     * This method is called when a message is send to the inChannel of this Processor
     * Contains the leader election logic
     *
     */
    @Override
    synchronized public void update(Observable observable, Object o) {
            messageBuffer = (Buffer) observable;
            Message text = messageBuffer.getMessage();
            int j = text.getIdNumber();
            int d = text.getDistance();
            int k = text.getPhase();

            System.out.println("\nRecieved message" + text.getMessageType() + "\tBy:" + this.procId + " From:" + messageBuffer.getSender().getProcId());


            if(text.getMessageType()==MessageType.PROBE)
                messageProbe(messageBuffer,j,k,d);
            else
                try {
                    messageReply(messageBuffer,j,k,d);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
    }

    void messageProbe(Buffer messageBuffer,int j,int k,int d){
        Processor sender = messageBuffer.getSender();
        Message text=messageBuffer.getMessage();
        if (text.getMessageType() == MessageType.PROBE && sender.getProcId() == this.leftProcessor.procId) {
            if (j < this.procId) {
                System.out.println("Probe Message from" + sender.procId + " Swallowed by:" + this.procId);
                return;
            }
            if (j == this.procId) {
                System.out.println(this.procId + ": Leader!");
                System.exit(0);
            }
            if (j > this.procId && (d < Math.pow(2, k))) {
                Message probeMessage = new Message(MessageType.PROBE, j, k, d + 1);
                this.rightProcessor.sendMessageToMyBuffer(probeMessage, this);
            }
            if (j > this.procId && (d >= Math.pow(2, k))) {
                Message replyMessage = new Message(MessageType.REPLY, j, k, -1);
                this.leftProcessor.sendMessageToMyBuffer(replyMessage, this);
            }

        }

        if (text.getMessageType() == MessageType.PROBE && sender.getProcId() == this.rightProcessor.procId) {
            if (j == this.procId) {
                System.out.println(this.procId + ": Leader!");
                isLeader = true;
                System.exit(0);
            }
            if (j > this.procId && (d < Math.pow(2, k))) {
                Message probeMessage = new Message(MessageType.PROBE, j, k, d + 1);
                this.leftProcessor.sendMessageToMyBuffer(probeMessage, this);
            }
            if (j > this.procId && (d >= Math.pow(2, k))) {
                Message replyMessage = new Message(MessageType.REPLY, j, k, -1);
                this.rightProcessor.sendMessageToMyBuffer(replyMessage, this);
            }
            if (j < this.procId) {
                System.out.println("Probe Message from" + sender.procId + " Swallowed by:" + this.procId);
            }
        }

    }

    void messageReply(Buffer messageBuffer,int j,int k,int d) throws InterruptedException {
        Processor sender = messageBuffer.getSender();
        Message text=messageBuffer.getMessage();
        if (text.getMessageType() == MessageType.REPLY && sender.procId == this.leftProcessor.procId) {
            this.recievedReply[0] = true;
            if (j != this.procId) {
                Message replyMessage = new Message(MessageType.REPLY, j, k, -1);
                rightProcessor.sendMessageToMyBuffer(replyMessage, this);
            } else if (recievedReply[1] && this.procId == j) {
                Message newProbeMessage = new Message(MessageType.PROBE, this.procId, k + 1, 1);
                System.out.println("!!!!!!Phase" + k + " Winner is: " + this.procId);
                leftProcessor.sendMessageToMyBuffer(newProbeMessage, this);
                rightProcessor.sendMessageToMyBuffer(newProbeMessage, this);

            }

        }

        if (text.getMessageType() == MessageType.REPLY && sender.procId == this.rightProcessor.procId) {
            this.recievedReply[1] = true;
            if (j != this.procId) {
                Message replyMessage = new Message(MessageType.REPLY, j, k, -1);
                leftProcessor.sendMessageToMyBuffer(replyMessage, this);
            } else if (recievedReply[0] && j == this.procId) {
                Thread.sleep(2000);
                Message newProbeMessage = new Message(MessageType.PROBE, this.procId, k + 1, 1);
                leftProcessor.sendMessageToMyBuffer(newProbeMessage, this);
                rightProcessor.sendMessageToMyBuffer(newProbeMessage, this);

            }

        }

    }

}
