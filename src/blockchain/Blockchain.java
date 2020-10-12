package blockchain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// Singleton
public class Blockchain {
    private static Blockchain instance;
    public List<Block> blockList;
    private Util utility;
    private Queue<String> msgQueue;
    private List<String> tempMsgList;
    private transient Boolean acceptingMsg;

    private Blockchain() {
        blockList = new ArrayList<>();
        utility = new Util();
        msgQueue = new LinkedList<>();
        tempMsgList = new ArrayList<>();
        acceptingMsg = true;
    }

    public static Blockchain getInstance() {
        if (instance == null) {
            instance = new Blockchain();
            return instance;
        }
        return instance;
    }

    public List<String> getTempMsgList() {
        return tempMsgList;
    }

    public boolean getAcceptingMsgStatus() {
        return acceptingMsg;
    }

    public void isAcceptingMsg(Boolean acceptingMsg) {
        this.acceptingMsg = acceptingMsg;
    }

    public void startApp() {

        int poolSize = Runtime.getRuntime().availableProcessors();
        ExecutorService miningTaskExecutor = Executors.newFixedThreadPool(poolSize);
        ExecutorService msgTaskExecutor = Executors.newFixedThreadPool(poolSize);

        // Blockchain starts to accept messages from users
        for (int i=0; i<5; i++) {
            msgTaskExecutor.submit(new ReceiveMsgTask(instance));
        }

        // Blockchain starts to accept block creation tasks from miners
        for (int i=0; i<5; i++) {
            miningTaskExecutor.submit(new MiningTask(instance));
        }

        miningTaskExecutor.shutdown();
        msgTaskExecutor.shutdown();

        try {
            boolean terminated =
                    miningTaskExecutor.awaitTermination(60, TimeUnit.SECONDS)
                    && msgTaskExecutor.awaitTermination(60, TimeUnit.SECONDS);

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

    public void addToMsgQueue(String msg) {
        msgQueue.add(msg);
    }

    public void emptyMsgQueue() {
        while(!msgQueue.isEmpty()) {
            tempMsgList.add(msgQueue.poll());
        }
    }

    public void emptyTempMsgList() {
        tempMsgList.clear();
    }
}
