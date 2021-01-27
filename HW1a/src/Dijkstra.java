import java.util.*;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class Dijkstra {
    private Graph graph;
    private Node currNode;
    private List<Integer> nodeDistances;
    private Set<Integer> visitedNodes;
    private AtomicBoolean threadFinished;
    private List<PriorityQueue<Node>> nodeQueue = new ArrayList<>();

    public List<Integer> RunAlgo(Graph graph, int numThreads) throws InterruptedException {
        this.graph = graph;
        threadFinished = new AtomicBoolean(false);
        nodeDistances = new ArrayList<>(graph.getNumNodes());
        currNode = new Node(graph.getSourceNode(), 0);

        // Preset distances to infinity
        for (int i = 0; i < graph.getNumNodes(); i++)
            nodeDistances.add(i, Integer.MAX_VALUE);
        // Set starting node distance to 0
        nodeDistances.set(graph.getSourceNode(), 0);

        // Init threads
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            PriorityQueue<Node> threadQueue = new PriorityQueue<>();
            nodeQueue.add(threadQueue);
        }

        // Find min node once the threads have finished one iteration of Dijkstra's using await
        FindMinNode findMinNode = new FindMinNode(nodeQueue, threadFinished, visitedNodes, currNode);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(numThreads, findMinNode);
        // Break up graph and distribute among threads
        int startNode;
        int endNode = 0;
        int subgraphSize = graph.getNumNodes() / numThreads;
        int excessNodes = graph.getNumNodes() % numThreads;
        // Create + Start threads
        for (int i = 0; i < numThreads; i++) {
            startNode = endNode; //always one node overlap to later connect subgraphs
            endNode = startNode + subgraphSize;
            // if there are excess nodes after distributing, then some subgraphs get one node until all excess nodes gone
            if (excessNodes > 0) {
                endNode = endNode + 1;
                excessNodes = excessNodes - 1;
            }
            // Create new thread and run dijkstra on subgraph
            Thread DThread = new DijkstraThread(graph, startNode, endNode, visitedNodes, nodeQueue.get(i), nodeDistances,
                    currNode, cyclicBarrier, threadFinished);
            DThread.start();
            // Add new thread to queue
            threads.add(DThread);
        }
        // Sync threads
        for (int i = 0; i < numThreads; i++){
            threads.get(i).join();
        }
        return nodeDistances;
    }

    public static class FindMinNode implements Runnable {

        private List<PriorityQueue<Node>> nodeQueue;
        private AtomicBoolean threadFinished;
        private Set<Integer> visitedNodes;
        private Node currNode;

        public FindMinNode(List<PriorityQueue<Node>> nodeQueue, AtomicBoolean threadFinished, Set<Integer> visitedNodes, Node currNode) {
            this.nodeQueue = nodeQueue;
            this.threadFinished = threadFinished;
            this.visitedNodes = visitedNodes;
            this.currNode = currNode;
        }

        @Override
        public void run() {
            while (true) {
                Node minNode = null;
                int index = 0;

                for (int i = 0; i < nodeQueue.size(); i++){
                    // if thread's queue is not empty, get first node in priorityqueue which will be node w/ smallest dist
                    if (!nodeQueue.get(i).isEmpty()) {
                        Node node = nodeQueue.get(i).peek();
                        if (minNode == null || Objects.requireNonNull(node).compareTo(minNode) < 0) {
                            minNode = node;
                            index = i;
                        }
                    }
                }
                // if minNode not found b/c queues are empty, nothing more to do
                if (minNode == null){
                    threadFinished.set(true);
                    return;
                }
                else if(!visitedNodes.contains(minNode.getNode())) {
                    // min node found and not yet visited
                    visitedNodes.add(minNode.getNode());
                    currNode.setNode(minNode.getNode());
                    currNode.setDistance(minNode.getDistance());
                    nodeQueue.get(index).remove();
                    return;
                }
                else {
                    // min node found but visited already, remove
                    nodeQueue.get(index).remove();
                }

            }
        }
    }
}
