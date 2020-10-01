package blockchain;

public class MiningTask implements Runnable{

    private final Blockchain blockchain;

    public MiningTask(Blockchain blockchain) {
        this.blockchain = blockchain;
    }

    @Override
    public void run() {
        try {
            while (true) {
                long start = System.currentTimeMillis();
                Block newBlock = blockchain.createBlock();
                long end = System.currentTimeMillis();

                if (newBlock != null) {
                    long timeElapsed = (end - start) / 1000;
                    System.out.println(newBlock);
                    System.out.println("Block was generating for " + timeElapsed + " seconds\n");

                    if (timeElapsed < 15)
                        Util.setValidNumZero(Util.getValidNumZero() + 1);
                    else if (timeElapsed > 30)
                        Util.setValidNumZero(Util.getValidNumZero() - 1);

                    break;
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
