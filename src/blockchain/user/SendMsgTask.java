package blockchain.user;

import blockchain.Blockchain;

public class SendMsgTask implements Runnable{

    private Miner miner;
    private String[] sampleMsg;
    private Blockchain blockchain;

    public SendMsgTask(Blockchain blockchain,String[] sampleMsg) {
        this.blockchain = blockchain;
        this.sampleMsg = sampleMsg;
    }


    @Override
    public void run() {
        this.miner = new Miner(blockchain);
        try {
                for (int i = 0; i < sampleMsg.length; ) {
                    if (blockchain.getAcceptingMsgStatus()) {
                        miner.sendMessage(sampleMsg[i]);
                        i++;
                        Thread.sleep(100);
                    }
                }
            }
        catch(Exception e){
                System.out.println("Chat simulator exception");
        }
    }
}
