package edu.cooper.ece465;

import edu.cooper.ece465.messages.InitMessage;
import netscape.javascript.JSObject;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class CoordinatorThread extends Thread{
    private Graph graph;
    private int startNode;
    private int endNode;
    private HashSet<Integer> visitedNodes;
    private PriorityQueue<Node> nodeQueue;
    private List<Integer> nodeDistances;
    private Node minNode;
    private CyclicBarrier barrier;
    private AtomicBoolean isFinished;


    public CoordinatorThread(Graph graph, int startNode, int endNode, HashSet<Integer> visitedNodes, PriorityQueue<Node> nodeQueue, List<Integer> nodeDistances, Node minNode, CyclicBarrier barrier, AtomicBoolean isFinished) {
        this.graph = graph;
        this.startNode = startNode;
        this.endNode = endNode;
        this.visitedNodes = visitedNodes;
        this.nodeQueue = nodeQueue;
        this.nodeDistances = nodeDistances;
        this.minNode = minNode;
        this.barrier = barrier;
        this.isFinished = isFinished;
    }

    @Override
    public void run() {
        try(ServerSocket serversocket = new ServerSocket(420)){
            //establish connection w/ client node
            Socket socket = serversocket.accept();

            // Setup write to client
            PrintStream printStream = new PrintStream(socket.getOutputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // Setup read from client
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            do {
                printStream.println("Init commence.");
                objectOutputStream.writeObject(new InitMessage(graph, nodeDistances, startNode, endNode));
                printStream.println("Init complete.");
            } while (bufferedReader.readLine() != "Init received.");

//            printStream.println("Main Loop started.");
////            int[] rec_visitNode = null;
////            String[] rec_priorqueue;
//            while (isFinished.get() != true) {
//                //send info
//                objectOutputStream.writeObject(nodeQueue);
//                objectOutputStream.writeObject(visitedNodes);
//                objectOutputStream.writeObject(minNode);
//
//                //wait for node response
//
////                rec_visitNode = (int[])objectInputStream.readObject();
////                rec_priorqueue = (String[])objectInputStream.readObject();
//
//                //CONVERT PRIORITY QUEUE INFO INTO PRIORITY QUEUE
////                nodeQueue = rec_priorqueue;
//
//                //wait for other threads to finish as well
//                barrier.await();
//            }
            printStream.println("EXIT");

        } catch (IOException e) {
            e.printStackTrace();
        }


//        while(!isFinished.get()) {
//            int currNode = minNode.getNode();
//            int currDistance = minNode.getDistance();
//            // Get all neighbors for the current node
//            List<Integer> currNeighbors = graph.getAdjMatrix().get(currNode);
//            // Loop through all neighbors and update distance if neccessary
//            for (int i = startNode; i < endNode; i++) {
//                if(currNeighbors.get(i) > 0 && !visitedNodes.contains(i)) {
//                    int newDistance = currDistance + currNeighbors.get(i);
//                    if (newDistance < nodeDistances.get(i)) {
//                        nodeDistances.set(i, newDistance);
//                        nodeQueue.add(new Node(i, newDistance));
//                    }
//                }
//            }
//            try {
//                //wait for all threads to finish their iteration
//                barrier.await();
//            } catch (InterruptedException | BrokenBarrierException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
