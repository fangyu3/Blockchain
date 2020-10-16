package blockchain;

import blockchain.user.Miner;

public class CreateMinerTask implements Runnable{
    private Miner miner;

    @Override
    public void run() {

        try {
            miner = new Miner();
            Blockchain.addMiner(miner);
            // Keep running until 15 blocks in blockchain
            while (Blockchain.getLength() != 15) {
                // Mine for block
                miner.createBlock();
                // Perform transaction
                miner.performTransaction();
            }
        }
        catch (Exception e) {
            System.out.println("Miner exception");
        }
    }
}
