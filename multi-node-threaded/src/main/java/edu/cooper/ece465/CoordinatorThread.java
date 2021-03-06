package edu.cooper.ece465;


import edu.cooper.ece465.messages.NodeMessage;
import edu.cooper.ece465.messages.InitMessage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class CoordinatorThread extends Thread{
    private Graph graph;
    private int startNode;
    private int endNode;
    private List<Integer> nodeDistances;
    private NodeMessage minNode;
    private CyclicBarrier barrier;
    private AtomicBoolean isFinished;
    private int portNumber;
    private PriorityQueue<NodeMessage> localMinNode;
    private final byte[] md5hash;
    private final String path;


    public CoordinatorThread(Graph graph, int startNode, int endNode, List<Integer> nodeDistances,
                             NodeMessage minNode, CyclicBarrier barrier, AtomicBoolean isFinished, int portNumber, PriorityQueue<NodeMessage> localMinNodes, byte[] md5Hash, String path) {
        this.graph = graph;
        this.startNode = startNode;
        this.endNode = endNode;
        this.nodeDistances = nodeDistances;
        this.minNode = minNode;
        this.barrier = barrier;
        this.isFinished = isFinished;
        this.portNumber = portNumber;
        this.localMinNode = localMinNodes;
        this.md5hash = md5Hash;
        this.path = path;
    }

    @Override
    public void run() {
        List <Integer> final_nodeDist;
        System.out.println("Establishing connection on port " + portNumber);
        try(ServerSocket serversocket = new ServerSocket(portNumber)){
            //establish connection w/ client node
            Socket socket = serversocket.accept();
            System.out.println("Connection established on port " + portNumber);

            // Setup write to client
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // Setup read from client
            ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));


            objectOutputStream.writeObject(new InitMessage(startNode, endNode, path, md5hash));
            objectOutputStream.reset();

            boolean initloop = true;
            while (!isFinished.get()) {
                if (!initloop) {
                    //send info
                    objectOutputStream.writeObject(minNode);
                    objectOutputStream.reset();
                }
                initloop = false;
                //wait for node response
                NodeMessage nodeResponse = (NodeMessage)objectInputStream.readObject();

                if (nodeResponse.getMinNode() != null) {
                    localMinNode.add(nodeResponse);
                }
                //wait for other threads to finish as well
                barrier.await();

            }

            objectOutputStream.writeObject(minNode);
            objectOutputStream.reset();

            final_nodeDist = (List<Integer>) objectInputStream.readObject();


            for (int i = startNode; i < endNode; i++) {
                nodeDistances.set(i, final_nodeDist.get(i));
            }
            socket.close();

        } catch (ClassNotFoundException | InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("I/O ERROR EXITING");
            System.exit(-1);
        }
    }
}
