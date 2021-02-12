package edu.cooper.ece465;

import edu.cooper.ece465.messages.InitMessage;
import edu.cooper.ece465.messages.ServerToClientMessage;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class Worker {
    public static void main(String[] args) {
        try(Socket s = new Socket("localhost", 420)){
            ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(s.getOutputStream());

            boolean initDone = false;
            InitMessage initData;
            ServerToClientMessage itrData;

            Graph graph = null;
            int startNode = 0;
            int endNode = 0;
            HashSet<Integer> visitedNodes;
            PriorityQueue<Node> nodeQueue;
            List<Integer> nodeDistances = null;
            Node minNode;
            boolean isFinished = false;

            while(!initDone) {
                try {
                    initData = (InitMessage) objectInputStream.readObject();
                    graph = initData.getGraph();
                    startNode = initData.getStartNode();
                    endNode = initData.getEndNode();
                    nodeDistances = initData.getNodeDist();
                    initDone = true;
                } catch (EOFException ignored) {
                }
            }

            while(!isFinished) {
                try {
                    itrData = (ServerToClientMessage) objectInputStream.readObject();
                    if (itrData.getCurrNode() == null) {
                        isFinished = true;
                    } else {
                        minNode = itrData.getCurrNode();
                        visitedNodes = itrData.getVisitedNode();
                        nodeQueue = itrData.getPriorityQueue();

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
                        objectOutputStream.writeObject(nodeQueue);
                        objectOutputStream.reset();
                    }
                } catch (EOFException ignored) {
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
