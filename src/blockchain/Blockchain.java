package blockchain;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private static int blockCount = 0;
    public static List<Block> blockChain = new ArrayList<>();

    public static Block createBlock() {
        blockCount++;
        String prevHashVal = "0";

        if (blockCount!=1)
            prevHashVal = blockChain.get(blockCount-2).getHashVal();
        Block newBlock = new Block(blockCount,prevHashVal);
        blockChain.add(newBlock);
        return newBlock;
    }

    public static boolean validate() {
        for (int i=0; i<blockChain.size()-1; i++) {
            String currHashValue = blockChain.get(i).getHashVal();
            String prevHashValue = blockChain.get(i+1).getPrevBlockHashVal();
            if (!currHashValue.equals(prevHashValue) || !Util.validateHashValue(currHashValue))
                return false;
        }
        return true;
    }
}
