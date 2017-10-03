package com.sjsu.vector;

public class VectorClock implements Comparable<VectorClock>{
    //TODO: read up how to use a comparable and a comparator
    //TODO: Do you see an advantage in making it an Integer ??
    int[] vc;

    public VectorClock(int noOfProcesses ) {
        vc = new int [noOfProcesses];
        //TODO : Set all to 0.  Do you need to explicitly initilize to 0
    }
    public VectorClock(){

    }

    public VectorClock clone(){
        VectorClock newCopy = new VectorClock();
        newCopy.vc=this.vc.clone();
        return newCopy;
    }


    //Returns a negative integer, zero, or a positive integer as this object is less than, 
    //equal to, or greater than the specified object.
    @Override
    public int compareTo(VectorClock o) {
        // TODO implement a compare to method that will compare two vector clocks
        
    	for (int i = 0; i < o.vc.length; i++) {
            if (this.vc[i] < o.vc[i]) {
                updateAt(i, o.vc[i]);
                }
        }
        
        return 0;
    }
    /**
     * Based on a event vector clock will be incremented, changed or updated.
     * Which index should be updated will be decided by a processor
     * @param index
     * @param value
     */
    public void updateAt(int index, int value){
        vc[index]= value;
        //System.out.println("\nUpdated!");
    }

    public void incrementAt(int index)
    {
        vc[index]+=1;
        //System.out.println("\nIncremented!");
    }

    public void printVC(){
        for (int i=0;i<this.vc.length;i++){
            System.out.print(this.vc[i]);
            System.out.printf("\t");
        }
    }

}