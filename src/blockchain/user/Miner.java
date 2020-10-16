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

public class Miner {
    private Blockchain blockchain;
    private HashUtil utility;
    private long minerId;
    private KeyGeneratorUtil keyGenerator;

    public Miner(Blockchain blockchain) {
        try {
            this.blockchain = blockchain;
            this.utility = new HashUtil();
            this.minerId = Thread.currentThread().getId();
            this.keyGenerator = new KeyGeneratorUtil(1024,this.minerId);
            synchronized (Miner.class) {
                keyGenerator.generateKeys();
            }
        }
        catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendMessage(String msg) throws InvalidKeyException,InterruptedException,Exception {
        synchronized (Miner.class) {
            blockchain.addToMsgQueue(new Message(msg, keyGenerator.getPrivateKeyFilePath(),keyGenerator.getPublicKey()));
        }
    }

    public boolean createBlock() {
        long start = System.currentTimeMillis();
        int blockchainLen = blockchain.getLength();

        int id = blockchainLen + 1;
        String prevBlockHashVal = id==1 ? "0"
                : blockchain.getBlockList().get(blockchainLen - 1).getHashVal();
        List<Message> chats = id==1 ? new ArrayList<>()
                : blockchain.getTempMsgList();

        Block newBlock = new Block(id,prevBlockHashVal,chats,minerId);
        long end = System.currentTimeMillis();

        // CRITICAL SECTION
        synchronized (Miner.class) {
            if (addToBlockchain(newBlock, (end - start) / 1000)) {

                blockchain.isAcceptingMsg(false);

                // empty main msg queue to temp list
                blockchain.resetMsgQueue();

                blockchain.isAcceptingMsg(true);
                return true;
            }
            return false;
        }
    }

    private boolean addToBlockchain(Block newBlock, long timeElapsed) {
        if (blockchain.validateNewBlock(newBlock)) {
            blockchain.getBlockList().add(newBlock);
            System.out.println(newBlock);
            System.out.print("Block data: ");
            if (newBlock.getData().size()==0)
                System.out.println("no messages");
            else {
                System.out.println("");
                newBlock.getData()
                        .stream()
                        .forEach(e->System.out.println(e.getMsgId() + new String(e.getData().get(0), StandardCharsets.UTF_8)));
            }
            System.out.println("Block was generating for " + timeElapsed + " seconds");
            utility.updateValidNumZero(timeElapsed);
            System.out.println();
            return true;
        }
        return false;
    }
}