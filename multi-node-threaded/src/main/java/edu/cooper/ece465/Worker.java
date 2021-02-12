package edu.cooper.ece465;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class Worker {
    public static void main(String[] args) {
        try(Socket s = new Socket("localhost", 420)){
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());

            boolean initDone = false;
            String initData;
            String itrData;

            Graph graph;
            int startNode;
            int endNode;
            Set<Integer> visitedNodes;
            PriorityQueue<Node> nodeQueue;
            List<Integer> nodeDistances;
            Node minNode;
            boolean isFinished = false;

            while(!initDone) {
                if ((initData = br.readLine()) != null) {
                    // TODO: set init data by parsing json
                    initDone = true;
                }
            }

            while(!isFinished) {
                if ((itrData = br.readLine()) != null) {
                    if (itrData.equals("exit")) {
                        isFinished = true;
                    } else {

                        int currNode = minNode.getNode();
                        int currDistance = minNode.getDistance();
                        // Get all neighbors for the current node
                        List<Integer> currNeighbors = graph.getAdjMatrix().get(currNode);
                        // Loop through all neighbors and update distance if neccessary
                        for (int i = startNode; i < endNode; i++) {
                            if (currNeighbors.get(i) > 0 && !visitedNodes.contains(i)) {
                                int newDistance = currDistance + currNeighbors.get(i);
                                if (newDistance < nodeDistances.get(i)) {
                                    nodeDistances.set(i, newDistance);
                                    nodeQueue.add(new Node(i, newDistance));
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
