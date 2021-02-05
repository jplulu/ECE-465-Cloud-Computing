package edu.cooper.ece465;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Util {
    // Read an input file and return an adjacency matrix graph object
    public static Graph readGraph(String fileName) throws IOException {
        List<List<Integer>> adjMatrix = new ArrayList<>();
        int sourceNode;
        int numNodes;

        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        String line = bufferedReader.readLine();
        if (line != null) {
            String[] params = line.split(" ");
            numNodes = Integer.parseInt(params[0]);
            sourceNode = Integer.parseInt(params[1]);
        } else {
            System.out.println("Input file must specify the number of nodes and the source node");
            return null;
        }
        while ((line = bufferedReader.readLine()) != null) {
            List<Integer> row = new ArrayList<>(numNodes);
            String[] params = line.split(" ");
            if (params.length == numNodes) {
                for (String val : params) {
                    row.add(Integer.parseInt(val));
                }
            } else {
                System.out.println("Invalid adjacency matrix");
                return null;
            }
            adjMatrix.add(row);
        }

        return new Graph(sourceNode, adjMatrix);
    }
    
    // Write the results of the algorithm to a file
    public static void writeResults(String fileName, List<Integer> results) {
        try (PrintWriter writer = new PrintWriter(fileName, StandardCharsets.UTF_8)) {
            for (int i = 0; i < results.size(); i++) {
                int val = results.get(i);
                if (val < Integer.MAX_VALUE) {
                    writer.printf("Minimum distance to node %d is %d\n", i, val);
                } else {
                    writer.printf("Minimum distance to node %d is inf\n", i);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
