# HW1 - Parallelized Single-source Dijkstra's Algorithm

This program is a parallelized implementation of the single-source Dijkstra's algorithm using Java threads.

## Description
The program will take an adjacency matrix represented graph and break it up into smaller graphs, each being assigned to a thread. The threads all perform Dijkstra's on their respective chunks, updating the node weights locally. Once all the threads finish executing, the global minimum node is selected using the node weights from all the threads, which is then broadcasted to all threads to be used in the next iteration of the algorithm. The algorithm finishes once an unvisited global minimum node cannot be found. The program outputs the minimum distances from the source node to all other nodes.


## Usage
To run the program:
```
git clone https://github.com/jplulu/ECE-465-Cloud-Computing.git
cd ECE-465-Cloud-Computing
./build.sh

# Single Node Multithreaded
java -cp single-node-multithreaded/target/single-node-multithreaded-0.0.1-jar-with-dependencies.jar edu.cooper.ece465.Main
```
## Empirical Time analysis
Number of Nodes: [1000, 2000, 3000, 4000, 5000, 6000, 7000]

1 Thread: [52, 64, 152, 226, 342, 518, 821]

2 Thread: [39, 53, 95, 158, 258, 327, 575]

4 Thread: [48, 93, 108, 154, 222, 297, 498]


Nodesize < 3000 Nodes
1 Thread = 2 Thread < 4 Thread 
With a small to medium size graph, a smaller amount of threads resulted in shorter run times. This may be attributed to the overhead caused by the context switching.


Nodesize > 3000 Nodes
4 Thread > 2 Thread > 1 Thread
With a larger graph size, the number of threads has a more significant impact on the run time. While four threads has the shorter run time, the difference in the runtime of 2 threads compared to 4 threads is not linear. It can be noted that the difference in times seems to increase as the number of nodes in the graph increases, indicating performance differences may be more evident with a larger graph size.

## References
https://en.wikipedia.org/wiki/Parallel_single-source_shortest_path_algorithm

https://cse.buffalo.edu/faculty/miller/Courses/CSE633/Ye-Fall-2012-CSE633.pdf
