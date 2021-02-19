package edu.cooper.ece465;

import edu.cooper.ece465.messages.ClientToServerMessage;
import edu.cooper.ece465.messages.InitMessage;
import edu.cooper.ece465.messages.ServerToClientMessage;
import netscape.javascript.JSObject;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
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
    private int portNumber;


    public CoordinatorThread(Graph graph, int startNode, int endNode, HashSet<Integer> visitedNodes, PriorityQueue<Node> nodeQueue, List<Integer> nodeDistances, Node minNode, CyclicBarrier barrier, AtomicBoolean isFinished, int portNumber) {
        this.graph = graph;
        this.startNode = startNode;
        this.endNode = endNode;
        this.visitedNodes = visitedNodes;
        this.nodeQueue = nodeQueue;
        this.nodeDistances = nodeDistances;
        this.minNode = minNode;
        this.barrier = barrier;
        this.isFinished = isFinished;
        this.portNumber = portNumber;
    }

    @Override
    public void run() {
        List <Integer> final_nodeDist = new ArrayList<>();
        System.out.println("Establishing connection on port " + portNumber);
        try(ServerSocket serversocket = new ServerSocket(portNumber)){
            //establish connection w/ client node
            Socket socket = serversocket.accept();
            System.out.println("Connection established on port " + portNumber);

            // Setup write to client
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // Setup read from client
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());


            objectOutputStream.writeObject(new InitMessage(graph, nodeDistances, startNode, endNode));
            objectOutputStream.reset();
            while (!isFinished.get()) {
                //send info
                objectOutputStream.writeObject(new ServerToClientMessage(minNode, visitedNodes, nodeQueue));
                objectOutputStream.reset();

                //wait for node response
                //override priority queue
                PriorityQueue<Node> tempnodeQueue = (PriorityQueue<Node>)objectInputStream.readObject();
                nodeQueue.clear();
                while (!tempnodeQueue.isEmpty()) {
                    nodeQueue.add(tempnodeQueue.remove());
                }


//                System.out.println("Thread: " + nodeQueue);
                //wait for other threads to finish as well
                barrier.await();
            }
            objectOutputStream.writeObject(new ServerToClientMessage(null, null, null));
            objectOutputStream.reset();
            //loop & update corresponding range of nodes
            final_nodeDist = (List<Integer>) objectInputStream.readObject();
//            System.out.println(final_nodeDist);
            for (int i = startNode; i < endNode; i++) {
                nodeDistances.set(i, final_nodeDist.get(i));
            }
            socket.close();
//            System.out.println("Dijkstra finished, socket closed.");

        } catch (IOException | ClassNotFoundException | InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
