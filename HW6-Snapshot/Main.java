import java.util.ArrayList;
import java.util.*;


/**
 * Created by tphadke on 9/27/17.
 */
public class Main {
    ArrayList<Processor> processorList;
    Processor processor1,processor2,processor3;
    Algorithm algo;

    Main(){
        init();
    }

    private void init() {
        processorList=new ArrayList<>();

        //Channels from P3 to P1 and P2 to P1
        Buffer channelP31 = new Buffer("31");
        Buffer channelP21 = new Buffer("21");

        //Channels from P3 to P2 and P1 to P2
        Buffer channelP32 = new Buffer("32"); //source destination
        Buffer channelP12 = new Buffer("12"); //source Dest

        //Channels from P2 to P3 and P1 to P3
        Buffer channelP23 = new Buffer("23");
        Buffer channelP13 = new Buffer("13");


        List<Buffer> inChannelsP1 = new ArrayList<>();
        inChannelsP1.add(channelP21);
        inChannelsP1.add(channelP31);
        List<Buffer> outChannelsP1 = new ArrayList<>();
        outChannelsP1.add(channelP12);
        outChannelsP1.add(channelP13);
        this.processor1 = new Processor(1, inChannelsP1, outChannelsP1); //Only observes in channels.
        processorList.add(processor1);

        List<Buffer> inChannelsP2 = new ArrayList<>();
        inChannelsP2.add(channelP12);
        inChannelsP2.add(channelP32);
        List<Buffer> outChannelsP2 = new ArrayList<>();
        outChannelsP2.add(channelP21);
        outChannelsP2.add(channelP23);
        this.processor2 = new Processor(2, inChannelsP2, outChannelsP2); //Only observes in channels.
        processorList.add(processor2);


        List<Buffer> inChannelsP3 = new ArrayList<>();
        inChannelsP3.add(channelP13);
        inChannelsP3.add(channelP23);
        List<Buffer> outChannelsP3 = new ArrayList<>();
        outChannelsP3.add(channelP31);
        outChannelsP3.add(channelP32);
        this.processor3 = new Processor(3, inChannelsP3, outChannelsP3); //Only observes in channels.
        processorList.add(processor3);

        //added the following to spawn algorithm threads
        this.algo = new Algorithm(processor1, processor2, processor3);
        Runnable r1 = new Executor(algo, algo.processor1);
        Runnable r2 = new Executor(algo, algo.processor2);
        Runnable r3 = new Executor(algo, algo.processor3);

        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        Thread t3 = new Thread(r3);

        t1.start();
        t2.start();
        t3.start();
    }

    void printRecordedChannelMessages(Processor p){
        for(Map.Entry<Buffer,List<Message>> entry : p.channelState.entrySet()){
            String lab = entry.getKey().getLabel();
            System.out.print("For channel "+ lab+": ");
            for(Message mm : entry.getValue()){
                System.out.print(mm.getMessageType().toString()+" ");
            }
            System.out.println();
        }
    }

    void printProcessorState(Processor p){
        System.out.println("Processor "+p.getId()+": Recorded State:" + p.ans);
    }

    public void printSnapshot(){
        synchronized (this){
            System.out.println("----------------------SNAPSHOT RESULTS----------------------------------------------");
            for(Processor p: this.processorList){
                printProcessorState(p);
            }
            for(Processor p: this.processorList){
                printRecordedChannelMessages(p);
            }
            System.out.println("------------------------------------------------------------------------------------");
        }
    }

    public void printMessageBuffers(Processor p){
        for(Buffer inb: p.inChannels){
            System.out.println("\n"+inb.getLabel()+" \nStart :"+inb.startRecord+ " \nStop: "+inb.stopRecord);
            for(int i=0;i<inb.getTotalMessageCount();i++)
                System.out.printf(inb.getMessage(i).messageType.toString()+" , ");
        }
    }



    public static void main(String args[]) throws InterruptedException {
        Main m=new Main();
        System.out.println("Initiating Snapshot");
        Thread.sleep(10);
        m.processor1.initiateSnapShot();
        m.printSnapshot();
        System.exit(0);

    }


}
