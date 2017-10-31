# Leader Election in Async Ring O(n log n ) Algorithm

# Synopsis
This is an implementation of leader-election algorithm in an asynchronous ring with time complexity O(nLogn ) . The goal is to select a leader in a set of processors connected in a ring. In our program, each processor has a id field ("procId"), and this value of this id is used as the criteria for comparing processors when electing leaders. The end result is that the processor with the highest id is elected the leader processor.

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
Ring with 6 processors with IDs 10, 22, 11, 60, 50, and 44.<br>
->(P10)-->(P22)-->(P11)-->(P60)-->(P50)-->(P44)-><br>
|_______________________________________________| 

This ring is initialized in the init() method in Main.

CASE #2
Alternatively, used as input a ring with 5 processors with IDs 10, 22, 11 and 44.<br>

->(P1)-->(P2)-->(P3)-->(P4)--><br>
|____________________________|  

Change the init2() in the the Main constructor to init() to use this ring.                           


# Output

Output for CASE 1 (see above for input)
```
---------------------------------------------------
    Winner of Phase:0 Process:22
---------------------------------------------------
    Winner of Phase:0 Process:60
---------------------------------------------------
    Winner of Phase:1 Process:60
---------------------------------------------------
    Winner of Phase:2 Process:60
---------------------------------------------------
    Winner of Phase:3 Process:60

Leader:60
--->Forwarding Terminating Message to Process:50
--->Forwarding Terminating Message to Process:44
--->Forwarding Terminating Message to Process:10
--->Forwarding Terminating Message to Process:22
--->Forwarding Terminating Message to Process:11

Leader:60
Processor P10 is leader? false
Processor P22 is leader? false
Processor P11 is leader? false
Processor P60 is leader? true
Processor P50 is leader? false
Processor P44 is leader? false

Process finished with exit code 0

```

Output for CASE 2 (see above for input)

```
---------------------------------------------------
    Winner of Phase:0 Process:22
---------------------------------------------------
    Winner of Phase:0 Process:44
---------------------------------------------------
    Winner of Phase:1 Process:44
---------------------------------------------------
    Winner of Phase:2 Process:44

Leader:44
--->Forwarding Terminating Message to Process:10
--->Forwarding Terminating Message to Process:22
--->Forwarding Terminating Message to Process:11

Processor P10 is leader? false
Processor P22 is leader? false
Processor P11 is leader? false
Processor P44 is leader? true

Process finished with exit code 0

```

# File List
Buffer.java <br>
Main.java <br>
Message.java <br>
MessageType.java <br>
Processor.java <br>
