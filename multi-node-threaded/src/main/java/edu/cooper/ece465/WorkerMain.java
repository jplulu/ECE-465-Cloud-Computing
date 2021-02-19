package edu.cooper.ece465;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class WorkerMain {
    public static void main(String[] args) {

        if (args.length != 2 ){
            System.out.println("java -cp multi-node-threaded/target/multi-node-threaded-0.0.1-jar-with-dependencies.jar edu.cooper.ece465.WorkerMain [IP of Coordinator] [Coordinator Port]");
            System.out.println("Error: Wrong number of arguments provided.");
            return;
        }

        Worker worker = new Worker(args[0], Integer.parseInt(args[1]));

        Instant start = Instant.now;
        worker.start(4); 
        Instant end = Instant.now();
        System.out.println("Took " + Duration.between(start, end).toMillis());
    }
}


