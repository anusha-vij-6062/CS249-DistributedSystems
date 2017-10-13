import java.util.ArrayList;

public class ChannelRecorder implements Runnable {
    Buffer channel;
    Processor processor;
    String label;

    /***
     * Constructor
     * @param channel
     * @param processor
     */
    public ChannelRecorder(Buffer channel, Processor processor) {
        this.channel = channel;
        this.processor = processor;
        this.label = channel.getLabel();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted())
            ;
        this.processor.channelState.put(channel, new ArrayList<Message>(channel.getMessage().subList(channel.startRecord, channel.stopRecord - 1)));
        System.out.println("Stop Recording Channel:" + this.channel.getLabel());

    }
}

