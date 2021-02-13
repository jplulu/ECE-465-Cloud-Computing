# HW2 - Multi-Node Multi-threaded Single-source Dijkstra's Algorithm

This program is a multi-Node multi-threaded implementation of the single-source Dijkstra's algorithm using Java threads.

# Algorithm Description
The multi-node version of the algorithm builds on top of the single-node multi-threaded version from HW1. To implement multi-node, a coordinator and multiple workers are used; all networking are done through the use of Java sockets and custom serializable messages. At the start of the algorithm, the coordinator spawns worker threads that establishes connection with the workers nodes via socket; it then breaks up the input graph into subgroups of vertices and initializes the worker nodes by assigning each node a subgroup to be processed. In a worker node, it first establishes connection with the coordinator via socket and then performs Dijkstra's on its respective chunk, updating the vertex weights locally, and putting the vertices into a local priority queue. This priority queue is sent back to the coordinator at the end of each iteration. At the start of each iteration, the coordinator first determines the vertex with the global minimum distance using the priority queues received from all the worker nodes; it then broadcasts this vertex to each work node through a custom message object via socket. Using this new vertex, each worker node enters another iteration of Dijkstra's as described previously. The algorithm finishes once an unvisited global minimum vertex can no longer be found. This information is broadcasted to each worker node through an exit message, signaling the termination of the algorithm. The program outputs the minimum distances from the source node to all other nodes.

## Usage
To run the program:
```
git clone https://github.com/jplulu/ECE-465-Cloud-Computing.git
cd ECE-465-Cloud-Computing
./build.sh
```
Run the coordinator first:
```
java -cp multi-node-threaded/target/multi-node-threaded-0.0.1-jar-with-dependencies.jar edu.cooper.ece465.CoordinatorMain
```
Then run the worker on each node:
```
java -cp multi-node-threaded/target/multi-node-threaded-0.0.1-jar-with-dependencies.jar edu.cooper.ece465.WorkerMain
```
