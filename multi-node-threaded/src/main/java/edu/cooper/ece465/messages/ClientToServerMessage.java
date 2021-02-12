package edu.cooper.ece465.messages;

import edu.cooper.ece465.Node;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class ClientToServerMessage implements Serializable {
    private final HashSet<Integer> visitedNode;
    private final PriorityQueue<Node> priorityQueue;

    public ClientToServerMessage(HashSet<Integer> visitedNode, PriorityQueue<Node> priorityQueue) {
        this.visitedNode = visitedNode;
        this.priorityQueue = priorityQueue;
    }

    public HashSet<Integer> getVisitedNode() {
        return visitedNode;
    }

    public PriorityQueue<Node> getPriorityQueue() {
        return priorityQueue;
    }
}
