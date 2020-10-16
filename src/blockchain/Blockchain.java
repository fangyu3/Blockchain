package blockchain;

import blockchain.user.Miner;
import blockchain.user.Transaction;
import blockchain.util.TransactionVerificationUtil;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Blockchain {

    private static List<Block> entries;
    private static List<Transaction> newBlockTransactions;
    private static Queue<Transaction> transactionsQueue;
    private static volatile boolean acceptNewTrx;
    private static Map<Long, Miner> registeredMiners;

    static {
        entries = new ArrayList<>();
        newBlockTransactions = new ArrayList<>();
        transactionsQueue = new LinkedList<>();
        registeredMiners = new HashMap<>();
        acceptNewTrx = true;
    }

    public static boolean isAcceptNewTrx() {
        return acceptNewTrx;
    }

    public static int getLength() {
        return entries.size();
    }

    public static List<Block> getEntries() {
        return new ArrayList<>(entries);
    }

    public static Map<Long, Miner> getRegisteredMiners() {
        return new HashMap<>(registeredMiners);
    }

    public static List<Transaction> getNewBlockTransactions() {
        return new ArrayList<>(newBlockTransactions);
    }

    public static void addMiner(Miner newMiner) {
        registeredMiners.put(newMiner.getId(),newMiner);
    }

    public static void addBlock(Block newBlock) {
        entries.add(newBlock);
    }

    public static void addTransaction(Transaction newTransaction) {
        transactionsQueue.add(newTransaction);
    }

    public static void startApp() {
        int poolSize = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);

        for (int i=0; i<poolSize; i++) {
            executor.submit(new CreateMinerTask());
        }

        executor.shutdown();

        try {
            boolean terminated = executor.awaitTermination(15, TimeUnit.SECONDS);

            if (terminated) {
                System.out.println("The executor was successfully stopped");
            } else {
                System.out.println("Timeout elapsed before termination");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void resetTrxQueue() {
        acceptNewTrx = false;

        newBlockTransactions.clear();
        while(!transactionsQueue.isEmpty()) {
            newBlockTransactions.add(transactionsQueue.poll());
        }

        acceptNewTrx = true;
    }

    public static boolean validateNewBlock(Block newBlock) {

        Block prevBlock = getLength()==0 ? null:entries.get(getLength() - 1);
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

        return validateTransactions(newBlock);
    }

    private static boolean validateTransactions(Block newBlock) {
        // Check public/private key matches for all messages in new block
        try {
            for (Transaction trx : newBlock.getTransactions()) {
                if (!TransactionVerificationUtil
                        .verifySignature(trx.getData().get(0),
                                trx.getData().get(1),
                                trx.getPublicKey()))
                {
                    System.out.println("Key match fail");
                    return false;
                }
            }

            // Check if msg ids are valid
            List<Block> tempList = new ArrayList<>(entries);
            tempList.add(newBlock);
            List<Transaction> transactions = tempList.stream()
                    .flatMap(e->e.getTransactions().stream())
                    .collect(Collectors.toList());

            for (int i=transactions.size()-1; i>0; i--) {
                if (transactions.get(i).getTransactionId() < transactions.get(i-1).getTransactionId())
                    return false;
            }
        }
        catch (Exception e) {
            System.out.println("Transaction verification exception in blockchain");
        }
        return true;
    }
}