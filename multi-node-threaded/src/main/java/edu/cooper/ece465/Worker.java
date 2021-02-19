package edu.cooper.ece465;

import edu.cooper.ece465.messages.ClientToServerMessage;
import edu.cooper.ece465.messages.InitMessage;
import edu.cooper.ece465.messages.ServerToClientMessage;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class Worker {
    private Graph graph;
    private int startNode;
    private int endNode;
    private Node currNode;
    private List<Integer> nodeDistances;
    private HashSet<Integer> visitedNodes = new HashSet<>();
    private List<PriorityQueue<Node>> nodeQueue = new ArrayList<>();
    // Using an atomicboolean mutable so we can change the value in the threads
    private AtomicBoolean isFinished;
    private final int portNumber;


    public Worker(int portNumber) {
        this.portNumber = portNumber;
    }

    public void start(int numThreads) {
        System.out.println("Worker started on port " + portNumber);
        try(Socket s = new Socket("localhost", portNumber)){
            System.out.println("Connection establish with server on port " + portNumber);
            ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(s.getOutputStream());

            init(objectInputStream, numThreads);
            List<Thread> threads = new ArrayList<>();

            FindMinNodeLocal findMinNodeLocal = new FindMinNodeLocal(nodeQueue, isFinished, visitedNodes, currNode, startNode, objectInputStream, objectOutputStream);
            CyclicBarrier cyclicBarrier = new CyclicBarrier(numThreads, findMinNodeLocal);

            int subGraphSize = (endNode - startNode) / numThreads;
            int excessNodes = (endNode - startNode) % numThreads;
            // Create + Start threads
            for (int i = startNode; i < numThreads; i++) {
                endNode = startNode + subGraphSize;
                // if there are excess nodes after distributing, then some subgraphs get one node until all excess nodes gone
                if (excessNodes > 0) {
                    endNode = endNode + 1;
                    excessNodes = excessNodes - 1;
                }

                // Create new thread and run dijkstra on subgraph
                Thread DThread = new WorkerThread(graph, startNode, endNode, visitedNodes, nodeQueue.get(i), nodeDistances,
                        currNode, cyclicBarrier, isFinished);
                DThread.start();
                // Add new thread to queue
                threads.add(DThread);

                // Update startNode to w/e endNode was w/ one node overlap in new subgraphs to later connect them together
                startNode = endNode;
            }
            // Sync threads
            for (int i = 0; i < numThreads; i++){
                threads.get(i).join();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void init(ObjectInputStream objectInputStream, int numThreads) {
        boolean initDone = false;
        InitMessage initData;

        while(!initDone) {
            try {
                initData = (InitMessage) objectInputStream.readObject();
                this.graph = initData.getGraph();
                this.startNode = initData.getStartNode();
                this.endNode = initData.getEndNode();
                this.isFinished = new AtomicBoolean(false);
                this.nodeDistances = new ArrayList<>(graph.getNumNodes());
                this.currNode = new Node(graph.getSourceNode(), 0);
                // Preset distances to infinity
                for (int i = 0; i < graph.getNumNodes(); i++)
                    this.nodeDistances.add(i, Integer.MAX_VALUE);
                // Set starting node distance to 0
                this.nodeDistances.set(graph.getSourceNode(), 0);
                for (int i = 0; i < numThreads; i++) {
                    PriorityQueue<Node> threadQueue = new PriorityQueue<>();
                    this.nodeQueue.add(threadQueue);
                }
                initDone = true;
                System.out.println("Worker initialized");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static class FindMinNodeLocal implements Runnable {

        private List<PriorityQueue<Node>> nodeQueue;
        private AtomicBoolean isFinished;
        private Set<Integer> visitedNodes;
        private Node currNode;
        private int startNode;
        private ObjectInputStream objectInputStream;
        private ObjectOutputStream objectOutputStream;

        public FindMinNodeLocal(List<PriorityQueue<Node>> nodeQueue, AtomicBoolean isFinished, Set<Integer> visitedNodes, Node currNode, int startNode, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
            this.nodeQueue = nodeQueue;
            this.isFinished = isFinished;
            this.visitedNodes = visitedNodes;
            this.currNode = currNode;
            this.startNode = startNode;
            this.objectInputStream = objectInputStream;
            this.objectOutputStream = objectOutputStream;
        }

        @Override
        public void run() {
            while (true) {
                Node minNode = null;
                int index = 0;

                for (int i = 0; i < nodeQueue.size(); i++){
                    // if thread's queue is not empty, get first node in priorityqueue which will be node w/ smallest dist
//                    System.out.println("Coordinator: " + nodeQueue.get(i));
                    if (!nodeQueue.get(i).isEmpty()) {
                        Node node = nodeQueue.get(i).peek();
                        //if minNode not found or current node smaller than minNode, set minNode as current node
                        if (minNode == null || Objects.requireNonNull(node).compareTo(minNode) < 0) {
                            minNode = node;
                            index = i;
                        }
                    }
                }
                // if minNode not found b/c queues are empty, the algorithm is finished
                if(minNode == null || !visitedNodes.contains(minNode.getNode())) {
                    // min node found and not yet visited, set currNode as minNode then remove minNode from the queue
                    try {
                        objectOutputStream.writeObject(new ClientToServerMessage(minNode, startNode));
                        objectOutputStream.reset();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    visitedNodes.add(minNode.getNode());
                    currNode.setNode(minNode.getNode());
                    currNode.setDistance(minNode.getDistance());
                    nodeQueue.get(index).remove();
                    return;
                }
                else {
                    // min node found but visited already, remove
                    nodeQueue.get(index).remove();
                }

            }
        }
    }
}
