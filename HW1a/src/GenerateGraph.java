import java.io.*;
import java.util.Random;

public class GenerateGraph {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Proper Usage is: java GenerateGraph.java [Number of Source Nodes]");
            System.exit(1);
        }
        Random random = new Random();
        FileWriter fileWriter = new FileWriter("./HW1a/input.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        try {
            // First lines for #nodes + value of source node (default to zero)
            bufferedWriter.append(args[0] + " 0");
            bufferedWriter.newLine();

            for(int i = 0; i < Integer.parseInt(args[0]); i++){
                for(int j = 0; j < Integer.parseInt(args[0]); j++){
                    int edge = random.nextInt(100);
                    if(i==j) {
                        bufferedWriter.append("0 ");
                    }
                    else if (edge <= 50) {
                        int edgeDist = random.nextInt(200);
                        bufferedWriter.append(edgeDist + " ");
                    }
                    else {
                        bufferedWriter.append("0 ");
                    }
                }
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
