# DFS Spanning Tree Algorithm with specified Root

###Prerequisites
```
1. Java version "1.8.0_92"
```
###Synopsis
This program simulates the DFS Spanning Tree Algorithm with specified root. The body of the algorithm is implemented in the update() method of the Processor class. In a DFS manner, it sends the message <M> to each node in the graph. The root sets itself as its parent, while the rest of the nodes sets as theirs parent the node from which it first received the message <M>. 
The <already> and <parent> messages are used to pass messages back to the parent and eventually to the root, singaling the end of the algorithm. The message <parent> is used to singal to a parent node to add nodes to its children list. The message <already> is used to signal to a node that a node already has a parent.
The input to the program is a graph where each node is a processor and the edges represent connections. This graph is represented by a HashMap in Main, where for each entry, the key is a Processor, and the value is a list of Processors that are adjacent to (connected to) that processor.

###Running the program :

1. Save all of the source code (Buffer.java, Main.java, Message.java, and Processor.java) to the same directory. 
2. Change directory (cd) to the directory containing all the source codes.
3. Run the command "javac *.java" to compile the code.
4. After compilation, run the command "java Main" to run the code. This should print the final Spanning Tree from running the algorithm to the console.

###Input
A graph as show below was given as the input. 
The init() method within the Main class constructor is used to initialize the graph.
``` 
Process p0=new Processor(0);
List<Processor> neighbour = new ArrayList<>(Arrays.asList(p1,p2,p3));
p0.setUnexplored(neighbour);
``` 
####Graph
```      
         P3
        /
       /
    P0
   /  \
  /    \ 
 /      \
/        \
P1-------P2
|         | 
|         |
P4-------P5


```


##Expected Output Tree
```
--------------PRINTING TREE---------------
Root Process 0<br>

Process ID :0	 Parent ID: 0	 Children : 1 3
Process ID :1	 Parent ID: 0	 Children : 2   
Process ID :2	 Parent ID: 1	 Children : 5  
Process ID :3	 Parent ID: 0	 Children :   
Process ID :4	 Parent ID: 5	 Children :    
Process ID :5	 Parent ID: 2	 Children : 4  
```

##Contributors

1. Tanuja Phadake for providing the skeletal code for the DFS spanning tree program.<br>
2. Steven Yen  SJSU ID: 006147401 <br>
3. Rashmeet Kaur Khanuja SJSU ID: 012409982 <br>
4. Anusha Vijay SJSU ID: 010826062 <br>

###File List
1.IBuffer.java
2.Buffer.java
3.Processor.java
4.Iprocessor.java
5.Message.java
6.Main.java
7.README.md
8.sampleOutput.txt
