/**
 * CS249 HW4 Convergecast Program
 * @author Rashmeet Khanuja, Anusha Vijay, Steven Yen
 * This is the BinaryTree ADT. It contains the Node class as
 * an inner class since Node is logically part of the tree.
 * This class also contains the FindMax() method and the 
 * concatToStringBuff() method that passes information back 
 * from the leaves towards the root (after the recursion bottoms out)
 * in a convergecast fashion.
 */
public class BinaryTree {
	
	Node root;
	
	//Place Node inside BinaryTree as it is logically part of it
	public static class Node{
		int value;
		Node left;
		Node right;
		Node parent;
		int maxInSubtree; //max value in subtree rooted at current node
		String buffer; //buffer containing all nodes in subtree
			
		public Node(int val){
			value = val;
			left = null;
			right = null;
			parent = null;
			maxInSubtree = Integer.MIN_VALUE;
			buffer = "";
		}
		
		/**
		 * Add children nodes to the calling node ("this")
		 * @param lc left child node
		 * @param rc right child node
		 */
		public void addChildren(Node lc, Node rc){	
			if(lc != null){
				this.left = lc;
				lc.parent = this;
			}
			
			if(rc !=null){
				this.right = rc;
				rc.parent = this;	
			}	
		}
		
		/**
		 * Getter/accessor for the "maxInSubtree" field
		 * @return int value of the "maxInSubtree" field
		 */
		public int getMaxField(){
			return maxInSubtree;
		}
		
		/**
		 * Getter/accessor for the "buffer" field
		 * @return string value of the "buffer" field
		 */
		public String getStringBuff(){
			return buffer;
		}
	
	}
	
	/**
	 * Setter/mutator method for root node
	 * @param node node to be designated root
	 */
	public void setRoot(Node node){
		root = node;
	}
	
	/**
	 * Getter/accessor method for root node
	 * @return
	 */
	public Node getRoot(){
		return root;
	}
	
	/**
	 * Recursively call to find max bottoms out at leaf nodes and
	 * starts passing the max values observed in a given subtree back
	 * also sets the "maxInSubtree" field of each node along the way
	 * @param node the current node
	 * @return the max value observed in the subtree rooted at the current node.
	 */
	public int FindMax(Node node){	
		if(node == null){
			//this is the base case (happens at sentinel child nodes of leaves).
			return Integer.MIN_VALUE;
		}else{
			//recursive case called on internal nodes.
			node.maxInSubtree = Integer.max(node.value, Integer.max(FindMax(node.left), FindMax(node.right)));
			return node.maxInSubtree;
		}
	}
	
	/**
	 * Concatenates value of all nodes rooted at current node into a string and pass result back
	 * @param node the current node 
	 * @return string containing values of all nodes in subtree rooted at the current node.
	 */
	public String concatToStringBuff(Node node){	
		if(node == null){
			//this is the base case (happens at sentinel child nodes of leaves).
			return "";
		}else{
			//recursive case called on internal nodes.
			node.buffer += concatToStringBuff(node.left) + node.value + "  " + concatToStringBuff(node.right);
			return node.buffer;
		}
	}

}
