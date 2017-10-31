/**
 * Leader Election in Asyn Ring O(n^2) Algorithm
 * CS 249 Team #2 Rashmeet Khanuja, Anusha Vijay, Steven Yen
 */

import java.util.Observable;
import java.util.Observer;

public class Processor implements Observer {

    private int procId; //identifier associated with this processor
    private Processor leftProcessor;
    private Processor rightProcessor;
    Boolean isLeader; //is this processor a leader?
    Boolean asleep;
    Buffer messageBuffer;
    Boolean recievedLeft;
    Boolean recievedRight;
    int leader; //Holds the processid of the leader

    public Processor(int id){
        procId = id;
        asleep = true;
        isLeader = false;
        this.messageBuffer = new Buffer();
        this.messageBuffer.addObserver(this);
        recievedLeft = false; //left
        recievedRight = false; //right
        leader=-1;
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
    public void update(Observable observable, Object o) {
        messageBuffer = (Buffer) observable;
        Message text = messageBuffer.getMessage();
        Processor sender = messageBuffer.getSender();

        synchronized(messageBuffer) {
            switch (text.getMessageType()) {
                case PROBE:
                    int j = text.getIdNumber();
                    int d = text.getDistance();
                    int k = text.getPhase();
                    if (sender.getProcId() == this.leftProcessor.procId)
                        try {
                            messageProbe(messageBuffer, j, k, d, true);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    else
                        try {
                            messageProbe(messageBuffer, j, k, d, false);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    break;
                case REPLY:
                    j = text.getIdNumber();
                    k = text.getPhase();
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
                    if(leader!=-1)
                        break;
                    leader = text.getIdNumber();
                    if(leftProcessor.leader==-1) {
                        System.out.println("--->Forwarding Terminating Message to Process:" + leftProcessor.getProcId());
                        leftProcessor.sendMessageToMyBuffer(text, this);
                    }
                    break;
            }
        }
    }

    void messageProbe(Buffer messageBuffer, int j, int k, int d, boolean isLeft) throws InterruptedException {
        //if j== id, then terminate as leader
        if (j == procId) {
            isLeader = true;
            leader=procId;
            System.out.println("\nLeader:" + this.procId);
            sendTerminateMessage(procId);
        }

        //if it recieves a lower message do nothing.
        else if (j < this.procId) {
            return;
        }

        // j> id and d< 2k then send (probe,j,k,d+1) to right
        else if(j>this.procId && d < Math.pow(2,k)){
            sendProbe(j,k,d+1,isLeft);
        }

        //j >id and d>= 2k  then send reply  j,k to left
        else if (j > this.procId && d >= (Math.pow(2, k))) {
            sendReply(j,k,isLeft);
        }
    }
     void sendReply(int j,int k,boolean isLeft){
        Message replyMessage = new Message(MessageType.REPLY, j, k, -1);
        if (isLeft)
            leftProcessor.sendMessageToMyBuffer(replyMessage, this);
        else
            rightProcessor.sendMessageToMyBuffer(replyMessage, this);
    }

    void sendProbe(int j,int k,int d,boolean isLeft){
        Message probeMessage = new Message(MessageType.PROBE, j, k, d+1);
        if (isLeft)
            rightProcessor.sendMessageToMyBuffer(probeMessage, this);
        else
            leftProcessor.sendMessageToMyBuffer(probeMessage, this);

    }

     void messageReply(int j, int k, boolean isLeft) throws InterruptedException {
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
                    startNewPhase(k);
                recievedLeft=true;
            }
            else{
                if(recievedLeft)
                    startNewPhase(k);
                recievedRight=true;
            }
        }
    }

    void startNewPhase(int k) throws InterruptedException {
        this.recievedLeft = false;
        this.recievedRight = false;
        Thread.sleep(100);
        Message newPhase = new Message(MessageType.PROBE, this.procId, k + 1, 1);
        System.out.println("---------------------------------------------------");
        System.out.println("    Winner of Phase:" + k + " Process:" + this.procId);
        Thread.sleep(100);
        this.leftProcessor.sendMessageToMyBuffer(newPhase, this);
        this.rightProcessor.sendMessageToMyBuffer(newPhase, this);
    }


    void sendTerminateMessage(int procId){
        Message terminateMessage=new Message(MessageType.TERMINATE,procId);
        this.leftProcessor.sendMessageToMyBuffer(terminateMessage,this);
    }
}
