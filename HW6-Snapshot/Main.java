import java.util.ArrayList;
import java.util.List;

/**
 * Created by tphadke on 9/27/17.
 */
public class Main {

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

        Thread.sleep(3000);

        processor1.initiateSnapShot();

        System.out.println("processor1.ans = " + processor1.ans);
        System.out.println("processor2.ans = " + processor2.ans);
        System.out.println("processor3.ans = " + processor3.ans);

    }


}
