package edu.cooper.ece465;

public class WorkerMain {
    public static void main(String[] args) {

        int numThreads = 1;
        if (args.length != 2 && args.length != 3 ){
            System.out.println("java -cp multi-node-threaded/target/multi-node-threaded-0.0.1-jar-with-dependencies.jar edu.cooper.ece465.WorkerMain [IP of Coordinator] [Coordinator Port] [Optional: Number of threads]");
            System.out.println("Error: Wrong number of arguments provided.");
            return;
        }
        if (args.length == 3) {
            numThreads = Integer.parseInt(args[2]);
        }
        Worker worker = new Worker(args[0], Integer.parseInt(args[1]));

        long runtime = worker.start(numThreads);
        if (runtime != -1) {
            System.out.println("Took " + runtime);
        }
    }
}


