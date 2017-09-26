/**
 * CS249 HW4 Convergecast Program
 * @author Rashmeet Khanuja, Anusha Vijay, Steven Yen
 * This Main class has a helper method init() that is 
 * used to initialize the binary tree. Then in the main method
 * the FindMax() and concatToStringBuff() methods are called 
 * on the root. Which passes the max value from all the nodes to 
 * the root, as well as forming a string concatenation containg the 
 * values of the all the nodes.
 */
public class Main {

	public static void main(String[] args) {
		
		//Initialize the tree per the assignment
		BinaryTree T1 = init();
		
		//Call the algorithm on the root node of the tree
		T1.FindMax(T1.getRoot()); //populates "max_at_subtree" field of each node in tree
		T1.concatToStringBuff(T1.getRoot()); //populates "buffer" field of each node in tree
		
		//Print the result of max value and stringBuffer stored at root
		System.out.println("Root has max value from all the nodes, and it is: "+T1.getRoot().getMaxField());
		System.out.println("Root has the values of all the nodes concatenated, and it is " + T1.getRoot().getStringBuff());
	
	}
	
	/**
	 * Initializes the binary tree given in assignment
	 * @return the BinaryTree object
	 */
	public static BinaryTree init(){

		BinaryTree.Node na = new BinaryTree.Node(2); //this is the root node
		BinaryTree.Node nb = new BinaryTree.Node(7);
		BinaryTree.Node nc = new BinaryTree.Node(5);
		BinaryTree.Node nd = new BinaryTree.Node(2);
		BinaryTree.Node ne = new BinaryTree.Node(6);
		BinaryTree.Node nf = new BinaryTree.Node(9);
		BinaryTree.Node ng = new BinaryTree.Node(5);
		BinaryTree.Node nh = new BinaryTree.Node(11);
		BinaryTree.Node ni = new BinaryTree.Node(4);
		
		na.addChildren(nb, nc);
		nb.addChildren(nd, ne);
		nc.addChildren(null, nf);
		ne.addChildren(ng, nh);
		nf.addChildren(ni, null);
	
		BinaryTree tree = new BinaryTree();
		tree.setRoot(na);
		
		return tree;
		
	}
	
}
