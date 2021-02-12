package edu.cooper.ece465;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class WorkerMain {
    public static void main(String[] args) {

        Worker worker = new Worker(Integer.parseInt(args[0]));

        Instant start = Instant.now();
        worker.start();
        Instant end = Instant.now();
        System.out.println("Took " + Duration.between(start, end).toMillis());
    }
}


