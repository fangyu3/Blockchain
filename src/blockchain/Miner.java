package blockchain;

import java.util.Random;

public class Miner {
    private Blockchain blockchain;
    private Util utility;

    public Miner(Blockchain blockchain) {
        this.blockchain = blockchain;
        this.utility = new Util();
    }

    public boolean createBlock() {
        Random random = new Random();

        long start = System.currentTimeMillis();
        int blockchainLen = blockchain.getLength();

        int id = blockchainLen + 1;
        String prevBlockHashVal = id == 1 ?
                "0" : blockchain.getBlockList().get(blockchainLen - 1).getHashVal();
        long minerId = Thread.currentThread().getId();
        long magicNumber = random.nextLong();
        Block newBlock = new Block(id,prevBlockHashVal,magicNumber,minerId);
        long end = System.currentTimeMillis();

        // CRITICAL SECTION
        synchronized (Block.class) {
            return addToBlockchain(newBlock, (end - start) / 1000);
        }
    }

    private boolean addToBlockchain(Block newBlock, long timeElapsed) {
        if (blockchain.validateNewBlock(newBlock)) {
            blockchain.getBlockList().add(newBlock);
            System.out.println(newBlock);
            System.out.println("Block was generating for " + timeElapsed + " seconds");
            utility.updateValidNumZero(timeElapsed);
            System.out.println();
            return true;
        }
        return false;
    }
}
