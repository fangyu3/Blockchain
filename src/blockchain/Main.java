package blockchain;

public class Main {
    public static void main(String[] args) {
        Blockchain myBlockchain = Blockchain.getInstance();
        ChatSimulator myChatSimulator = ChatSimulator.getInstance(myBlockchain);
        myChatSimulator.start();
        myBlockchain.populate();
    }
}