package blockchain;

import java.util.List;

// Singleton
public class ChatSimulator extends Thread {

    private static ChatSimulator instance;
    private Blockchain blockchain;
    private List<String> chats;

    private ChatSimulator(Blockchain blockchain) {
        this.blockchain = blockchain;
        this.chats = List.of(
                "Tom: Hey, I'm first!",
                "Sarah: It's not fair!",
                "Sarah: You always will be first because it is your blockchain!",
                "Sarah: Anyway, thank you for this amazing chat.",
                "Tom: You're welcome :)",
                "Nick: Hey Tom, nice chat"
        );
    }

    public static ChatSimulator getInstance(Blockchain blockchain) {
        if (instance == null) {
            instance = new ChatSimulator(blockchain);
            return instance;
        }
        return instance;
    }

    @Override
    public void run() {
        try {
            for (int i=0;i<chats.size();) {
                if(blockchain.getAcceptingMsgStatus()) {
                    blockchain.addToMsgQueue(chats.get(i));
                    i++;
                    Thread.sleep(100);
                }
            }
        }
        catch (Exception e) {
            System.out.println("Chat simulator exception");
        }
    }
}