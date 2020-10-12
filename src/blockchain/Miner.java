package blockchain;

import java.util.List;
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
//        List<String> data = null;
//        System.out.println(blockchain.getTempMsgList());
        List<String> data = id == 1? null : blockchain.getTempMsgList();
        Block newBlock = new Block(id,prevBlockHashVal,magicNumber,minerId,data);
        long end = System.currentTimeMillis();


        // CRITICAL SECTION
        synchronized (Miner.class) {
            if (addToBlockchain(newBlock, (end - start) / 1000)) {
                // Close Msg queue and empty msg
                blockchain.isAcceptingMsg(false);
                blockchain.emptyTempMsgList();
                blockchain.emptyMsgQueue();

                // Open Msg queue to accept msg
                blockchain.isAcceptingMsg(true);
                return true;
            }
            return false;
        }
    }

    private boolean addToBlockchain(Block newBlock, long timeElapsed) {

        if (blockchain.validateNewBlock(newBlock)) {
            blockchain.getBlockList().add(newBlock);
            System.out.println(newBlock);
            // Print block content data
            System.out.print("Block data: ");
            if (newBlock.getData().isEmpty())
                System.out.println("no messages");
            else {
                System.out.println("");
                newBlock.getData().stream().forEach(System.out::println);
            }
            System.out.println("Block was generating for " + timeElapsed + " seconds");
            utility.updateValidNumZero(timeElapsed);
            System.out.println();
            return true;
        }
        return false;
    }
}
