package com.sjsu.vector;

import static com.sjsu.vector.MessageType.RECIEVE;

/**
 * CS249 VectorClock Program
 * A skeleton code was provided on which we built-upon
 * @author Rashmeet Khanuja, Anusha Vijay, Steven Yen
 *
 */
public class Algorithm {
    int noOfProcessors;
    Processor p0, p1, p2;

    public Algorithm() {
        super();
        this.noOfProcessors = 3;
        // TODO : initialize all the processors
        p0 = new Processor(0, 3);
        p1 = new Processor(1, 3);
        p2 = new Processor(2, 3);
    }

    // Write hard coded execution plan for processors
    public void executionPlanForP0() throws InterruptedException {
        //sample plan from slides

        Thread.sleep(200);
        this.send(p1, p0);

        Thread.sleep(500);
        this.send(p2, p0); //made this smaller

        Thread.sleep(400);
        this.compute(p0, new Message(MessageType.COMPUTATION, p0.vc.clone()));

        Thread.sleep(300);
        //this.receive(p0, p1);

        Thread.sleep(400);
        this.compute(p0, new Message(MessageType.COMPUTATION, p0.vc.clone()));

        Thread.sleep(400);
        synchronized (this) {
            System.out.println("\n------The final VECTOR CLOCK for process 0 is---");
            p0.vc.printVC();
        }

    }

    // Write hard coded execution plan for processors
    public void executionPlanForP1() throws InterruptedException {
        //sample plan from slides
        Thread.sleep(400);
        //this.receive(p1, p0);

        Thread.sleep(200);
        //this.receive(p1, p2); //phi7

        Thread.sleep(300); //phi8
        this.send(p2, p1);

        Thread.sleep(300); //phi9 (p1 recieve from p2, then sends m to p1)
        this.recieveAndSend(p0, p1);

        Thread.sleep(600);
        synchronized (this) {
            System.out.println("\n----------The final VECTOR CLOCK for process 1 is----");
            p1.vc.printVC();
        }
    }

    public void executionPlanForP2() throws InterruptedException {

        Thread.sleep(100);
        this.compute(p2, new Message(MessageType.COMPUTATION, p2.vc.clone()));

        Thread.sleep(200); //phi11
        this.compute(p2, new Message(MessageType.COMPUTATION, p2.vc.clone()));

        Thread.sleep(200); //phi12
        this.send(p1, p2);

        Thread.sleep(300); //phi13
        //this.receive(p2, p0);

        Thread.sleep(100); //phi14
        this.send(p1, p2);

        Thread.sleep(300); //phi15
        //this.receive(p2, p1);

        Thread.sleep(300); //phi16
        this.compute(p2, new Message(MessageType.COMPUTATION, p2.vc.clone()));

        Thread.sleep(600);
        synchronized (this) {
            System.out.println("\n----------The final VECTOR CLOCK for process 2 is----");
            p2.vc.printVC();
        }
    }
    private void compute(Processor p, Message computeMessage) throws InterruptedException {
        p.sendMessageToMyBuffer(computeMessage, p);
    }
    /**
     * Overloaded version of send with variable delay
     *
     * @param to
     * @param from
     * @throws InterruptedException
     */
    private void send(Processor to, Processor from) throws InterruptedException {
        from.calculateVectorClocks(new Message(MessageType.SEND, from.vc.clone()));
        Message recieveMessage = new Message(RECIEVE, from.vc.clone());
        to.sendMessageToMyBuffer(recieveMessage, from);

    }

    synchronized private void recieveAndSend(Processor sendTo, Processor sendFrom) throws InterruptedException {
        Message recieveMessage = new Message(RECIEVE, sendFrom.vc.clone());
        sendTo.sendMessageToMyBuffer(recieveMessage, sendFrom);
    }
}
