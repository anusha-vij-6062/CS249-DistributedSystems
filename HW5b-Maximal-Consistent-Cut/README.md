# Maximal Consistent Cut

# Synopsis
This program determines the maximal consistent cut in an execution given an input cut. That is, it finds the latest consistent cut that does not exceed (is less than or equal to) the input cut k. This is achieved by making each processor store an array of the vector clock timestamps of the all the computation events. Once the processor receives a cut message, it reads the entry in the cut that corresponds to the processor and set it as m. Then starting from the mth timestamp in the store array, it scans down the store until it reaches a timestamp store[m’]<=k (based on the vector comparison), the value of m’ will be the entry in the maximal consistent cut that corresponds to this processor. Every processor will perform this same calculation to determine its m’. Together these m’ makes up the final consistent cut result. 

For this assignment we used as the input space-time diagram (execution plan) given in Fig.6.7 (pg 134) of the book “Distributed Computing: Fundamentals, Simulations and Advanced Topics, 2ed, by H. Attiya and J. Welch”. The input cut and the expected output are as follows:

Input cut 	|expected output	  |our output
<1,3>		    |<1,3>			        |<1,3>			
<1,4>		    |<1,4>			        |<1,4>
<2,6>		    |<2,5>			        |<2,5>

# Running the program:
The program can be run by importing/opening this project “HW5b-Maximal-Consistent-Cut” in an IDE like Eclipse or IntelliJ. Alternatively, the program can be compiled and ran through a CLI terminal (provided the javac and java environmental variables are setup). To run the program from terminal, following the steps below:

1..Ensure the eight .java files are saved in a folder with the directory path "src/com/sjsu/vector".
2..Change directory into the "vector" folder that contains the eight .java files (see file list section).
3..Run the command "javac *.java" to compile the program.
4..Change directory out three levels to the "src" folder with the command "cd ../../../".
5..Run the command "java com.sjsu.vector.Main" to run the program.
6..The output (like the example shown below) will be displayed in the terminal.

# Input 
For this assignment we used as the input space-time diagram (execution plan) given in Fig.6.7 (pg 134) of the book “Distributed Computing: Fundamentals, Simulations and Advanced Topics, 2ed, by H. Attiya and J. Welch”.

# Output
…
P1 send COMPUTATION message to P1 
  VC of P1 updated to: [0	1	]
  VC of P0 updated to: [1	0	]
P1 send COMPUTATION message to P1 
  VC of P1 updated to: [0	2	]
P1 send COMPUTATION message to P1 
  VC of P1 updated to: [0	3	]
P0 send COMPUTATION message to P0 
  VC of P0 updated to: [2	0	]
P0 send RECIEVE message to P1 
  VC of P1 updated to: [1	4	]
P1 send COMPUTATION message to P1 
  VC of P1 updated to: [1	5	]
P0 send COMPUTATION message to P0 
  VC of P0 updated to: [3	0	]
  VC of P0 updated to: [4	0	]
P0 send RECIEVE message to P1 
  VC of P1 updated to: [4	6	]

------The final VECTOR CLOCK for process 0 is---
[4	0	]
The store array is: [<1, 0>,	<2, 0>,	<3, 0>,	<4, 0>,	]

----------The final VECTOR CLOCK for process 1 is----
[4	6	]
The store array is: [<0, 1>,	<0, 2>,	<0, 3>,	<1, 4>,	<1, 5>,	<4, 6>,	]

Starting access from:2 0
Compared to 2 0
P1 send CUT message to P1

Starting access from:4 6
Compared to 4 6
Passed
Compared to 1 5
Based on an input cut of <2, 6>,
The Final Maximal Consistent Cut is <2, 5>
…
…
Based on an input cut of <1, 3>,
The Final Maximal Consistent Cut is <1, 3>
…
…
Based on an input cut of <1, 4>,
The Final Maximal Consistent Cut is <1, 4>
…

# Prerequisites
-java version "1.8.0_144"
-java SE Runtime Environment (build 1.8.0_144-b01)

# Contributors
-Rashmeet Kaur Khanuja
-Anusha Vijay
-Steven Yen

# File List
Algorithm.java, Buffer.java, Executor.java, Main.java, Message.java, MessageType.java, Processor.java, VectorClock.java

