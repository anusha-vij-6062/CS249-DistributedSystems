
import java.util.*;

/**
 * Performs all the processor related tasks
 *
 * @author Sample
 * @version 1.0
 */

public class Processor implements Observer {

    List<Buffer> inChannels;
    int num = 0; //count of number of ALGORITHM messages received
    int ans = 0; //number ALGORITHM messages received before first MARKER message received
    int id;
    Map<Buffer,Thread> recorderList; //Holds a the thread list so it can be interrupted Later on.
    List<Buffer> outChannels = null;
    Boolean isDuplicate; //Flag
    /**
     * This is a map that will record the state of each incoming channel and all the messages
     * that have been received by this channel since the arrival of marker and receipt of duplicate marker
     */
    Map<Buffer, List<Message>> channelState = null;
    /**
     * This map is used to keep track of markers received on a channel. When a marker arrives at a channel
     * the channel is added to the map. If a marker arrives again then this map will have an entry already present from before.
     */
    Map<Buffer, Integer> channelMarkerCount = null;

    /**
     * @param id of the processor
     * @param inChannels List of incoming channels
     * @param outChannels List of outgoing channels from the processor
     */
    public Processor(int id, List<Buffer> inChannels, List<Buffer> outChannels) {
        this.inChannels = inChannels;
        this.outChannels = outChannels;
        this.id=id;
        this.num=0;
        for(Buffer b:inChannels){
            b.addObserver(this);
        }
        channelMarkerCount=new HashMap<>();
        channelState=new HashMap<>();
        recorderList=new HashMap<>();
        isDuplicate=false;

    }


    /**
     * A dummy implementation which will record current state and print out "Answer" of the algorithm
     */
    public void recordMyCurrentState() {
        System.out.printf("\nProcess %d: Recording my registers...", this.id);
        System.out.printf("\nProcess %d: Recording my program counters...", this.id);
        System.out.printf("\nProcess %d: Recording my local variables...\n", this.id);
    }

    /**
     * This method marks the channel as empty
     * @param channel
     */
    public void recordChannelAsEmpty(Buffer channel) {
        System.out.println("Clearing incoming channel"+channel.getLabel());
        channelState.put(channel, Collections.emptyList());
    }

    /**
     * This method spawns another Thread that starts recording a channel
     * @param channel The input channel which has to be monitered
     */

    public void recordChannel(Buffer channel){
        channel.startRecord=channel.getTotalMessageCount(); //
        Runnable r1=new ChannelRecorder(channel,this);
        Thread t=new Thread(r1);
        t.start();
        recorderList.put(channel,t);
        System.out.println("\nStart Recording from process: "+this.getId()+ " on channel "+channel.getLabel());
    }

    /**
     * Overloaded method, called with single argument
     * This method will add a message to this processors buffer.
     * Other processors will invoke this method to send a message to this Processor
     *
     * @param message Message to be sent
     * @param channel Channel the message to be sent on
     */
    public void sendMessgeTo(Message message, Buffer channel) {
        channel.saveMessage(message);
    }

    /**
     * Gets called when a Processor receives a message in its buffer
     * Processes the message received in the buffer
     */

    public void update(Observable observable, Object arg) {
        Message message=(Message) arg;
        if (message.getMessageType().equals(MessageType.MARKER)) {
            System.out.println("Recieved Marker Message by:"+this.id);
            Buffer fromChannel = (Buffer) observable;
            if (!isDuplicate) {
                isDuplicate=true;
                if (ans == 0){
                    ans = num;
                }
                System.out.println("Recieved first Marker Message by Processor :" + this.id + " On channel: " + fromChannel.getLabel());
                this.channelMarkerCount.put(fromChannel, 1);
                this.recordMyCurrentState();
                recordChannelAsEmpty(fromChannel);
                channelMarkerCount.put(fromChannel, channelMarkerCount.get(fromChannel) + 1); //don't need this
                for (Buffer incomingChannel : this.inChannels) {
                    if (incomingChannel != fromChannel) {
                        recordChannel(incomingChannel);
                    }
                }
                Message markerMessage = new Message(MessageType.MARKER);
                for (Buffer outgoingChannel : this.outChannels) {
                    sendMessgeTo(markerMessage, outgoingChannel);
                }
            }
            else {
                fromChannel.stopRecord=fromChannel.getTotalMessageCount();
                System.out.println("Recieved Duplicated Marker Message By:"+this.id+" On: "+fromChannel.getLabel());
                System.out.println("Stop Recording the channel"+fromChannel.getLabel()+"By Process:"+this.id);
                int size=fromChannel.getTotalMessageCount();
                System.out.println("Start Index:"+fromChannel.startRecord);
                System.out.println("Stop Index:"+fromChannel.stopRecord);
                recorderList.get(fromChannel).interrupt();
            }
        }
        else{
            //Performing Algorithm
            if (message.getMessageType().equals(MessageType.ALGORITHM)) {
                num++;
            }
        }
    }

    /**
     * 1. Record current state (done)
     * 2. Send Marker message on all output channels (done)
     * 3. Record incoming channels
     */

    public void initiateSnapShot() throws InterruptedException {
        isDuplicate=true;
        recordMyCurrentState();
        Message markerMessage=new Message(MessageType.MARKER);
        inChannels.get(0).startRecord=inChannels.get(0).getTotalMessageCount();
        inChannels.get(1).startRecord=inChannels.get(1).getTotalMessageCount();
        for(Buffer inChannel:this.inChannels){
            this.recordChannel(inChannel);
        }
        if(ans == 0){
            ans = num;
        }
        //Send marker message to all outgoing channels.
        for(Buffer outChannel: this.outChannels){
            System.out.println("Sending Marker Message to channel :"+outChannel.getLabel());
            this.channelMarkerCount.put(outChannel,1);
            sendMessgeTo(markerMessage,outChannel);
        }

    }
    /**
     * Looks up channel by label
     * @param lab label of desired channel.
     * @return the channel with matching label or null if none.
     */
    public Buffer getOutChannelByLabel(String lab){
        for(Buffer buff : outChannels){
            if(buff.getLabel().equals(lab)){
                return buff;
            }
        }
        return null;
    }
    /**
     * getter for the process ID.
     * @return
     */
    public int getId(){

        return id;
    }
}