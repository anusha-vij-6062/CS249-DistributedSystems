# Leader Election in Async Ring O(n^2) Algorithm

# Synopsis
This is an implementation of the O(n^2) algorithm for leader-election in an asynchronous ring. The goal is to select a leader in a set of processors connected in a ring. In our program, each processor has a id field ("procId"), and this value of this id is used as the criteria for comparing processors when electing leaders. The end result is that the processor with the highest id is elected the leader processor.

# Running the program
The program can be run by importing the project folder into an IDE like Eclipse or IntelliJ. Alternatively, the program can be compiled and ran through a CLI terminal (provided the javac and java environmental variables are setup). To run the program from terminal, follow the steps below:

1. Ensure the six .java files (see below file list) are saved in the same directory. <br>
2. Change directory into the folder that contains the six .java files. <br>
3. Run the command "javac *.java" to compile the program. <br>
4. Run the command "java Main" to run the program.  <br>
5. The output (like the example shown below) will be displayed in the terminal.<br>

# Input
We tried two different inputs for this program.

CASE #1
Ring with 6 processors with IDs 1, 44, 12, 10, 50, and 33 as shown on lecture slide 10.
->(P01)-->(P44)-->(P12)-->(P10)-->(P50)-->(P33)-><br>
|_______________________________________________| 

This ring is initialized in the init2() method in Main.

CASE #2
Alternatively, used as input a ring with 5 processors with IDs 0, 1, 2, 3, and 4 as shown on lecture slide 14.

->(P0)-->(P1)-->(P2)-->(P3)-->(P4)-><br>
|__________________________________|  

Change the init2() in the the Main constructor to init() to use this ring.                           


# Output
Output for CASE #1 (see above)
```
P12 received IDENTIFIER message with id=44 
P44 received IDENTIFIER message with id=1 
P1 received IDENTIFIER message with id=33 
P44 received IDENTIFIER message with id=33 
P33 received IDENTIFIER message with id=50 
P50 received IDENTIFIER message with id=10 
P10 received IDENTIFIER message with id=12 
P50 received IDENTIFIER message with id=12 
P1 received IDENTIFIER message with id=50 
P44 received IDENTIFIER message with id=50 
P10 received IDENTIFIER message with id=44 
P50 received IDENTIFIER message with id=44 
P12 received IDENTIFIER message with id=50 
P10 received IDENTIFIER message with id=50 
P50 received IDENTIFIER message with id=50 
P50 declares self as leader 
P33 received TERMINATE message
P1 received TERMINATE message
P44 received TERMINATE message
P12 received TERMINATE message
P10 received TERMINATE message
P50 received TERMINATE message
TERMINATE message went around entire ring. Algorithm done.
Processor P1 is leader? false
Processor P44 is leader? false
Processor P12 is leader? false
Processor P10 is leader? false
Processor P50 is leader? true
Processor P33 is leader? false

Process finished with exit code 0
```
<br>
Output for CASE #2 (see above)
```
P4 received IDENTIFIER message with id=0 
P3 received IDENTIFIER message with id=4 
P2 received IDENTIFIER message with id=3 
P1 received IDENTIFIER message with id=2 
P0 received IDENTIFIER message with id=1 
P4 received IDENTIFIER message with id=1 
P0 received IDENTIFIER message with id=2 
P4 received IDENTIFIER message with id=2 
P1 received IDENTIFIER message with id=3 
P0 received IDENTIFIER message with id=3 
P4 received IDENTIFIER message with id=3 
P2 received IDENTIFIER message with id=4 
P1 received IDENTIFIER message with id=4 
P0 received IDENTIFIER message with id=4 
P4 received IDENTIFIER message with id=4 
P4 declares self as leader 
P3 received TERMINATE message
P2 received TERMINATE message
P1 received TERMINATE message
P0 received TERMINATE message
P4 received TERMINATE message
TERMINATE message went around entire ring. Algorithm done.
Processor P0 is leader? false
Processor P1 is leader? false
Processor P2 is leader? false
Processor P3 is leader? false
Processor P4 is leader? true

Process finished with exit code 0
```

# File List
Buffer.java <br>
Executor.java <br>
Main.java <br>
Message.java <br>
MessageType.java <br>
Processor.java <br>
