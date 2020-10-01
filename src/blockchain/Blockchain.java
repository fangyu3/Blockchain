package blockchain;

import java.util.ArrayList;
import java.util.List;

// Singleton
public class Blockchain {
    private static Blockchain instance;
    public List<Block> blockList;

    private Blockchain() {
        blockList = new ArrayList<>();
    };

    public static Blockchain getInstance() {
        if (instance == null)
            return new Blockchain();

        return instance;
    }

    public Block createBlock() {

        int newBlockId = getLength() + 1;
        String prevHashVal = newBlockId==1 ?
                            "0":blockList.get(getLength()-1).getHashVal();

        Block newBlock = new Block(newBlockId,prevHashVal);

        // CRITICAL SECTION
        synchronized (this){
            if (validateNewBlock(newBlock))
                blockList.add(newBlock);
            else
                newBlock = null;

            return newBlock;
        }

    }

    private boolean validateNewBlock(Block newBlock) {

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
            if (!currHashValue.equals(prevHashValue) || !Util.validateHashValue(currHashValue))
                return false;
        }
        return true;
    }

    private int getLength() {
        return blockList.size();
    }
}
