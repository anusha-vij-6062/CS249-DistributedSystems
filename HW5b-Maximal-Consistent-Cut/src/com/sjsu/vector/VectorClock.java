package com.sjsu.vector;
/**
 * Maximal Consistent Cut Program
 * @author Rashmeet Khanuja, Anusha Vijay, Steven Yen
 * Skeleton code provided on which we built-upon.
 */
public class VectorClock implements Comparable<VectorClock>{
    int[] vc;

    public VectorClock(int noOfProcesses ){
        vc = new int [noOfProcesses];
    }

    public VectorClock(){
    }

	/**
	* Create a copy of vector clock with it's own vc array,
	* that is not simply a reference to the one passed in.
	*/
    public VectorClock clone(){
        VectorClock newCopy = new VectorClock();
        newCopy.vc=this.vc.clone();
        return newCopy;
    }

    public VectorClock(int[] k){
        vc=new int[2];
        this.vc=k;
    }

    @Override
    public int compareTo(VectorClock o) {
    //implement a compare to method that will compare two vector clocks
    //check for equality
    // 0= equal
    // 1= greater
    // -1= smaller
    	boolean isEqual = true;
        for(int i=0; i<o.vc.length;i++){
    		if(this.vc[i]!=o.vc[i]){
    			isEqual = false;
    			break;
    		}
    	}
        
    	if(isEqual)
    	    return 0;

    	boolean isSmaller = true;
    	for(int i=0; i<o.vc.length;i++){
    		if(this.vc[i]>o.vc[i]){
    			isSmaller = false;
    			break;
    		}
    	}
    	if(isSmaller){
    		return -1;
    	}else{
    		return 1;
    	}    	
    }

	/**
	* Overloaded version of above method that takes
	* in an int[] array as a parameter.
	*/
    public int compareTo(int[] arr){
        VectorClock otherVC= new VectorClock(arr);
        return this.compareTo(otherVC);
    }

    /**
     * Based on a event vector clock will be incremented, changed or updated.
     * Which index should be updated will be decided by a processor
     * @param index
     * @param value
     */
    public void updateAt(int index, int value){
        vc[index]= value;
    }

    public void incrementAt(int index) {
        vc[index]+=1;
    }

    public void printVC(){
    	System.out.print("[");
        for (int i=0;i<this.vc.length;i++){
            System.out.print(this.vc[i]);
            System.out.printf(" ");
        }
        System.out.println("]");
    }

}
