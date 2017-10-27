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
    Boolean recievedLeft;
    Boolean recievedRight;
    int leader;
    boolean sentClean;
    


    public Processor(int id) {
        procId = id;
        asleep = true;
        isLeader = false;
        this.messageBuffer = new Buffer();
        this.messageBuffer.addObserver(this);
        recievedLeft = false; //left
        recievedRight = false; //right
        leader=-1;
        sentClean=false;
    }

    public void setNeighbours(Processor left, Processor right) {
        this.leftProcessor = left;
        this.rightProcessor = right;
    }


    public int getProcId() {
        return procId;
    }


    public boolean getIsLeader() {
        return isLeader;
    }


    public void sendMessageToMyBuffer(Message msg, Processor sender) {
        this.messageBuffer.setMessage(msg, sender);
    }


    public Processor getLeftProcessor() {
        return leftProcessor;
    }

    public Processor getRightProcessor() {
        return rightProcessor;
    }


    @Override
    synchronized public void update(Observable observable, Object o) {
        messageBuffer = (Buffer) observable;
        Message text = messageBuffer.getMessage();
        Processor sender = messageBuffer.getSender();





        switch (text.getMessageType()) {
            case PROBE:
                int j = text.getIdNumber();
                int d = text.getDistance();
                int k = text.getPhase();
                //System.out.printf("\n<Message:%s,ID:%d,k:%d,d:%d>\n", text.getMessageType(), j, k, d);
                if (sender.getProcId() == this.leftProcessor.procId)
                    try {
                        System.out.println("\nRecieved message" + text.getMessageType() + "\tBy:" + this.procId + " From:" + messageBuffer.getSender().getProcId()+"Left <ID:"+j+",k:"+k+",d:"+d+">");

                        messageProbe(messageBuffer, j, k, d, true);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                else
                    try {
                        System.out.println("\nRecieved message" + text.getMessageType() + "\tBy:" + this.procId + " From:" + messageBuffer.getSender().getProcId()+ " Right <ID:"+j+",k:"+k+",d:"+d+">");
                        messageProbe(messageBuffer, j, k, d, false);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                break;
            case REPLY:
                j = text.getIdNumber();
                k = text.getPhase();
                System.out.printf("\nFrom: %d,<Message:%s,ID:%d,k:%d> By:%d\n",sender.getProcId(), text.getMessageType(), j, k ,this.getProcId());
                if (sender.getProcId() == this.leftProcessor.procId)

                    try {
                        messageReply(j, k, true);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                else
                    try {
                        messageReply(j, k, false);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                break;
            case TERMINATE:
                leader=text.getIdNumber();
                if(leader==procId)
                    isLeader=true;
                System.out.println("Forwarding Terminating Message to Process:"+leftProcessor.getProcId());
                leftProcessor.sendMessageToMyBuffer(text,this);
                Thread.interrupted();
                break;
            case CLEAN:
                System.out.println("\nRecieved CLEAN message!"+this.procId+" From"+messageBuffer.sender.procId);
                this.recievedLeft=false;
                this.recievedRight=false;
                Message clean=new Message(MessageType.CLEAN);
                if(!sentClean){
                    sentClean=true;
                    leftProcessor.sendMessageToMyBuffer(clean,this);}
        }
    }


    synchronized void messageProbe(Buffer messageBuffer, int j, int k, int d, boolean isLeft) throws InterruptedException {

        //if j== id, then terminate as leader
        if (j == procId) {
            isLeader = true;
            System.out.println("\n!!!!!!Leader bitches!" + this.procId);
            Message terminate=new Message(MessageType.TERMINATE,this.procId);
            this.leftProcessor.sendMessageToMyBuffer(terminate,this);
            Thread.interrupted();
        }

        //if it recieves a lower message do nothing.
        else if (j < this.procId) {
            //System.out.println("Probe Message from" + sender.procId + " Swallowed by:" + this.procId);
            return;
        }


        // j> id and d< 2k then send (probe,j,k,d+1) to right
        else if(j>this.procId && d < Math.pow(2,k)){
            Message probeMessage = new Message(MessageType.PROBE, j, k, d+1);
            if (isLeft)
                rightProcessor.sendMessageToMyBuffer(probeMessage, this);
            else
                leftProcessor.sendMessageToMyBuffer(probeMessage, this);
        }

        //j >id and d>= 2k  then send reply  j,k to left
        else if (j > this.procId && d >= (Math.pow(2, k))) {
            Message replyMessage = new Message(MessageType.REPLY, j, k, -1);
            if (isLeft)
                leftProcessor.sendMessageToMyBuffer(replyMessage, this);
            else
                rightProcessor.sendMessageToMyBuffer(replyMessage, this);

        }

       else System.out.println("Matched Nothing!");
    }

    synchronized void messageReply(int j, int k, boolean isLeft) throws InterruptedException {
        Thread.sleep(1000);
        if (j != this.procId) {
            if (isLeft) {
                recievedLeft=false;
                recievedRight=false;
                rightProcessor.sendMessageToMyBuffer(new Message(MessageType.REPLY, j, k, -1), this);
            } else {
                recievedLeft=false;
                recievedRight=false;
                leftProcessor.sendMessageToMyBuffer(new Message(MessageType.REPLY, j, k, -1), this);
            }

        } else {
            if (isLeft) {
                if (recievedRight)
                    startNewPhase(k,isLeft);
                recievedLeft=true;
                System.out.println("Recieved First Left by"+this.procId+" Phase"+k);
            }
            else{
                if(recievedLeft)
                    startNewPhase(k,isLeft);
                recievedRight=true;
                System.out.println("Recieved First Right by"+this.procId+" Phase"+k);
            }
        }
    }

    synchronized void startNewPhase(int k,boolean isLeft) throws InterruptedException {
        if(isLeft)
            System.out.println("Recieved Second Left"+this.procId+" Phase"+k);
        System.out.println("Recieved Second Right by"+this.procId+" Phase"+k);
        this.recievedLeft = false;
        this.recievedRight = false;
        Thread.sleep(100);
        Message newPhase = new Message(MessageType.PROBE, this.procId, k + 1, 1);
        System.out.println("---------------------------------------------------");
        System.out.println("    Winner of Phase:" + k + " Process:" + this.procId);
//        if(!sentClean)
//        {this.sentClean=true;
//        this.leftProcessor.sendMessageToMyBuffer(new Message(MessageType.CLEAN),this);}
        Thread.sleep(100);
        this.leftProcessor.sendMessageToMyBuffer(newPhase, this);
        this.rightProcessor.sendMessageToMyBuffer(newPhase, this);
    }
}
