

/**
 * This is the simulation of a main algorithm that will run on processors P1, P2, P3
 * This could be a banking application, payroll application or any other distributed application
 */
public class Algorithm {

    /**
     * The processors which will participate in a distributed application
     */
    Processor processor1, processor2, processor3;

    public Algorithm(Processor processor1, Processor processor2, Processor processor3) {
        //TODO: Homeork: Initialize processors so that they represent the topology of 3 processor system

        this.processor1 = processor1;
        this.processor2 = processor2;
        this.processor3 = processor3;
    }

    public void executionPlanP1 () throws InterruptedException{

        Thread.sleep(200);
        this.send(processor2,processor1.getOutChannelByLabel("12")); //phi1

        Thread.sleep(500);
        this.send(processor3, processor1.getOutChannelByLabel("13")); //phi2

        Thread.sleep(400);
        this.compute(processor1); //phi3

        Thread.sleep(300);
        //this.receive(p0, p1); //phi4
        //receive event is simply modeled by update() method of observer.

        Thread.sleep(200);
        this.compute(processor1);//phi5

/**
 * TODO: Homework: Implement send message from processor1 to different processors. Add a time gap betweeen two different
 *                send events. Add computation events between two diferent sends.
 *      [Hint: Create a loop that kills time, sleep , wait on somevalue etc..]
 *
 */
        //processor1.sendMessgeTo(null,null);

        //processor1.sendMessgeTo(null,null);

        //processor1.sendMessgeTo(null,null);

    }

    // Write hard coded execution plan for processors
    public void executionPlanP2() throws InterruptedException{
        Thread.sleep(400);
        //this.receive(p1, p0); //phi6

        Thread.sleep(200);
        //this.receive(p1, p2); //phi7

        Thread.sleep(400); //phi8
        this.send(processor3,processor2.getOutChannelByLabel("23"));

        Thread.sleep(400); //phi9 (p1 recieve from p2, then sends m to p1)
        this.send(processor1,processor2.getOutChannelByLabel("21"));

    }

    // Write hard coded execution plan for processors
    public void executionPlanP3() throws InterruptedException{
        Thread.sleep(100); //phi10
        this.compute(processor3);

        Thread.sleep(200); //phi11
        this.compute(processor3);

        Thread.sleep(200); //phi12
        this.send(processor2,processor3.getOutChannelByLabel("32"));

        Thread.sleep(300); //phi13
        //this.receive(p2, p0);

        Thread.sleep(100); //phi14
        this.send(processor2,processor3.getOutChannelByLabel("32"));

        Thread.sleep(300); //phi15
        //this.receive(p2, p1);

        Thread.sleep(300); //phi16
        this.compute(processor3);

    }

    /**
     * A dummy computation.
     * @param p
     */
    public void compute(Processor p) {
        System.out.println("Doing some computation on " + p.getClass().getSimpleName());
    }

    /**
     *
     * @param to processor to which message is sent
     * @param channel the incoming channel on the to processor that will receive this message
     */
    public void send(Processor to, Buffer channel) {
        to.sendMessgeTo(new Message(MessageType.ALGORITHM), channel); // ALGORITHM
    }

}
