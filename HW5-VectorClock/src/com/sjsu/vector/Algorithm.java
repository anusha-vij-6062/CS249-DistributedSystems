package com.sjsu.vector;

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
        this.send(p1, p0,200); //phi1

        Thread.sleep(500);
        this.send(p2, p0,100); //phi2

        Thread.sleep(400);
        this.compute(p0, new Message(MessageType.COMPUTATION, p0.vc.clone())); //phi3

        Thread.sleep(300);
        //this.receive(p0, p1); //phi4
		//receive event is simply modeled by update() method of observer.

        Thread.sleep(200); 
        this.compute(p0, new Message(MessageType.COMPUTATION, p0.vc.clone())); //phi5

        Thread.sleep(600);
        synchronized (this) {
            System.out.println("\n------The final VECTOR CLOCK for process 0 is---");
            p0.vc.printVC();
        }

    }

    // Write hard coded execution plan for processors
    public void executionPlanForP1() throws InterruptedException {
        
        Thread.sleep(400);
        //this.receive(p1, p0); //phi6

        Thread.sleep(200);
        //this.receive(p1, p2); //phi7

        Thread.sleep(400); //phi8
        this.send(p2, p1,200);

        Thread.sleep(400); //phi9 (p1 recieve from p2, then sends m to p1)
        this.recieveAndSend(p0, p1, 100);

        Thread.sleep(1200);
        synchronized (this) {
            System.out.println("\n----------The final VECTOR CLOCK for process 1 is----");
            p1.vc.printVC();
        }
    }

    public void executionPlanForP2() throws InterruptedException {

        Thread.sleep(100); //phi10
        this.compute(p2, new Message(MessageType.COMPUTATION, p2.vc.clone()));

        Thread.sleep(200); //phi11
        this.compute(p2, new Message(MessageType.COMPUTATION, p2.vc.clone()));

        Thread.sleep(200); //phi12
        this.send(p1, p2,100);

        Thread.sleep(300); //phi13
        //this.receive(p2, p0);

        Thread.sleep(100); //phi14
        this.send(p1, p2, 400);

        Thread.sleep(300); //phi15
        //this.receive(p2, p1);

        Thread.sleep(300); //phi16
        this.compute(p2, new Message(MessageType.COMPUTATION, p2.vc.clone()));

        Thread.sleep(1100);
        synchronized (this) {
            System.out.println("\n----------The final VECTOR CLOCK for process 2 is----");
            p2.vc.printVC();
        }
    }
	
	/**
	* Method that simulates an internal computation event.
	* by sending a COMPUTE message to this processor's buffer.
	*/
    private void compute(Processor p, Message computeMessage) throws InterruptedException {
        p.sendMessageToMyBuffer(computeMessage, p);
    }
	
    /**
     * send event from one process to another
     * @param to
     * @param from
     * @throws InterruptedException
     */
    private void send(Processor to, Processor from) throws InterruptedException {
        from.calculateVectorClocks(new Message(MessageType.SEND, from.vc.clone()));
        Message recieveMessage = new Message(MessageType.RECIEVE, from.vc.clone());
        to.sendMessageToMyBuffer(recieveMessage, from);

    }
	
	/**
     * Overloaded version of send() with variable delay
     * @param to
     * @param from
     * @param delay milliseconnd to wait before setting message (models send delay)
     * @throws InterruptedException
     */
    synchronized private void send(Processor to, Processor from, int delay) throws InterruptedException {
        
		//we refraimed from setting the SEND message and just call calculateVectorClocks
		//here in the send method because we don't wanat SEND messages to override other
		//messages. Additionally sender doesn't really need to set SEND to it's own buffer.
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        	
			}
			
		};
		sendThread.start();
    }

	/**
	* Method used to simulate event phi9, which receives and sends in one event
	* @param sendTo processor that current process will send to
	* @param sendFrom the processor that just recieved a message and is sending to "sendTo"
	*/
    synchronized private void recieveAndSend(Processor sendTo, Processor sendFrom,int delay) throws InterruptedException {
		final int dd = delay;
		final Processor fromF = sendFrom;
		final Processor toF = sendTo;
		
		Thread sendThread = new Thread(){
			
			public void run(){
				Message recieveMessage = new Message(MessageType.RECIEVE,fromF.vc.clone());
				try {
					Thread.sleep(dd);
					toF.sendMessageToMyBuffer(recieveMessage,fromF);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        	
			}
			
		};
		sendThread.start();
		
    }
}
