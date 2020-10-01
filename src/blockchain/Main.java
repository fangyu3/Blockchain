package blockchain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        Blockchain myBlockchain = Blockchain.getInstance();
        int poolSize = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);

        for (int i=0; i<5; i++) {
            executor.submit(new MiningTask(myBlockchain));
        }

        executor.shutdown();
    }
}
