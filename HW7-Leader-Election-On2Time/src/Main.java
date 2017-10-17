/**
 * Leader Election in Asyn Ring O(n^2) Algorithm
 * CS 249 Team #2 Rashmeet Khanuja, Anusha Vijay, Steven Yen
 */

public class Main {

    public static void main(String[] args){

        Buffer C14 = new Buffer("C14");
        Buffer C43 = new Buffer("C43");
        Buffer C32 = new Buffer("C32");
        Buffer C21 = new Buffer("C21");

        Processor P1 = new Processor(1,C21,C14);
        Processor P2 = new Processor(2,C32,C21);
        Processor P3 = new Processor(3,C43,C32);
        Processor P4 = new Processor(4,C14,C43);

        //Each processor sends a message with its identifier to its left neighbor
        P4.sendMessageToMyInBuffer(new Message(MessageType.IDENTIFIER,1));
        P1.sendMessageToMyInBuffer(new Message(MessageType.IDENTIFIER,2));
        P2.sendMessageToMyInBuffer(new Message(MessageType.IDENTIFIER,3));
        P3.sendMessageToMyInBuffer(new Message(MessageType.IDENTIFIER,4));

        //Check who is leader
        printProcInfo(P1);
        printProcInfo(P2);
        printProcInfo(P3);
        printProcInfo(P4);
    }

    /**
     * Helper method for printing out info about each processor
     * @param p processor whose info we want to print.
     */
    public static void printProcInfo(Processor p){
        System.out.printf("Processor P%d ",p.getProcId());
        System.out.println("is leader? " + p.getIsLeader());
    }

}
