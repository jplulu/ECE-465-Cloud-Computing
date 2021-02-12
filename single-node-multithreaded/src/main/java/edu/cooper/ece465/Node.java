package edu.cooper.ece465;

import java.io.Serializable;

public class Node implements Comparable<Node>, Serializable {
    private int node;
    private int distance;

    public Node(int node, int distance) {
        this.node = node;
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public int getNode() {
        return node;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setNode(int node) {
        this.node = node;
    }

    public String toString(){
        return String.valueOf(node) + " " + String.valueOf(distance);
    }

    @Override
    public int compareTo(Node node) {
        return Double.compare(this.distance, node.distance);
    }
}