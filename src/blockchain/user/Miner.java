package blockchain.user;

import blockchain.Block;
import blockchain.Blockchain;
import blockchain.util.HashUtil;
import blockchain.util.KeyGeneratorUtil;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Miner {

    private long minerId;
    private HashUtil utility;
    private int balance;
    private KeyGeneratorUtil keyGenerator;

    public Miner() throws NoSuchAlgorithmException, NoSuchProviderException {
        minerId = Thread.currentThread().getId();
        utility = new HashUtil();
        balance = 100;
        keyGenerator = new KeyGeneratorUtil(1024,minerId);
        synchronized (Miner.class) {
            keyGenerator.generateKeys();
        }
    }

    public int getBalance() {
        return balance;
    }

    public long getId() {
        return minerId;
    }

    public boolean createBlock() {
        long start = System.currentTimeMillis();
        int blockchainLen = Blockchain.getLength();

        int id = blockchainLen + 1;
        String prevBlockHashVal = id==1 ? "0"
                : Blockchain.getEntries().get(blockchainLen - 1).getHashVal();

        List<Transaction> newBlockTransactions = id==1 ? new ArrayList<>()
                : Blockchain.getNewBlockTransactions();

        Block newBlock = new Block(id,prevBlockHashVal,newBlockTransactions,minerId);
        long end = System.currentTimeMillis();

        // CRITICAL SECTION
        synchronized (Miner.class) {
            if (addToBlockchain(newBlock,(end - start) / 1000)) {
                // empty main Trx queue to new Node Trx list
                Blockchain.resetTrxQueue();
                return true;
            }
            return false;
        }
    }

    private boolean addToBlockchain(Block newBlock, long timeElapsed) {
        if (Blockchain.validateNewBlock(newBlock)) {
            Blockchain.addBlock(newBlock);
            System.out.println(newBlock);
            System.out.print("Block data: ");

            if (newBlock.getTransactions().size()==0)
                System.out.println("no transactions");
            else {
                System.out.println("");
                newBlock.getTransactions()
                        .stream()
                        .forEach(e->System.out.println(e.getTransactionId() + " " +
                                new String(e.getData().get(0), StandardCharsets.UTF_8)));
            }

            System.out.println("Block was generating for " + timeElapsed + " seconds");
            utility.updateValidNumZero(timeElapsed);
            balance += 100;
            return true;
        }
        return false;
    }

    public void performTransaction() throws InvalidKeyException,Exception {
        synchronized (Miner.class) {
            List<Long> minerIds = new ArrayList<>(Blockchain.getRegisteredMiners().keySet());
            Random random = new Random();

            for (int i = 0; i < 2; ) {
                if (Blockchain.isAcceptNewTrx()) {
                    Long receiverId = minerIds.get(random.nextInt(minerIds.size()));
                    int amountSent = random.nextInt(balance);

                    if (!decreaseBalance(amountSent))
                        amountSent = 0;

                    Blockchain.getRegisteredMiners().get(receiverId).increaseBalance(amountSent);

                    String record = "Miner #" + minerId + " sent $" + amountSent + " to Miner #" + receiverId;
                    Transaction trx = new Transaction(record, keyGenerator.getPrivateKeyFilePath(), keyGenerator.getPublicKey());
                    Blockchain.addTransaction(trx);
                    i++;
                }
            }
        }
    }

    public boolean decreaseBalance(int amount) {

        if (amount > balance)
            return false;

        balance -= amount;
        return true;
    }

    public void increaseBalance(int amount) {
        balance += amount;
    }
}