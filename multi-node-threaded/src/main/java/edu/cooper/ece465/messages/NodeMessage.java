package edu.cooper.ece465.messages;

import edu.cooper.ece465.Node;

import java.io.Serializable;

public class NodeMessage implements Comparable<NodeMessage>, Serializable {
    private Node minNode;
    private int startNode;


    public NodeMessage(Node minNode, int startNode) {
        this.minNode = minNode;
        this.startNode = startNode;
    }

    public Node getMinNode() {
        return minNode;
    }

    public int getStartNode() {
        return startNode;
    }

    @Override
    public int compareTo(NodeMessage nodeMessage) {
        return Double.compare(this.minNode.getDistance(), nodeMessage.minNode.getDistance());
    }

    public void setMinNode(Node minNode) {
        this.minNode = minNode;
    }

    public void setStartNode(int startNode) {
        this.startNode = startNode;
    }
}
