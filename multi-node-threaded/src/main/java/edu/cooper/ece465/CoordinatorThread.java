package edu.cooper.ece465;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class CoordinatorThread extends Thread{
    private Graph graph;
    private int startNode;
    private int endNode;
    private Set<Integer> visitedNodes;
    private PriorityQueue<Node> nodeQueue;
    private List<Integer> nodeDistances;
    private Node minNode;
    private CyclicBarrier barrier;
    private AtomicBoolean isFinished;


    public CoordinatorThread(Graph graph, int startNode, int endNode, Set<Integer> visitedNodes, PriorityQueue<Node> nodeQueue, List<Integer> nodeDistances, Node minNode, CyclicBarrier barrier, AtomicBoolean isFinished) {
        this.graph = graph;
        this.startNode = startNode;
        this.endNode = endNode;
        this.visitedNodes = visitedNodes;
        this.nodeQueue = nodeQueue;
        this.nodeDistances = nodeDistances;
        this.minNode = minNode;
        this.barrier = barrier;
        this.isFinished = isFinished;
    }

    @Override
    public void run() {
        while(!isFinished.get()) {
            int currNode = minNode.getNode();
            int currDistance = minNode.getDistance();
            // Get all neighbors for the current node
            List<Integer> currNeighbors = graph.getAdjMatrix().get(currNode);
            // Loop through all neighbors and update distance if neccessary
            for (int i = startNode; i < endNode; i++) {
                if(currNeighbors.get(i) > 0 && !visitedNodes.contains(i)) {
                    int newDistance = currDistance + currNeighbors.get(i);
                    if (newDistance < nodeDistances.get(i)) {
                        nodeDistances.set(i, newDistance);
                        nodeQueue.add(new Node(i, newDistance));
                    }
                }
            }
            try {
                //wait for all threads to finish their iteration
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
