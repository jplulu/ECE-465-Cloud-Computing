# HW2 - Multi-Node Multi-threaded Single-source Dijkstra's Algorithm

This program is a multi-Node multi-threaded implementation of the single-source Dijkstra's algorithm using Java threads.

# Algorithm Description
The multi-node version of the algorithm builds on top of the single-node multi-threaded version from HW1. To implement multi-node, a coordinator and multiple workers are used; all networking are done through the use of Java sockets and custom serializable messages. At the start of the algorithm, the coordinator spawns worker threads that establishes connection with the workers nodes via socket; it then breaks up the input graph into subgroups of vertices and initializes the worker nodes by assigning each node a subgroup to be processed. In a worker node, it first establishes connection with the coordinator via socket and then performs Dijkstra's on its respective chunk, updating the vertex weights locally, and putting the vertices into a local priority queue. This priority queue is sent back to the coordinator at the end of each iteration. At the start of each iteration, the coordinator first determines the vertex with the global minimum distance using the priority queues received from all the worker nodes; it then broadcasts this vertex to each work node through a custom message object via socket. Using this new vertex, each worker node enters another iteration of Dijkstra's as described previously. The algorithm finishes once an unvisited global minimum vertex can no longer be found. This information is broadcasted to each worker node through an exit message, signaling the termination of the algorithm. The program outputs the minimum distances from the source node to all other nodes.

## Usage
To build the program:
```
git clone https://github.com/jplulu/ECE-465-Cloud-Computing.git
cd ECE-465-Cloud-Computing
./build.sh
```
An input file must be present for the algorithm to run. To generate the input graph:
```
java -cp single-node-multithreaded/target/single-node-multithreaded-0.0.1-jar-with-dependencies.jar edu.cooper.ece465.GenerateGraph [Number of nodes]
```
Run the coordinator first:
```
java -cp multi-node-threaded/target/multi-node-threaded-0.0.1-jar-with-dependencies.jar edu.cooper.ece465.CoordinatorMain [Number of nodes] [Ports corresponding to # of nodes]
```
Then run the worker on each node:
```
java -cp multi-node-threaded/target/multi-node-threaded-0.0.1-jar-with-dependencies.jar edu.cooper.ece465.WorkerMain [IP of Coordinator] [Coordinator Port] [Optional: Number of threads]
```
Output of the algorithm will be generated in a file called ```multioutput.txt``` in ```ECE-465-Cloud-Computing/```

# Time Analysis - First Revision
Tests were conducted with 10, 100, and 1000 vertices.

Results for each test case in milliseconds
|#Nodes		|10 vertices			|100 vertices			|1000 vertices |
| --- | --- | --- | --- |
|Single		|107ms			|1793ms			|255836ms |
|Four		|99ms				|832ms			|25651ms |


There was a large time increase when moving from single-node local to a multi-node networked algorithm. We attribute this largely to I/O times as well as certain limitations with the data structures we used such as the inefficiency of updating certaining lists, priority queues in particular. In terms of performace, for 10 vertices, we see that there is little to no difference between the 1 vs 4 nodes and the small difference in time is negligable enough to state that there is no performance difference. When we move to 100 vertices, it can be clearly seen that there is about a roughly 2x performance increase moving from 1 node to 4 nodes. WIth 1000 vertices in the graph, the performance jumps to 10x indicating that the algorithm is much more effective on larger graphs.

# Second Revision Update
For second revision, we were able to significantly reduce runtime by lowering the amount of data sent over the network, resulting in less network latency and processing need. To achieve this, we modified the communication protocol between the coordinator and the workers. In the first revision, the workers sent priority queues of vertices to the coordinator and the coordinator would determine the vertex with the minimum distance, update the priority queues, and send them back to the workers. This resulted in huge network overhead due to the processing of large lists.

In the second revision, we modified the protocol by performing the processing of priority queues locally on the worker nodes and sending the local minimum vertices of each worker to the coordinator, which then determines the global minimum vertex and broadcasts it back to the worker nodes. This significantly reduced the network overhead since only a single vertex is being sent each iteration instead of lists of vertices.

# Time Analysis - Second Revision

## References
https://en.wikipedia.org/wiki/Parallel_single-source_shortest_path_algorithm

https://cse.buffalo.edu/faculty/miller/Courses/CSE633/Ye-Fall-2012-CSE633.pdf
