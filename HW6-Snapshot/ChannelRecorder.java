import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ChannelRecorder implements Runnable{
    Buffer channel;
    Processor processor;
    String label;
    List<Message> messageSinceMarker;
    Boolean stopRecording;

    public ChannelRecorder(Buffer channel,Processor processor){
        this.channel=channel;
        this.processor=processor;
        this.label=channel.getLabel();
        messageSinceMarker=new ArrayList<>();
        stopRecording=false;
    }

    /*/Here print the value stored in the inChannels to stdout or file

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
    //
    */

    public void recordChannel() throws InterruptedException {
        System.out.println("\nRecording channel:"+this.channel.getLabel());
        channel.start=true;
        int lastIndex=channel.getTotalMessageCount();
        System.out.println("\nLastIndex:"+lastIndex);
        int currentIndex=lastIndex;
        while (!stopRecording){
            currentIndex=channel.getTotalMessageCount();
            if(lastIndex!=currentIndex){
                this.messageSinceMarker.add(channel.getMessage(currentIndex));
                lastIndex=currentIndex;

            }
        }
    }
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted())
            ;
        this.processor.channelState.put(channel, new ArrayList<Message>(channel.getMessage().subList(channel.startRecord, channel.stopRecord-1)));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!Thread is interrupted!!" + this.processor.id + "Pf" + this.channel.getLabel());

    }

    }

