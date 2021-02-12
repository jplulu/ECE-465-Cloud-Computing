package edu.cooper.ece465.messages;

import edu.cooper.ece465.Node;

import java.io.Serializable;
import java.util.HashSet;
import java.util.PriorityQueue;

public class ServerToClientMessage implements Serializable {
    private final Node currNode;
    private final HashSet<Integer> visitedNode;
    private final PriorityQueue<Node> priorityQueue;

    public ServerToClientMessage(Node currNode, HashSet<Integer> visitedNode, PriorityQueue<Node> priorityQueue) {
        this.currNode = currNode;
        this.visitedNode = visitedNode;
        this.priorityQueue = priorityQueue;
    }

    public Node getCurrNode() {
        return currNode;
    }

    public HashSet<Integer> getVisitedNode() {
        return visitedNode;
    }

    public PriorityQueue<Node> getPriorityQueue() {
        return priorityQueue;
    }
}
