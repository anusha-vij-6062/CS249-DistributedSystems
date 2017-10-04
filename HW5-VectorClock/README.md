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
