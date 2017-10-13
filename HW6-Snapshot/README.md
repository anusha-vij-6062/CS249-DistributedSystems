# Chandy-Lamport Distributed Snapshot Algorithm

# Synopsis
This an implementation of the Chandy-Lamport Distributed Snapshot Algorithm. It is used to create a snapshot (a image) of the global state of a distributed system at a moment in time. A snapshot includes the state of each processor (such as heap, local variable, etc) as well as the state of all the communication channels (which has messages that are in transit). Any processor in the system can initiate the snapshot. 

We simulated a system with 3 processors P1, P2, P3 that are continuously sending ALGORITHM messages to each other. Each pair of processors have two channels connection them (e.g. between P1 and P2, there are channels C12 and C21 for either direction). P1 initiates a snapshot by recording its own state, start recording on all incoming channels, and sending a special MARKER message on all the outgoing channels. In our implementation the only processor state that is recorded is the field "num" which stores the number of ALGORITHM messages received. The value of this field is copied into the field "ans" when the snapshot is triggered via the arrival of a MARKER message. In our implementation, the communication channels are represented by objects of the Buffer class. The processor and buffer follow the observer-observable pattern, where each processor observes all its incoming channels. 

# Running the program
The program can be run by importing the project folder into an IDE like Eclipse or IntelliJ. Alternatively, the program can be compiled and ran through a CLI terminal (provided the javac and java environmental variables are setup). To run the program from terminal, follow the steps below:

1. Ensure the eight .java files (see below file list) are saved in the same directory. <br>
2. Change directory into the folder that contains the eight .java files. <br>
3. Run the command "javac *.java" to compile the program. <br>
4. Run the command "java Main" to run the program.  <br>
5. The output (like the example shown below) will be displayed in the terminal.<br>

# Input
A system with 3 processors that are continuously sending ALGORITHM messages to each other like shown in the CS249 lecture slides on Chandy-Lamport https://www.slideshare.net/secret/e5XLIcu14AolDr

# Sample Output

```
----------------------SNAPSHOT RESULTS----------------------------------------------
Processor 1: Recorded State:4
Processor 2: Recorded State:5
Processor 3: Recorded State:2
For channel 21: ALGORITHM 
For channel 31: ALGORITHM ALGORITHM ALGORITHM 
For channel 32: ALGORITHM ALGORITHM MARKER 
For channel 12: 
For channel 13: ALGORITHM 
For channel 23: 
---------------------------------------------------------------------
```

# File List
Algorithm.java
Buffer.java
ChannelRecorder.java
Executor.java
Main.java
Message.java
MessageType.java
Processor.java
