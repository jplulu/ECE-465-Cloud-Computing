# HW1 - Parallelized Single-source Dijkstra's Algorithm

This program is a parallelized implementation of the single-source Dijkstra's algorithm using Java threads. The program takes an adjacency matrix represented graph and a source node as input and outputs the minimum distance from the source node to all other nodes within the graph.

## Description
The program will take a graph and break it up into smaller graphs, each being assigned to a thread. The threads all perform Dijkstra's on their respective chunks, updating the node weights locally. Once all the threads finish executing, the global minimum node is selected using the node weights from all the threads, which is then broadcasted to all threads to be used in the next iteration of the algorithm. The algorithm finishes once an unvisited global minimum node cannot be found. The program outputs the minimum distances from the source node to all other nodes.

## Usage
An ```input.txt``` file that contains an adjacency matrix graph must be present in ```HW1a/```. This file must specify the number of nodes, the source node, and the adjacency matrix itself in the format below:
```
<number of nodes> <source node>
0 0 1
1 0 2
0 1 0
```
To run the program:
```
cd src/
javac Main.java
java Main
```

## References
https://en.wikipedia.org/wiki/Parallel_single-source_shortest_path_algorithm
https://cse.buffalo.edu/faculty/miller/Courses/CSE633/Ye-Fall-2012-CSE633.pdf
