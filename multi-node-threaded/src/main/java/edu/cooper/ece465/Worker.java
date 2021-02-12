package edu.cooper.ece465;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class Worker {
    private Graph graph;
    private int startNode;
    private int endNode;
    private Set<Integer> visitedNodes;
    private PriorityQueue<Node> nodeQueue;
    private List<Integer> nodeDistances;
    private Node minNode;

    public static void main(String[] args) {
        try(Socket s = new Socket("localhost", 420)){
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

            boolean initDone = false;
            String initData;

            while(!initDone) {
                if ((initData = br.readLine()) != null) {
                    JSONObject jsonObject = new JSONObject(initData);
                    
                    initDone = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
