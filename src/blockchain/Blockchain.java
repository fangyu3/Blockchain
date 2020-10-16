package blockchain;

import blockchain.user.Message;
import blockchain.user.MiningTask;
import blockchain.util.HashUtil;
import blockchain.util.MessageVerificationUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

// Singleton
public class Blockchain {
    private static Blockchain instance;
    public List<Block> blockList;
    private HashUtil utility;
    private Queue<Message> msgQueue;
    private List<Message> tempMsgList;
    private volatile Boolean acceptingMsg;

    private Blockchain() {
        blockList = new ArrayList<>();
        utility = new HashUtil();
        msgQueue = new LinkedList<>();
        tempMsgList = new ArrayList<>();
        acceptingMsg = true;

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

        // check id of new block equals +1 of id of the prev block
        // && check prevHash field of new block equals hash of prev block
        if (!newBlock.getPrevBlockHashVal().equals(prevBlockHashVal)
                && newBlock.getId() != prevBlockId + 1) {
            return false;
        }

        // Check public/private key matches for all messages in new block
        try {
            for (Message msg : newBlock.getData()) {
                if (!MessageVerificationUtil
                        .verifySignature(msg.getData().get(0), msg.getData().get(1), msg.getPublicKey())) {
                    System.out.println("Key match fail");
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println("Key verification exception in blockchain");
        }

        // Check if msg ids are valid
        List<Block> tempList = new ArrayList<>(blockList);
        tempList.add(newBlock);
        List<Message> messages = tempList.stream()
                                    .flatMap(e->e.getData().stream())
                                    .collect(Collectors.toList());

        for (int i=messages.size()-1; i>0; i--) {
            if (messages.get(i).getMsgId() < messages.get(i-1).getMsgId())
                return false;
        }

        return true;
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

    public void addToMsgQueue(Message msg) {
        msgQueue.add(msg);
    }

    public List<Message> getTempMsgList() {
        return new ArrayList<>(tempMsgList);
    }

    public void isAcceptingMsg(boolean status) {
        acceptingMsg = status;
    }

    public boolean getAcceptingMsgStatus() {
        return acceptingMsg;
    }

    public void resetMsgQueue() {
        tempMsgList.clear();
        while(!msgQueue.isEmpty()) {
            tempMsgList.add(msgQueue.poll());
        }
    }

}