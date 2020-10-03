package blockchain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// Singleton
public class Blockchain {
    private static Blockchain instance;
    public List<Block> blockList;
    private Util utility;

    private Blockchain() {
        blockList = new ArrayList<>();
        utility = new Util();
    };

    public static Blockchain getInstance() {
        if (instance == null) {
            instance = new Blockchain();
            return instance;
        }

        return instance;
    }

    public void populate() {

        int poolSize = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);

        for (int i=0; i<5; i++) {
            executor.submit(new MiningTask(instance));
        }

        executor.shutdown();

        try {
            boolean terminated = executor.awaitTermination(60, TimeUnit.SECONDS);

            if (terminated) {
                System.out.println("The executor was successfully stopped");
            } else {
                System.out.println("Timeout elapsed before termination");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean validateNewBlock(Block newBlock) {

        Block prevBlock = getLength()==0 ? null:blockList.get(getLength() - 1);
        String prevBlockHashVal = "0";
        int prevBlockId = 0;

        if (prevBlock != null) {
            prevBlockHashVal = prevBlock.getHashVal();
            prevBlockId = prevBlock.getId();
        }

        if (newBlock.getPrevBlockHashVal() == prevBlockHashVal
                && newBlock.getId() == prevBlockId + 1) {
            return true;
        }
        return false;
    }

    public boolean validateBlockchain() {
        for (int i=0; i<blockList.size()-1; i++) {
            String currHashValue = blockList.get(i).getHashVal();
            String prevHashValue = blockList.get(i+1).getPrevBlockHashVal();
            if (!currHashValue.equals(prevHashValue) || !utility.validateHashValue(currHashValue))
                return false;
        }
        return true;
    }

    public int getLength() {
        return blockList.size();
    }

    public List<Block> getBlockList() {
        return blockList;
    }
}
