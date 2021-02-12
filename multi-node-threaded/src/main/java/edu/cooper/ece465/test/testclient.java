package edu.cooper.ece465.test;

import edu.cooper.ece465.Graph;
import edu.cooper.ece465.Node;
import edu.cooper.ece465.Util;
import edu.cooper.ece465.messages.ClientToServerMessage;
import edu.cooper.ece465.messages.InitMessage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class testclient {

    public static void main(String[] args) throws IOException {
        // need host and port, we want to connect to the ServerSocket at port 7777
        Socket socket = new Socket("localhost", 7777);
        System.out.println("Connected!");

        // get the output stream from the socket.
        OutputStream outputStream = socket.getOutputStream();
        // create an object output stream from the output stream so we can send an object through it
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        List<Integer> node = new ArrayList<>();
        node.add(2);
        node.add(4);
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(new Node(1,1));
        Graph graph = Util.readGraph("./multi-node-threaded/input.txt");

        System.out.println("Sending messages to the ServerSocket");
//        objectOutputStream.writeObject(new InitMessage(graph,node,0, 86 ));

        System.out.println("Closing socket and terminating program.");
        socket.close();
    }
}