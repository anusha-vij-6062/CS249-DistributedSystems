# Vector Clock Program

# Synopsis

Implementation of vector clock for communicating processes. Each processor maintains a vector clock that is updated as events take place. As such, we can assign each event of a particular processor a vector clock value. The vector clock can be used to determine the "happen-before" relationships between events. That is, if the vector clock of event A is less than the vector clock of event B (in that every entry of the vector clock array of A is less than that of B), then we can say definitively that event A happened before event B. The compareTo() method in the VectorClock class can be used to perform this comparison.

# Running the program

The program can be run by importing the "src" folder into an IDE like Eclipse or IntelliJ. Alternatively, the program can be compiled and ran through a CLI terminal (provided the javac and java environmental variables are setup). To run the program from terminal, following the steps below:

1. Ensure the eight .java files are saved in a folder with the directory path "src/com/sjsu/vector". 
2. Change directory into the "vector" folder that contains the eight .java files. 
3. Run the command "javac *.java" to compile the program. 
4. Change directory out three levels to the "src" folder with the command "cd ../../../".
5. Run the command "java com.sjsu.vector.Main" to run the program. 
6. The output (like the example shown below) will be displayed in the terminal.

# Input

As given in the class slides<br>

# Expected Output


```
P2 send COMPUTATION message to P2
  VC of P2 updated to: [0	0	1	]
  VC of P0 updated to: [1	0	0	]
P0 send RECIEVE message to P1 
  VC of P1 updated to: [1	1	0	]
P2 send COMPUTATION message to P2 
  VC of P2 updated to: [0	0	2	]
  VC of P2 updated to: [0	0	3	]
P2 send RECIEVE message to P1 
  VC of P1 updated to: [1	2	3	]
  VC of P0 updated to: [2	0	0	]
P0 send RECIEVE message to P2 
  VC of P2 updated to: [2	0	4	]
  VC of P1 updated to: [1	3	3	]
P1 send RECIEVE message to P2 
  VC of P2 updated to: [2	3	5	]
  VC of P2 updated to: [2	3	6	]
P2 send RECIEVE message to P1 
  VC of P1 updated to: [2	4	6	]
P0 send COMPUTATION message to P0 
  VC of P0 updated to: [3	0	0	]
P1 send RECIEVE message to P0 
  VC of P0 updated to: [4	4	6	]
P2 send COMPUTATION message to P2 
  VC of P2 updated to: [2	3	7	]
P0 send COMPUTATION message to P0 
  VC of P0 updated to: [5	4	6	]

----------The final VECTOR CLOCK for process 1 is----
[2	4	6	]

----------The final VECTOR CLOCK for process 2 is----
[2	3	7	]

------The final VECTOR CLOCK for process 0 is---
[5	4	6	]

```
