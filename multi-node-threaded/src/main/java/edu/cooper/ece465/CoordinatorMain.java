package edu.cooper.ece465;

import java.io.*;
import java.util.List;

public class CoordinatorMain {
    public static void main(String[] args) throws IOException, InterruptedException {

        if (args.length == 0 ){
            System.out.println("Proper use of Coordinator: java -cp multi-node-threaded/target/multi-node-threaded-0.0.1-jar-with-dependencies.jar edu.cooper.ece465.CoordinatorMain [Number of nodes] [Ports corresponding to # of nodes]");
            System.out.println("Error: No arguments provided.");
            return;
        }
        int numNodes = Integer.parseInt(args[0]);
        if(args.length != numNodes+1) {
            System.out.println("Proper use of Coordinator: java -cp multi-node-threaded/target/multi-node-threaded-0.0.1-jar-with-dependencies.jar edu.cooper.ece465.CoordinatorMain [Number of nodes] [Ports corresponding to # of nodes]");
            System.out.println("Error: Number of ports do not correspond to number of nodes.");
            return;
        }

        int[] portList = new int[args.length-1];
        for (int i=1; i < args.length; i++){
            portList[i-1] = Integer.parseInt(args[i]);
        }



        Graph graph = Util.readGraph("./input.txt");
        System.out.println("Starting server");

        List<Integer> results = runDijkstra(graph, numNodes, portList);

        Util.writeResults("multioutput.txt", results, 0);

    }

    private static List<Integer> runDijkstra(Graph graph, int numThreads, int[] portList) throws InterruptedException {
        Coordinator coordinator = new Coordinator();
        return coordinator.runAlgo(graph, numThreads, portList);
    }
}
