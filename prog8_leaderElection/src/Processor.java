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
    //Boolean recievedLeft;
    //Boolean recievedRight;
    int leader;
    boolean sentClean;
    int received;



    public Processor(int id) {
        procId = id;
        asleep = true;
        isLeader = false;
        this.messageBuffer = new Buffer();
        this.messageBuffer.addObserver(this);
        //recievedLeft = false; //left
        //recievedRight = false; //right
        received=0;
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

                if(procId == 11 && j==22 && k==1 && d==1) {
                    System.out.println("STOP!");
                }

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
                if(leader!=-1)
                    break;
                else {
                    leader = text.getIdNumber();
                    if (leader == procId)
                        isLeader = true;
                    System.out.println("Forwarding Terminating Message to Process:" + leftProcessor.getProcId());
                    leftProcessor.sendMessageToMyBuffer(text, this);
                    break;
                }
        }
    }


    void messageProbe(Buffer messageBuffer, int j, int k, int d, boolean isLeft) throws InterruptedException {
        //if j== id, then terminate as leader

        if(procId == 60 && k==1 ) {
            System.out.println("STOP!");
        }


        if (j == procId) {
            isLeader = true;
            System.out.println("\n!!!!!!Leader bitches!" + this.procId);
            Message terminate=new Message(MessageType.TERMINATE,this.procId);
            this.leftProcessor.sendMessageToMyBuffer(terminate,this);
            return;
        }

//        //if it recieves a lower message do nothing.
        if (j < this.procId) {
            System.out.println("Probe Message from" + messageBuffer.getSender().procId + " Swallowed by:" + this.procId);
            return;
        }

        // j> id and d< 2k then send (probe,j,k,d+1) to right
        System.out.println("--->>"+(int)Math.pow(2,k));
        if(j>this.procId && d < (int)(Math.pow(2,k))){
            System.out.println("Incrementing Hopcounter, Forwarding");
            Message probeMessage = new Message(MessageType.PROBE, j, k, d+1);
            if (isLeft)
                rightProcessor.sendMessageToMyBuffer(probeMessage, this);
            else
                leftProcessor.sendMessageToMyBuffer(probeMessage, this);
            return;
        }

        //j >id and d>= 2k  then send reply  j,k to left
        if (j > this.procId && d >= (int)(Math.pow(2, k))) {
            System.out.println("Sending reply matched d=pwd"+d+"="+(int)(Math.pow(2, k)));
            Message replyMessage = new Message(MessageType.REPLY, j, k, -1);
            if (isLeft)
                leftProcessor.sendMessageToMyBuffer(replyMessage, this);
            else
                rightProcessor.sendMessageToMyBuffer(replyMessage, this);
            return;

        }
    }

    void messageReply(int j, int k, boolean isLeft) throws InterruptedException {
        System.out.println("!!!Current Value:"+received+"Process::"+procId);
        if (j != this.procId) {
            System.out.println("Forwarding Reply");
            if (isLeft) {
//                recievedLeft=false;
//                recievedRight=false;
                rightProcessor.sendMessageToMyBuffer(new Message(MessageType.REPLY, j, k, -1), this);
                return;
            } else {
//                recievedLeft=false;
//                recievedRight=false;
                leftProcessor.sendMessageToMyBuffer(new Message(MessageType.REPLY, j, k, -1), this);
                return;
            }

        } else {
            if (isLeft) {
                if (received==1) {
                    received++;
                    System.out.println("Left:Winner! --> should be 2:"+received);
                    startNewPhase(k, isLeft);
                }
                received++;
                System.out.println("Left:Recieved First Left by"+this.procId+" Phase"+k+"\nUpdated! should be 1 value of rec:-->"+this.received);
            }
            else{
                System.out.println("Right: Prev value of rec:"+this.received+"Process::"+procId);
                if(received==1) {
                    System.out.println("Right: Winner! --> should be 2:"+received);
                    received++;
                    startNewPhase(k, isLeft);
                    return;
                }
                received++;
                System.out.println("Recieved First Right by"+this.procId+" Phase"+k+"Should be 1--->"+received);
                return;
            }
        }

    }

   void startNewPhase(int k,boolean isLeft) throws InterruptedException {
        System.out.println("Start Phase value of rec:"+this.received);
        if(isLeft)
            System.out.println("Recieved Second Left"+this.procId+" Phase"+k);
        else
            System.out.println("Recieved Second Right by"+this.procId+" Phase"+k);
        received=0;
        Message newPhase = new Message(MessageType.PROBE, this.procId, k + 1, 1);
        System.out.println("---------------------------------------------------");
        System.out.println("    Winner of Phase:" + k + " Process:" + this.procId);
        Thread.sleep(10);
        this.rightProcessor.sendMessageToMyBuffer(newPhase, this);
        Thread.sleep(1000);
        this.leftProcessor.sendMessageToMyBuffer(newPhase, this);


    }
}
