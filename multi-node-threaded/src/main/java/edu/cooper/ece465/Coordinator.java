package edu.cooper.ece465;

import edu.cooper.ece465.messages.NodeMessage;

import java.io.File;
import java.util.*;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class Coordinator {
    private Graph graph;
    private NodeMessage currNode;
    private List<Integer> nodeDistances;
    // Using an atomicboolean mutable so we can change the value in the threads
    private AtomicBoolean isFinished;
    private PriorityQueue<NodeMessage> localMinNodes= new PriorityQueue<>();

    public List<Integer> runAlgo(Graph graph, int numThreads, int[] portList) throws InterruptedException {
        // Init vars
        this.graph = graph;
        isFinished = new AtomicBoolean(false);
        nodeDistances = new ArrayList<>(graph.getNumNodes());
        currNode = new NodeMessage(null, 0);
        // Init threads + queues for threads
        List<Thread> threads = new ArrayList<>();
        // Preset distances to infinity
        for (int i = 0; i < graph.getNumNodes(); i++)
            nodeDistances.add(i, Integer.MAX_VALUE);
        // Set starting node distance to 0
        nodeDistances.set(graph.getSourceNode(), 0);
        // Find min node once the threads have finished one iteration of Dijkstra's using await
        FindMinNode findMinNode = new FindMinNode(isFinished, currNode, localMinNodes);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(numThreads, findMinNode);
        // Break up graph and distribute among threads
        int subgraphSize = graph.getNumNodes() / numThreads;
        int excessNodes = graph.getNumNodes() % numThreads;
        // Init vars
        int startNode = 0;
        int endNode;
        // Create + Start threads
        for (int i = 0; i < numThreads; i++) {
            endNode = startNode + subgraphSize;
            // if there are excess nodes after distributing, then some subgraphs get one node until all excess nodes gone
            if (excessNodes > 0) {
                endNode = endNode + 1;
                excessNodes = excessNodes - 1;
            }

            // Create new thread and run dijkstra on subgraph
            Thread DThread = new CoordinatorThread(graph, startNode, endNode, nodeDistances,
                    currNode, cyclicBarrier, isFinished, portList[i], localMinNodes);
            DThread.start();
            // Add new thread to queue
            threads.add(DThread);

            // Update startNode to w/e endNode was w/ one node overlap in new subgraphs to later connect them together
            startNode = endNode;
        }
        // Sync threads
        for (int i = 0; i < numThreads; i++){
            threads.get(i).join();
        }
        return nodeDistances;
    }

    public static class FindMinNode implements Runnable {

        private AtomicBoolean isFinished;
        private NodeMessage currNode;
        private PriorityQueue<NodeMessage> localMinNode;

        public FindMinNode(AtomicBoolean isFinished, NodeMessage currNode, PriorityQueue<NodeMessage> localMinNode) {
            this.isFinished = isFinished;
            this.currNode = currNode;
            this.localMinNode = localMinNode;
        }

//        @Override
//        public void run() {
//            while (true) {
//                Node minNode = null;
//                int index = 0;
//
//                for (int i = 0; i < nodeQueue.size(); i++){
//                    // if thread's queue is not empty, get first node in priorityqueue which will be node w/ smallest dist
////                    System.out.println("Coordinator: " + nodeQueue.get(i));
//                    if (!nodeQueue.get(i).isEmpty()) {
//                        Node node = nodeQueue.get(i).peek();
//                        //if minNode not found or current node smaller than minNode, set minNode as current node
//                        if (minNode == null || Objects.requireNonNull(node).compareTo(minNode) < 0) {
//                            minNode = node;
//                            index = i;
//                        }
//                    }
//                }
//                // if minNode not found b/c queues are empty, the algorithm is finished
//                if (minNode == null){
//                    isFinished.set(true);
//                    return;
//                }
//                else if(!visitedNodes.contains(minNode.getNode())) {
//                    // min node found and not yet visited, set currNode as minNode then remove minNode from the queue
//                    visitedNodes.add(minNode.getNode());
//                    currNode.setNode(minNode.getNode());
//                    currNode.setDistance(minNode.getDistance());
//                    nodeQueue.get(index).remove();
//                    return;
//                }
//                else {
//                    // min node found but visited already, remove
//                    nodeQueue.get(index).remove();
//                }
//
//            }
//        }
        @Override
        public void run() {
            if(localMinNode.size() == 0) {
                currNode.setMinNode(null);
                isFinished.set(true);
            }
            else {
                NodeMessage locMinNode = localMinNode.poll();
                currNode.setMinNode(locMinNode.getMinNode());
                currNode.setStartNode(locMinNode.getStartNode());
                localMinNode.clear();
            }
        }
    }
}
