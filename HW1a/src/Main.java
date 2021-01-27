import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Graph graph = Util.readGraph("./HW1a/input.txt");
        Dijkstra dijkstra = new Dijkstra();
        List<Integer> results = runDijkstra(graph, 4);
        System.out.println("DONE");
        System.out.println(results);
        Util.writeResults("output.txt", results);
    }

    private static List<Integer> runDijkstra(Graph graph, int numThreads) throws InterruptedException {
        Dijkstra dijkstra = new Dijkstra();
        return dijkstra.runAlgo(graph, numThreads);
    }
}