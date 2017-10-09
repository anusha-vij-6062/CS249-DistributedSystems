package com.sjsu.vector;

/**
 * CS249 Maximal Consistent Cut Program
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
    	
    	Thread.sleep(200);
    	this.send(p1, p0, 400);
    	
    	Thread.sleep(300);
    	this.compute(p0, new Message(MessageType.COMPUTATION, p0.vc.clone()));
    	
    	Thread.sleep(300);
    	this.compute(p0, new Message(MessageType.COMPUTATION, p0.vc.clone()));
    	
    	Thread.sleep(100);
    	this.send(p1, p0, 100);
    	
        Thread.sleep(1700);
        synchronized (this) {
            System.out.println("\n------------------Process 0---------------------");
            System.out.print("The store array is: ");
            p0.printStore();
            System.out.println("\n------------------------------------------------");
        }

    }

    // Write hard coded execution plan for processors
    public void executionPlanForP1() throws InterruptedException {
        
    	Thread.sleep(100);
    	this.compute(p1, new Message(MessageType.COMPUTATION, p1.vc.clone()));
    	
    	Thread.sleep(200);
    	this.compute(p1, new Message(MessageType.COMPUTATION, p1.vc.clone()));
    	
    	Thread.sleep(100);
    	this.compute(p1, new Message(MessageType.COMPUTATION, p1.vc.clone()));
    	
    	Thread.sleep(200);
    	//this.receive(p1,p0);
		//we opted not to use receive() method b/c wait is not needed when we
		//give  p1 enough time to recieve message by using thread.sleep.
    	
    	Thread.sleep(100);
    	this.compute(p1, new Message(MessageType.COMPUTATION, p1.vc.clone()));
    	
    	Thread.sleep(300);
    	//this.receive(p1,p0);
		//see above comment regarding receive().
    	
        Thread.sleep(2400);
        synchronized (this) {
            System.out.println("\n-------------------Process 1--------------------");
            System.out.print("The store array is: ");
            p1.printStore();
            System.out.println("\n------------------------------------------------");
        }
    }

    private void compute(Processor p, Message computeMessage) throws InterruptedException {
        p.sendMessageToMyBuffer(computeMessage, p);
    }

    /**
     * Simulates a send event
     * @param to the process receiving the message
     * @param from the process sending the message
     * @throws InterruptedException
     */
    private void send(Processor to, Processor from) throws InterruptedException {
        //we refrained from setting the SEND message and just call calculateVectorClocks
        //here in the send method because we don't want SEND messages to override other
        //messages that might be in the buffer. Also, we didn't really see a need for the
        //sender to send a SENT message to it's own buffer.
        from.calculateVectorClocks(new Message(MessageType.SEND, from.vc.clone()));
        Message recieveMessage = new Message(MessageType.RECIEVE, from.vc.clone());
        to.sendMessageToMyBuffer(recieveMessage, from);

    }

    /**
     * Overloaded version of the send method that allows variable delay.
     * This version of send can simulate the send delay due to channel speed.
     * @param to the process receiving the message
     * @param from the process sending the message
     * @param delay milliseconds that elapses between sender sends and receiver receives.
     * @throws InterruptedException
     */
    private void send(Processor to, Processor from, int delay) throws InterruptedException{
        //we refrained from setting the SEND message and just call calculateVectorClocks
        //here in the send method because we don't want SEND messages to override other
        //messages that might be in the buffer. Also, we didn't really see a need for the
        //sender to send a SENT message to it's own buffer.
        from.calculateVectorClocks(new Message(MessageType.SEND, from.vc.clone()));

        //Spawn an new thread that will model the send delay by setting the message
        //after the specified delay.
        final int dd = delay;
        final Processor fromF = from;
        final Processor toF = to;

        Thread sendThread = new Thread(){

            public void run(){
                Message recieveMessage = new Message(MessageType.RECIEVE,fromF.vc.clone());
                try {
                    Thread.sleep(dd);
                    toF.sendMessageToMyBuffer(recieveMessage,fromF);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        };
        sendThread.start();
    }

    /**
     * Special event where recieve and send is combined.
     * There's no such event in the example input for this program.
     * @param sendTo
     * @param sendFrom
     * @throws InterruptedException
     */
    synchronized private void recieveAndSend(Processor sendTo, Processor sendFrom) throws InterruptedException {
        Message recieveMessage = new Message(MessageType.RECIEVE, sendFrom.vc.clone());
        sendTo.sendMessageToMyBuffer(recieveMessage, sendFrom);
    }

}
