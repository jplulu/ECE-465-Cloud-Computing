package edu.cooper.ece465.messages;

import edu.cooper.ece465.Node;

import java.io.Serializable;

public class ClientToServerMessage implements Comparable<ClientToServerMessage>, Serializable {
    private final Node localMinNode;
    private final int startNode;


    public ClientToServerMessage(Node localMinNode, int startNode) {
        this.localMinNode = localMinNode;
        this.startNode = startNode;
    }

    public Node getLocalMinNode() {
        return localMinNode;
    }

    public int getStartNode() {
        return startNode;
    }

    @Override
    public int compareTo(ClientToServerMessage clientToServerMessage) {
        return Double.compare(this.localMinNode.getDistance(), clientToServerMessage.localMinNode.getDistance());
    }
}
