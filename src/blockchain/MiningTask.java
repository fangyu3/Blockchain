package blockchain;

public class MiningTask implements Runnable{

    private final Miner miner;

    public MiningTask(Blockchain blockchain) {
        this.miner = new Miner(blockchain);
    }

    @Override
    public void run() {
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
