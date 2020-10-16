package blockchain;

import blockchain.user.Transaction;
import blockchain.util.HashUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Block {
    private int id;
    private long timestamp;
    private String hashVal;
    private String prevBlockHashVal;
    private long magicNumber;
    private long minerId;
    private HashUtil utility;
    private List<Transaction> transactions;
    private String creatorMsg;

    public Block(int id, String prevBlockHashVal, List<Transaction> transactions, long minerId) {
        this.id = id;
        this.timestamp = new Date().getTime();
        this.prevBlockHashVal = prevBlockHashVal;
        this.minerId = minerId;
        this.utility = new HashUtil();
        this.transactions = transactions;
        this.creatorMsg = "miner" + minerId + " gets 100 VC";

        String hashKey = this.id +
                this.timestamp +
                this.prevBlockHashVal +
                this.minerId +
                this.transactions.stream()
                        .map(e->e.toString())
                        .reduce((x,y)->(x+y)) +
                this.creatorMsg;

        this.hashVal = generateHashValue(hashKey);
    }

    public String getHashVal() {
        return hashVal;
    }

    public int getId() {
        return id;
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public String getPrevBlockHashVal() {
        return prevBlockHashVal;
    }

    private String generateHashValue(String hashKey) {
        String result = "";
        Random random = new Random();
        this.magicNumber = random.nextLong();

        while (true) {
            result = utility.applySha256(hashKey+magicNumber);
            if (utility.validateHashValue(result))
                break;
            magicNumber++;
        }
        return result;
    }

    @Override
    public String toString() {
        return "Block:\n" +
                "Created by miner # " + minerId + "\n" +
                creatorMsg + "\n" +
                "Id: " + id + "\n" +
                "Timestamp: " + timestamp + "\n" +
                "Magic number: " + magicNumber + "\n" +
                "Hash of the previous block:\n" + prevBlockHashVal + "\n" +
                "Hash of the block:\n" + hashVal;
    }
}