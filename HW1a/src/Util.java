import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Util {
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
}
