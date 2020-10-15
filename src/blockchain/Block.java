package blockchain;

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
    private Util utility;
    private List<String> data;

    public Block(int id,String prevBlockHashVal,List<String> data) {
        Random random = new Random();

        this.id = id;
        this.prevBlockHashVal = prevBlockHashVal;
        this.timestamp = new Date().getTime();
        this.utility = new Util();
        this.minerId = Thread.currentThread().getId();
        this.magicNumber = random.nextLong();
        this.data = data;
        this.hashVal = generateHashValue();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getHashVal() {
        return hashVal;
    }

    public void setHashVal(String hashVal) {
        this.hashVal = hashVal;
    }

    public String getPrevBlockHashVal() {
        return prevBlockHashVal;
    }

    public void setPrevBlockHashVal(String prevBlockHashVal) {
        this.prevBlockHashVal = prevBlockHashVal;
    }

    public List<String> getData() {
        return new ArrayList<>(data);
    }

    @Override
    public String toString() {
        return "Block:\n" +
                "Created by miner # " + minerId + "\n" +
                "Id: " + id + "\n" +
                "Timestamp: " + timestamp + "\n" +
                "Magic number: " + magicNumber + "\n" +
                "Hash of the previous block:\n" + prevBlockHashVal + "\n" +
                "Hash of the block:\n" + hashVal;
    }

    private String generateHashValue() {
        String result = "";
        String dataStrValue = data.stream().reduce((x,y)->x+y).orElse("");
        String hashKey = id+timestamp+prevBlockHashVal+minerId+dataStrValue;
        while (true) {
            result = utility.applySha256(hashKey+magicNumber);
            if (utility.validateHashValue(result))
                break;
            magicNumber++;
        }
        return result;
    }
}