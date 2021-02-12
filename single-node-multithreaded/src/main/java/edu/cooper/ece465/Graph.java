package edu.cooper.ece465;

import java.io.Serializable;
import java.util.List;

public class Graph implements Serializable {
    private int sourceNode;
    private List<List<Integer>> adjMatrix;
    private int numNodes;

    public Graph(int sourceNode, List<List<Integer>> adjMatrix) {
        this.sourceNode = sourceNode;
        this.adjMatrix = adjMatrix;
        this.numNodes = adjMatrix.size();
    }

    public int getSourceNode() {
        return sourceNode;
    }

    public List<List<Integer>> getAdjMatrix() {
        return adjMatrix;
    }

    public int getNumNodes() {
        return numNodes;
    }
}