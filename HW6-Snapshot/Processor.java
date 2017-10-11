

import java.util.*;

/**
 * Performs all the processor related tasks
 *
 * @author Sample
 * @version 1.0
 */


public class Processor implements Observer {

    List<Buffer> inChannels = new ArrayList<>();
    int num = 0; //count of number of ALGORITHM messages received
    int ans = 0; //number ALGORITHM messages received before first MARKER message received
    int id;


    /**
     * List of output channels
     * //TODO: Homework: Use appropriate list implementation and replace null assignment with that
     *
     */
    List<Buffer> outChannels = null;

    /**
     * This is a map that will record the state of each incoming channel and all the messages
     * that have been received by this channel since the arrival of marker and receipt of duplicate marker
     * //TODO: Homework: Use appropriate Map implementation.
     */
    Map<Buffer, List<Message>> channelState = null;

    /**
     * This map can be used to keep track of markers received on a channel. When a marker arrives at a channel
     * put it in this map. If a marker arrives again then this map will have an entry already present from before.
     * Before  doing a put in this map first do a get and check if it is not null. ( to find out if an entry exists
     * or not). If the entry does not exist then do a put. If an entry already exists then increment the integer value
     * and do a put again.
     */
    Map<Buffer, Integer> channelMarkerCount = null;


    /**
     * @param id of the processor
     */
    public Processor(int id, List<Buffer> inChannels, List<Buffer> outChannels) {
        this.inChannels = inChannels;
        this.outChannels = outChannels;
        this.id=id;
        this.num=0;
        //TODO: Homework make this processor as the observer for each of its inChannel
        //Hint [loop through each channel and add Observer (this) . Feel free to use java8 style streams if it makes
        // it look cleaner]
        for(Buffer b:inChannels){
            b.addObserver(this);
        }
        channelMarkerCount=new HashMap<>();
        channelState=new HashMap<>();
    }


    /**
     * This is a dummy implementation which will record current state
     * of this processor
     */
    public void recordMyCurrentState() {
        System.out.printf("\nProcess %d: Recording my registers...",this.id);
        System.out.printf("\nProcess %d: Recording my program counters...",this.id);
        System.out.printf("\nProcess %d: Recording my local variables...\n",this.id);
    }

    /**
     * THis method marks the channel as empty
     * @param channel
     */
    public void recordChannelAsEmpty(Buffer channel) {
        System.out.println("Clearning incoming channel"+channel.getLabel());

        channelState.put(channel, Collections.emptyList());

    }

    /**
     * You should send a message to this recording so that recording is stopped
     * //TODO: Homework: Move this method recordChannel(..) out of this class. Start this method in a
     *                  separate thread. This thread will start when the marker arrives and it will stop
     *                  when a duplicate marker is received. This means that each processor will have the
     *                  capability to start and stop this channel recorder thread. The processor manages the
     *                  record Channel thread. Processor will have the ability to stop the thread.
     *                  Feel free to use any java concurrency  and thread classes to implement this method
     *
     *
     * @param channel The input channel which has to be monitered
     */

    public void recordChannel(Buffer channel) {
        System.out.println("Recording Incoming channels"+channel.getLabel());
        //Here print the value stored in the inChannels to stdout or file

        //TODO:Homework: Channel will have messages from before a marker has arrived. Record messages only after a
        //               marker has arrived.
        //               [hint: Use the getTotalMessageCount () method to get the messages received so far.
        int lastIdx = channel.getTotalMessageCount();
        List<Message> recordedMessagesSinceMarker = new ArrayList<>();
            //TODO: Homework: Record messages
            // [Hint: Get the array that is storing the messages from the channel. Remember that the Buffer class
            // has a member     private List<Message> messages;  which stores all the messages received.
            // When a marker has arrived sample this List of messages and copy only those messages that
            // are received since the marker arrived. Copy these messages into recordedMessagesSinceMarker
            // and put it in the channelState map.
            //
            // ]

        //channelState.put(channel, recordedMessagesSinceMarker);

    }

    /**
     * Overloaded method, called with single argument
     * This method will add a message to this processors buffer.
     * Other processors will invoke this method to send a message to this Processor
     *
     * @param message Message to be sent
     */
    public void sendMessgeTo(Message message, Buffer channel) {
        channel.saveMessage(message);

    }

    /**
     *
     * @return true if this is the first marker false otherwise
     */
    public boolean isFirstMarker() {
        //TODO: Implemetent this method
        for (Buffer in : inChannels) {
            if (channelMarkerCount.containsKey(in)) {
                //System.out.println("Found duplicate marker Message);
                return false;
            }

        }
        for (Buffer out : outChannels) {
            if (channelMarkerCount.containsKey(out)) {
                //System.out.println("Found duplicate marker Message recieved by channel: "+out.getLabel());
                return false;
            }

        }


        return true;
    }

        //[ Hint : Use the channelMarkerCount]


    /**
     * Gets called when a Processor receives a message in its buffer
     * Processes the message received in the buffer
     */
    public void update(Observable observable, Object arg) {
        //Buffer text=(Buffer) observable;
        //Message message = text.getMessage(0);
        Message message=(Message) arg;

        if (message.getMessageType().equals(MessageType.MARKER)) {
            System.out.println("Recieved Marker Message by:"+this.id);
            Buffer fromChannel = (Buffer) observable;
            //TODO: homework Record from Channel as Empty
            //TODO: add logic here so that if the marker comes back to the initiator then it should stop recording
            if (isFirstMarker()) {
                System.out.println("Recieved first Marker Message by:!"+this.id+" On: "+fromChannel.getLabel());
                this.channelMarkerCount.put(fromChannel,1);
                this.recordMyCurrentState();
                recordChannelAsEmpty(fromChannel);
                channelMarkerCount.put(fromChannel, channelMarkerCount.get(fromChannel) + 1); //don't need this
                for(Buffer incomingChannel:this.inChannels){
                    if(incomingChannel!=fromChannel){
                        recordChannel(incomingChannel);
                    }
                }
                Message markerMessage=new Message(MessageType.MARKER);

                if(ans ==0 ) {
                    ans = num;
                }

                for (Buffer outgoingChannel:this.outChannels){
                    sendMessgeTo(message,outgoingChannel);
                }


                //From the other incoming Channels (excluding the fromChannel which has sent the marker
                // startrecording messages
                //TODO: homework: Trigger the recorder thread from this processor so that it starts recording for each channel
                // Exclude the "Channel from which marker has arrived.


            } else {
                System.out.println("Recieved Duplicated Marker Message By:"+this.id+" On: "+fromChannel.getLabel());
                System.out.println("Stop Recording the channel"+fromChannel.getLabel()+"By Process:"+this.id);
                //this.channelMarkerCount.remove(fromChannel);
                //System.out.println("should be false!->"+this.channelMarkerCount.containsKey(fromChannel));
                //Means it isDuplicateMarkerMessage.
                //TODO: Homework Stop the recorder thread.
            }
            //TODO: Homework Send marker messages to each of the out channels
            // Hint: invoke  sendMessgeTo((Message) arg, outChannel) for each of the out channels

        }
        else{
            if (message.getMessageType().equals(MessageType.ALGORITHM)) {
                System.out.println("Processing Algorithm message....");
                num++;
            }  //There is no other type

        }


    }

    /**
     * 1. Record current state (done)
     * 2. Send Marker message on all output channels (done)
     * 3. Record incoming channels
     */
    public void initiateSnapShot() throws InterruptedException {
        System.out.println("In initiate snapshot");
        recordMyCurrentState();
        //TODO: Follow steps from Chandy Lamport algorithm. Send out a marker message on outgoing channel
        //[Hint: Use the sendMessgeTo method
        Message markerMessage=new Message(MessageType.MARKER);
        for(Buffer inChannel:this.inChannels){
            //System.out.println("Recording incoming channels"+inChannel.getLabel());
            this.recordChannel(inChannel);
        }

        if(ans == 0){
            ans = num;
        }

        //Send marker message to all outgoing channels.
        for(Buffer outChannel: this.outChannels){
            System.out.println("Sending MarkerMessage to:"+outChannel.getLabel());
            this.channelMarkerCount.put(outChannel,1);
            sendMessgeTo(markerMessage,outChannel);
            Thread.sleep(4000);


        }


        //TODO: homework Start recording on each of the input channels
    }

//Methods added 20171011

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

        System.out.println("Specified channel not found");
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
