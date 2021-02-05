# HW1 - Parallelized Single-source main.java.edu.cooper.ece465.Dijkstra's Algorithm

This program is a parallelized implementation of the single-source main.java.edu.cooper.ece465.Dijkstra's algorithm using Java threads.

## Description
The program will take an adjacency matrix represented graph and break it up into smaller graphs, each being assigned to a thread. The threads all perform main.java.edu.cooper.ece465.Dijkstra's on their respective chunks, updating the node weights locally. Once all the threads finish executing, the global minimum node is selected using the node weights from all the threads, which is then broadcasted to all threads to be used in the next iteration of the algorithm. The algorithm finishes once an unvisited global minimum node cannot be found. The program outputs the minimum distances from the source node to all other nodes.

## Usage
An ```input.txt``` file that contains an adjacency matrix graph is needed as input for the program. This file must specify the number of nodes, the source node, and the adjacency matrix itself in the format below:
```
<number of nodes> <source node>
0 0 1
2 0 3
0 4 0
```
An input.txt file can be randomly generated as follows:
```
cd HW1a/src/
javac main.java.edu.cooper.ece465.GenerateGraph.java
java main.java.edu.cooper.ece465.GenerateGraph <numberOfNodes>
```
To run the program:
```
cd HW1a/src/
javac main.java.edu.cooper.ece465.Main.java
java main.java.edu.cooper.ece465.Main
```

## References
https://en.wikipedia.org/wiki/Parallel_single-source_shortest_path_algorithm

https://cse.buffalo.edu/faculty/miller/Courses/CSE633/Ye-Fall-2012-CSE633.pdf
