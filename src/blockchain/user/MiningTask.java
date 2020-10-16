package blockchain.user;

import blockchain.Blockchain;

public class MiningTask implements Runnable{

    private Miner miner;
    private Blockchain blockchain;

    public MiningTask(Blockchain blockchain) {
        this.blockchain = blockchain;
    }

    @Override
    public void run() {
        miner = new Miner(blockchain);
        try {
            while(!miner.createBlock()) {
                ;
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}