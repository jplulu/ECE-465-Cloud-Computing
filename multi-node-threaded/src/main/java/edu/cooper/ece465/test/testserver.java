package edu.cooper.ece465.test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class testserver {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // don't need to specify a hostname, it will be the current machine
        ServerSocket ss = new ServerSocket(7777);
        System.out.println("ServerSocket awaiting connections...");
        Socket socket = ss.accept(); // blocking call, this will wait until a connection is attempted on this port.
        System.out.println("Connection from " + socket + "!");

        // get the input stream from the connected socket
        InputStream inputStream = socket.getInputStream();
        // create a DataInputStream so we can read data from it.
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        // read the list of messages from the socket
        while(true) {
            if (objectInputStream.readObject() == null) {
                System.out.println("null");
            }
        }
//
//        InitMessage listOfMessages = (InitMessage) objectInputStream.readObject();
//        System.out.println(listOfMessages.getGraph().getAdjMatrix());

    }
}