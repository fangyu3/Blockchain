package blockchain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Block {
    private int id;
    private long timestamp;
    private String hashVal;
    private String prevBlockHashVal;
    private long magicNumber;
    private long minerId;
    private Util utility;
    private List<String> data;

    public Block(int id,String prevBlockHashVal,long magicNumber,long minerId,List<String> data) {
        this.id = id;
        this.prevBlockHashVal = prevBlockHashVal;
        this.magicNumber = magicNumber;
        this.minerId = minerId;
        this.timestamp = new Date().getTime();
        this.utility = new Util();
        this.data = new ArrayList<>();
        setData(data);
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
        return data;
    }

    private void setData(List<String> data) {
        if (data != null)
            data.stream().forEach(e->this.data.add(e));
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
        String blockDataStrValue = "";
        if (!data.isEmpty())
            blockDataStrValue = data.stream().reduce((x,y)->x+" "+y).get();

        while (true) {
            result = utility.applySha256(id+timestamp+prevBlockHashVal+minerId+magicNumber+blockDataStrValue);
            if (utility.validateHashValue(result))
                break;
            magicNumber++;
        }
        return result;
    }

}
