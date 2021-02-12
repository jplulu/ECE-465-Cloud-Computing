package edu.cooper.ece465.messages;

import edu.cooper.ece465.Graph;

import java.io.Serializable;
import java.util.List;

public class InitMessage implements Serializable {
    private final Graph graph;
    private final List<Integer> nodeDist;
    private final int startNode, endNode;

    public InitMessage(Graph graph, List<Integer> n, int startNode, int endNode) {
        this.nodeDist = n;
        this.graph = graph;
        this.startNode = startNode;
        this.endNode = endNode;
    }

    public List<Integer> getNodeDist() {
        return nodeDist;
    }

    public Graph getGraph() {
        return graph;
    }

    public int getStartNode() {
        return startNode;
    }

    public int getEndNode() {
        return endNode;
    }

}
