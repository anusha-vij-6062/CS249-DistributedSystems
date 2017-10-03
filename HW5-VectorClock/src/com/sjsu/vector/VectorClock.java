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
    
    /**
     * Compares the calling vector clock (this) with the vc o
     * @return 0 if this vc is equal to o; -1 if this vc<o; +1 if this vc>o
     * @param o the other vector clock "this" vc will be compared to
     */
    @Override
    public int compareTo(VectorClock o) {
        // TODO implement a compare to method that will compare two vector clocks
        
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
        for (int i=0;i<this.vc.length;i++){
            System.out.print(this.vc[i]);
            System.out.printf("\t");
        }
    }

}