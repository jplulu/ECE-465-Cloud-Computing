# HW1 - Parallelized Single-source Dijkstra's Algorithm

This program is a parallelized implementation of the single-source Dijkstra's algorithm using Java threads.

## Algorithm Description
Dijstras's Algorithm is an algorithm for finding the shortest paths between nodes in a graph. In a single threaded implementation, the algorithm iteratively traverses each node starting from the source node, updates the weights, and selects the node with the minimum weight as the source node for the next iteration. The algorithm terminates once all the nodes have been visited.

In a multi-threaded implementation, the algorithm will take an adjacency matrix represented graph and break it up into subgroups of nodes; each of these subgroups will be assigned to a thread and processed. The threads all perform Dijkstra's on their respective chunks, updating the node weights locally, and putting the nodes into a global priority queue shared among the threads. Each thread is blocked upon its own completion of an iteration through the use of a CyclicBarrier in the Java concurrency library. Once all the threads finish executing, the shared priority queue is used to selected the global minimum node, which is then broadcasted to all threads to be used as the soruce node in the next iteration of the algorithm. The algorithm finishes once an unvisited global minimum node cannot be found. This information is stored in a global AtomicBoolean that is accessible by each thread, and updated at the end of each iteration. The program outputs the minimum distances from the source node to all other nodes.


## Usage
To run the program:
```
git clone https://github.com/jplulu/ECE-465-Cloud-Computing.git
cd ECE-465-Cloud-Computing
./build.sh

# Single Node Multithreaded
java -cp single-node-multithreaded/target/single-node-multithreaded-0.0.1-jar-with-dependencies.jar edu.cooper.ece465.Main
```
## Time analysis
**Tests are done on the following number of nodes**: [1000, 2000, 3000, 4000, 5000, 6000, 7000]

**Results for each test case in milliseconds**:

1 Thread: [52, 64, 152, 226, 342, 518, 821]

2 Threads: [39, 53, 95, 158, 258, 327, 575]

4 Threads: [48, 93, 108, 154, 222, 297, 498]


**For node size < 3000 nodes: 1 Thread = 2 Threads < 4 Threads**

With a small to medium size graph, a smaller amount of threads resulted in shorter run times. This may be attributed to the overhead caused by the context switching.

**For node size > 3000 nodes: 4 Thread < 2 Threads < 1 Threads**

With a larger graph size, the number of threads has a more significant impact on the run time. While four threads has the shorter run time, the difference in the runtime of 2 threads compared to 4 threads is not linear. It can be noted that the difference in times seems to increase as the number of nodes in the graph increases, indicating performance differences may be more evident with a larger graph size.

The complexity of this parallel Dijkstra's algorithm is O(V<sup>2</sup>/P + V * log(P)) where V is the number of nodes and P is the number of cores used whereas the complexity of single-threaded Dijkstra's algorithm is O(v^2). The parallel algorithm offers improvement over the single-threaded implementation.

## References
https://en.wikipedia.org/wiki/Parallel_single-source_shortest_path_algorithm

https://cse.buffalo.edu/faculty/miller/Courses/CSE633/Ye-Fall-2012-CSE633.pdf
