package com.sjsu.vector;

/**
 * CS249 VectorClock Program
 * A skeleton code was provided on which we built-upon
 * @author Rashmeet Khanuja, Anusha Vijay, Steven Yen
 *
 */
public class Algorithm {
    int noOfProcessors;
    Processor p0, p1;

    public Algorithm() {
        super();
        this.noOfProcessors = 2;
        // TODO : initialize all the processors
        p0 = new Processor(0, 2);
        p1 = new Processor(1, 2);

    }

    // Write hard coded execution plan for processors
    public void executionPlanForP0() throws InterruptedException {
        //sample plan from slides
    	
    	Thread.sleep(350);
    	this.send(p1, p0);
    	
    	Thread.sleep(300);
    	this.compute(p0, new Message(MessageType.COMPUTATION, p0.vc.clone()));
    	
    	Thread.sleep(300);
    	this.compute(p0, new Message(MessageType.COMPUTATION, p0.vc.clone()));
    	
    	Thread.sleep(100);
    	this.send(p1, p0);
    	
        Thread.sleep(600);
        synchronized (this) {
            System.out.println("\n------The final VECTOR CLOCK for process 0 is---");
            p0.vc.printVC();
            p0.printStore();
        }

    }

    // Write hard coded execution plan for processors
    public void executionPlanForP1() throws InterruptedException {
        
    	Thread.sleep(100);
    	this.compute(p1, new Message(MessageType.COMPUTATION, p1.vc.clone()));
    	
    	Thread.sleep(100);
    	this.compute(p1, new Message(MessageType.COMPUTATION, p1.vc.clone()));
    	
    	Thread.sleep(100);
    	this.compute(p1, new Message(MessageType.COMPUTATION, p1.vc.clone()));
    	
    	Thread.sleep(300);
    	//this.receive(p1,p0);
    	
    	Thread.sleep(100);
    	this.compute(p1, new Message(MessageType.COMPUTATION, p1.vc.clone()));
    	
    	Thread.sleep(300);
    	//this.receive(p1,p0);
    	
        Thread.sleep(1400);
        synchronized (this) {
            System.out.println("\n----------The final VECTOR CLOCK for process 1 is----");
            p1.vc.printVC();
            p1.printStore();
            int[] cut = {0,2};
            p1.calculateMaximalCut(cut);

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
        Message recieveMessage = new Message(MessageType.RECIEVE, from.vc.clone());
        to.sendMessageToMyBuffer(recieveMessage, from);

    }

    synchronized private void recieveAndSend(Processor sendTo, Processor sendFrom) throws InterruptedException {
        Message recieveMessage = new Message(MessageType.RECIEVE, sendFrom.vc.clone());
        sendTo.sendMessageToMyBuffer(recieveMessage, sendFrom);
    }
}
