package blockchain;

import java.util.Date;

public class Block {
    int id;
    long timestamp;
    String hashVal;
    String prevBlockHashVal;

    public Block(int id, String prevBlockHashVal) {
        this.id = id;
        this.timestamp = new Date().getTime();
        this.hashVal = Util.applySha256(id+""+timestamp);
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
                "Hash of the previous block:\n" + prevBlockHashVal + "\n" +
                "Hash of the block:\n" + hashVal + "\n";
    }
}
