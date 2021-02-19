package edu.cooper.ece465;

import edu.cooper.ece465.messages.InitMessage;
import edu.cooper.ece465.messages.ServerToClientMessage;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class Worker {
    private final int portNumber;
    private final String host;

    public Worker(String host, int portNumber) {
        this.host = host;
        this.portNumber = portNumber;
    }

    public void start() {
        System.out.println("Worker started on port " + portNumber);
        try(Socket s = new Socket(host, portNumber)){
            System.out.println("Connection establish with " + host + "::" + portNumber);
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
                    System.out.println("Worker initialized");
                } catch (EOFException ignored) {
                }
            }

            while(!isFinished) {
                try {
                    itrData = (ServerToClientMessage) objectInputStream.readObject();
//                    System.out.println(itrData.getCurrNode());
                    if (itrData.getCurrNode() == null) {
                        isFinished = true;
                        objectOutputStream.writeObject(nodeDistances);
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
//                            System.out.println(nodeDistances);
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
//                        System.out.println(nodeQueue);
                    }
                } catch (EOFException e) {
                    System.out.println("Server socket closed.");
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
