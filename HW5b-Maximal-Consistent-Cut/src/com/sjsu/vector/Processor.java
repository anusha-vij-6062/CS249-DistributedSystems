package com.sjsu.vector;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * CS249 Maximal Consistent Cut Program
 * A skeleton code was provided on which we built-upon
 * Performs all the processor related tasks
 * @author Rashmeet Khanuja, Anusha Vijay, Steven Yen
 * @version 1.0
 *
 */
public class Processor implements Observer {
    Buffer messageBuffer ;
    Integer id ;
    VectorClock vc ; //This is the current vector clock
    ArrayList<int[]> store; //storing the vc at each computation event (1-indexed, not 0-indexed)

    /**
     * Initializes the processor with id, children and unexplored lists. Adds himself in the observers list.
     * @param id of the processor
     */
    public Processor(int id, int totalProcesors) {
        messageBuffer = new Buffer();
        this.id = id;
        messageBuffer.addObserver(this);
        vc=new VectorClock(totalProcesors);
        store = new ArrayList<int[]>();
        store.add(new int[]{Integer.MIN_VALUE,Integer.MIN_VALUE}); //adding dummy 0th entry
    }

    /**
     * Print the indexed vector clock in the store
     * @param index number corresponding to the computation event we're interested in
     */
    public void printStore(int index) {
        System.out.println(store.get(index)[0]+" "+store.get(index)[1]);
    }

    /**
     * Loops through store array and print each vector clock
     */
    public void printStore() {
        System.out.print("[");
        //start from 1 b/c the 0th one is dummy
        for (int i=1;i<this.store.size();i++){
            int[] item = this.store.get(i);
            System.out.print("<"+item[0]);
            System.out.print(", ");
            System.out.print(item[1]+">");
            System.out.print(",\t");
        }
        System.out.print("]");
    }

    /**
     * Getter for the id of this process
     * @return process id as integer
     */
    public Integer getId(){
        return id;
    }

    /**
     * Sets the message in the messageBuffer of this processor
     * @param message the messagae to be set
     * @param sender the processor that send message to my (this processor's) buffer
     */
    public void sendMessageToMyBuffer(Message message, Processor sender) throws InterruptedException {
        System.out.printf("P%d send %s message to P%d %n",sender.id, message.messageType, this.id);
        this.messageBuffer.setMessage(message,sender);
    }

    /**
     * Gets called when a node receives a message in its buffer
     * Processes the message received in the buffer
     */
    synchronized public void update(Observable observable, Object arg) {
        try {
            Message text = messageBuffer.getMessage();
            calculateVectorClocks(text);
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method calculates the vector clock after receiving a message in the buffer
     * since this method is in Processor, it can directly update the "vc" field, so this
     * method does not need a return type (Can just be void).
     * @param recievedMessage
     * @throws InterruptedException
     */
    public void calculateVectorClocks(Message recievedMessage) throws InterruptedException {

        this.vc.incrementAt(id);

        if(recievedMessage.messageType==MessageType.RECIEVE){
	        for(int i=0; i< this.vc.vc.length; i++){
	        	if(i != this.getId()){
	        		if(this.vc.vc[i]<recievedMessage.vc.vc[i]){
	        			this.vc.updateAt(i,recievedMessage.vc.vc[i]);
	        		}
	        	}
	        }
        }

        System.out.printf("  VC of P%d updated to: ", this.getId());
        this.store.add(this.vc.vc.clone());
        this.vc.printVC();
    }

    /**
     * Calculate the maximal cut's entry corresponding to this processor
     * @param k a cut specified as an array. 0th index correspond to proc 0, 1st to proc 1, etc.
     * @return m' value corresponding to the entry of the maximal consistent cut corresponding to this proc.
     */
    public int calculateMaximalCut(int[] k){
        //cut ={1,5}
        VectorClock cut=new VectorClock(k);
        System.out.print("\nStarting access from:");
        this.printStore(k[this.id]);
        int m = k[this.id];
        int mp=-1;
        //compare 1,5 with 4,6, then 1,5 with 1,5 then with 1,4
        //{4,6}

        for (int i=m;i>=0;i--){
            System.out.print("Compared to ");
            printStore(i);

            if(cut.compareTo(store.get(i))>=0){
                mp=i;
                break;
            }
            else{
                System.out.println("Passed");
            }
        }
        return mp;
    }

}
