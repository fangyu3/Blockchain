package blockchain;

import java.util.ArrayList;
import java.util.List;

public class Miner {
    private Blockchain blockchain;
    private Util utility;

    public Miner(Blockchain blockchain) {
        this.blockchain = blockchain;
        this.utility = new Util();
    }

    public boolean createBlock() {

        long start = System.currentTimeMillis();
        int blockchainLen = blockchain.getLength();

        int id = blockchainLen + 1;
        String prevBlockHashVal = id==1 ? "0"
                : blockchain.getBlockList().get(blockchainLen - 1).getHashVal();
        List<String> chats = id==1 ? new ArrayList<>()
                : blockchain.getTempMsgList();

        Block newBlock = new Block(id,prevBlockHashVal,chats);
        long end = System.currentTimeMillis();

        // CRITICAL SECTION
        synchronized (Miner.class) {
            if (addToBlockchain(newBlock, (end - start) / 1000)) {

                blockchain.isAcceptingMsg(false);

                // empty main msg queue to temp list
                blockchain.resetMsgQueue();

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
            System.out.print("Block data: ");
            if (newBlock.getData().size()==0)
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