# CS249_HW4_Convergecast

###Prerequisites
- java version "1.8.0_144"
- Java(TM) SE Runtime Environment (build 1.8.0_144-b01)

###Synopsis
This program finds the max value in a binary tree. It does so by recursively calling the findmax algorithm, which bottoms out (base case) when a leave node that has no children (other then null) is reached. In this base case, the leave nodes would return its own value as the max (since the subtree rooted at a leave node has just one value). Internal nodes determine the max value in the subtree rooted at them by calling the FindMax() method recursively on its left and right child. As such, each node passes up to its parent the max value observed in the subtree rooted at that node when the method call on it (i.e. FindMax(node.left) or FindMax(node.right)) returns. Additionally, in our implmenetation, the Node class also has a field called "max_at_subtree" that is used by each node to keep track of the max value observed in the subtree rooted at it. The max values are passed up and evaluated in a Convergecast manner, where the node furthest from the root (the leaf nodes) learns the value first, then nodes closer and closer to the root learns their value, and the root node learns the max value in the end. 

The method forming the string concatenation of node values is similar. Instead of calculating max nodes, each node would pass up to its parent a string concatenation that includes the values of all the nodes in the subtree rooted at that node. After the recursion bottoms out at the base case, the string concatenations are progressivly passed up to their parents (from the leaf nodes toward the root node) in a convergecast manner.

###Running the program:
1. Save all of the source code files (BinaryTree.java and Main.java) to the same directory.
2. In the OS command line interface (terminal), change directory (cd) to the directory containing all the source codes.
3. Run the command "javac *.java" to compile all source code in the directory.
4. After compilation, run the command "java Main" (Main class contains the main method) to run the code. This should print the final results stored at the root node. 

###Input
A binary tree is given with the assignment to be used as the input. The init() method within the Main class is used to initialize this tree. The tree is given as follows
          2
         / \
    	/   \
       7     5
      / \     \
	 /   \     \
	2     6  	9
         / \    /
    	/   \  4
       5	11

For ease of reference, the nodes are labeled alphabetically as follows:
          na
         / \
    	/   \
       nb     nc
      / \     \
	 /   \     \
	nd    ne  	nf
         / \    /
    	/   \  ni
       ng	nh	   
	   	   
###Output

Root has max value from all the nodes, and it is: 11
Root has the values of all the nodes concatenated, and it is 2  7  5  6  11  2  5  4  9

###Below is a detailed print out of the final values of "max_at_subtree" and "buffer" for each node after the algorithm runs:
max of subtree rooted at na is 11
max of subtree rooted at nb is 11
max of subtree rooted at nc is 9
max of subtree rooted at nd is 2
max of subtree rooted at ne is 11
max of subtree rooted at nf is 9
max of subtree rooted at ng is 5
max of subtree rooted at nh is 11
max of subtree rooted at ni is 4

list of nodes in subtree rooted a na: 2  7  5  6  11  2  5  4  9  
list of nodes in subtree rooted a nb: 2  7  5  6  11  
list of nodes in subtree rooted a nc: 5  4  9  
list of nodes in subtree rooted a nd: 2  
list of nodes in subtree rooted a ne: 5  6  11  
list of nodes in subtree rooted a nf: 4  9  
list of nodes in subtree rooted a ng: 5  
list of nodes in subtree rooted a nh: 11  
list of nodes in subtree rooted a ni: 4 

###Contributors
1. Rashmeet Kaur Khanuja
2. Anusha Vijay
3. Steven Yen

###File List
1. BinaryTree.java
2. Main.java
	   