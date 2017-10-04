package com.sjsu.vector;

public class VectorClock implements Comparable<VectorClock>{
    int[] vc;

    public VectorClock(int noOfProcesses ) {
        vc = new int [noOfProcesses];
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
    //implement a compare to method that will compare two vector clocks
    //check for equality
    	boolean isEqual = true;
        for(int i=0; i<o.vc.length;i++){
    		if(this.vc[i]!=o.vc[i]){
    			isEqual = false;
    		}
    	}
        
    	if(isEqual) return 0;
    	
    	//check if greater/less than
    	boolean isSmaller = true;
    	for(int i=0; i<o.vc.length;i++){
    		if(this.vc[i]>o.vc[i]){
    			isSmaller = false;
    		}
    	}
    	if(isSmaller){
    		return -1;
    	}else{
    		return 1;
    	}    	
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
    	System.out.print("[");
        for (int i=0;i<this.vc.length;i++){
            System.out.print(this.vc[i]);
            System.out.printf("\t");
        }
        System.out.println("]");
    }

}
