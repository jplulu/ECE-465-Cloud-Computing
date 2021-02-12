package edu.cooper.ece465;

import java.io.*;
import java.net.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CoordinatorMain {
    public static void main(String[] args) throws IOException, InterruptedException {

        Graph graph = Util.readGraph("./input.txt");
        System.out.println("Starting server");


        List<Integer> results = runDijkstra(graph, 4);

        Util.writeResults("multioutput.txt", results, 0);

    }

    private static List<Integer> runDijkstra(Graph graph, int numThreads) throws InterruptedException {
        Coordinator coordinator = new Coordinator();
        return coordinator.runAlgo(graph, numThreads);
    }
}
