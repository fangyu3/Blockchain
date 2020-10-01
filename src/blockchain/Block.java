package blockchain;

import java.util.Date;
import java.util.Random;

public class Block {
    private int id;
    private long timestamp;
    private String hashVal;
    private String prevBlockHashVal;
    private Long magicNumber;

    public Block(int id, String prevBlockHashVal) {
        this.id = id;
        this.timestamp = new Date().getTime();
        this.hashVal = generateHashValue(id+timestamp+prevBlockHashVal);
        this.prevBlockHashVal = prevBlockHashVal;
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

    @Override
    public String toString() {
        return "Block:\n" +
                "Id: " + id + "\n" +
                "Timestamp: " + timestamp + "\n" +
                "Magic number: " + magicNumber + "\n" +
                "Hash of the previous block:\n" + prevBlockHashVal + "\n" +
                "Hash of the block:\n" + hashVal;
    }

    private String generateHashValue(String input) {
        String result = "";
        Random random = new Random();

        while (true) {
            magicNumber = random.nextLong();
            input = input+magicNumber;
            result = Util.applySha256(input);
            if (Util.validateHashValue(result))
                break;
        }
        return result;
    }
}
