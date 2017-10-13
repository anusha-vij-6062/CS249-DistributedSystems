import java.util.ArrayList;
import java.util.*;


/**
 * Created by tphadke on 9/27/17.
 */
public class Main {

    public static void printRecordedChannelMessages(Processor p){
        for(Map.Entry<Buffer,List<Message>> entry : p.channelState.entrySet()){
            String lab = entry.getKey().getLabel();
            System.out.print("For channel "+ lab+": ");
            for(Message mm : entry.getValue()){
                System.out.print(mm.getMessageType().toString()+" ");
            }
            System.out.println();

        }
    }

    public static void printProcessorState(Processor p){
        System.out.println("Processor "+p.getId()+": Recorded State:" + p.ans);
    }




    public static void main(String args[]) throws InterruptedException {

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
        Processor processor1 = new Processor(1, inChannelsP1, outChannelsP1); //Only observes in channels.

        List<Buffer> inChannelsP2 = new ArrayList<>();
        inChannelsP2.add(channelP12);
        inChannelsP2.add(channelP32);
        List<Buffer> outChannelsP2 = new ArrayList<>();
        outChannelsP2.add(channelP21);
        outChannelsP2.add(channelP23);
        Processor processor2 = new Processor(2, inChannelsP2, outChannelsP2); //Only observes in channels.


        List<Buffer> inChannelsP3 = new ArrayList<>();
        inChannelsP3.add(channelP13);
        inChannelsP3.add(channelP23);
        List<Buffer> outChannelsP3 = new ArrayList<>();
        outChannelsP3.add(channelP31);
        outChannelsP3.add(channelP32);
        Processor processor3 = new Processor(3, inChannelsP3, outChannelsP3); //Only observes in channels.

        /**
         * Choose one processor to initiale a snapshot. Please note that any processor has the capability to
         * initiate a snapshot.
         * //TODO: Homework: initiate snapshot
         * [Hint: call the initiateSnapshot method ]
         */

        //added the following to spawn algorithm threads
        Algorithm algo = new Algorithm(processor1, processor2, processor3);
        Runnable r1 = new Executor(algo, algo.processor1);
        Runnable r2 = new Executor(algo, algo.processor2);
        Runnable r3 = new Executor(algo, algo.processor3);

        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        Thread t3 = new Thread(r3);

        t1.start();
        t2.start();
        t3.start();
        Thread.sleep(200);

        processor1.initiateSnapShot();
        System.out.println("----------------------3----------------------------------------------");
        Thread.sleep(100);
//        t1.interrupt();
//        t2.interrupt();
//        t3.interrupt();
        Thread.sleep(100);

        printProcessorState(processor1);
        printProcessorState(processor2);
        printProcessorState(processor3);
        printRecordedChannelMessages(processor1);
        printRecordedChannelMessages(processor2);
        printRecordedChannelMessages(processor3);
        System.out.println("----------------------3----------------------------------------------");
        System.exit(0);

    }


}
