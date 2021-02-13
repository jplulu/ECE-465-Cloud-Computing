package edu.cooper.ece465;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
//        List<List<Long>> timediff = new ArrayList<>();
//        List<Long> oneThreadresults = new ArrayList<>();
//        List<Long> twoThreadresults = new ArrayList<>();
//        List<Long> fourThreadresults = new ArrayList<>();
//        timediff.add(oneThreadresults);
//        timediff.add(twoThreadresults);
//        timediff.add(fourThreadresults);
//        List<Integer> numNodeslist = new ArrayList<>();
//        System.out.println("# Nodes    1 Thread    2 Thread    4 Thread");
//        for (int numNodes = 1000; numNodes <= 7000; numNodes += 1000) {
//            String inputFilename = GenerateGraph.genGraph(numNodes);
//            Graph graph = Util.readGraph("./single-node-multithreaded/input/" + inputFilename);
//            Dijkstra dijkstra = new Dijkstra();
//            // Run with 1 thread
//            Instant start = Instant.now();
//            List<Integer> results = runDijkstra(graph, 1);
//            Instant end = Instant.now();
//            long OneThread = Duration.between(start, end).toMillis();
//            Util.writeResults("output_one" + numNodes + ".txt", results, OneThread);
//            oneThreadresults.add(OneThread);
//            // Run with 2 threads
//            start = Instant.now();
//            results = runDijkstra(graph, 2);
//            end = Instant.now();
//            long TwoThreads = Duration.between(start, end).toMillis();
//            Util.writeResults("output_two" + numNodes + ".txt", results, TwoThreads);
//            twoThreadresults.add(TwoThreads);
//            // Run with 4 threads
//            start = Instant.now();
//            results = runDijkstra(graph, 4);
//            end = Instant.now();
//            long FourThreads = Duration.between(start, end).toMillis();
//            Util.writeResults("output_four" + numNodes + ".txt", results, FourThreads);
//            fourThreadresults.add(FourThreads);
//
//            numNodeslist.add(numNodes);
//            System.out.format("%7d    %8d    %8d    %8d\n", numNodes, OneThread, TwoThreads, FourThreads);
//        }
//        Util.writeRuntime("runtime_outputs.txt", timediff, numNodeslist);
        Graph graph = Util.readGraph("./input.txt");


        List<Integer> results = runDijkstra(graph, 2);

        Util.writeResults("singleoutput.txt", results, 0);
    }

    private static List<Integer> runDijkstra(Graph graph, int numThreads) throws InterruptedException {
        Dijkstra dijkstra = new Dijkstra();
        return dijkstra.runAlgo(graph, numThreads);
    }
}
