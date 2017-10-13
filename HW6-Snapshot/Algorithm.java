

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
        this.processor1 = processor1;
        this.processor2 = processor2;
        this.processor3 = processor3;
    }

    public void executionPlanP1() throws InterruptedException {
        while (true) {
            this.send(processor2, processor1.getOutChannelByLabel("12"));
            this.compute(processor1);
            Thread.sleep(1);
            this.send(processor3, processor1.getOutChannelByLabel("13"));
            this.compute(processor1);
        }
    }

    public void executionPlanP2() throws InterruptedException {
        while (true) {
            this.send(processor1, processor2.getOutChannelByLabel("21"));
            this.compute(processor2);
            Thread.sleep(1);
            this.send(processor3, processor2.getOutChannelByLabel("23"));
            this.compute(processor2);
        }
    }


    public void executionPlanP3() throws InterruptedException{
        while (true) {
            this.send(processor1, processor3.getOutChannelByLabel("31"));
            Thread.sleep(2);
            this.send(processor2, processor3.getOutChannelByLabel("32"));
            this.compute(processor3);
        }

    }

    /***
     *
     * @param p Dummy class to perform some computation
     */

    public void compute(Processor p) {
        ;
    }

    public void send(Processor to, Buffer channel) {
        to.sendMessgeTo(new Message(MessageType.ALGORITHM), channel); // ALGORITHM
    }

}
