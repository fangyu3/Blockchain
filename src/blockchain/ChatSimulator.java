package blockchain;

import blockchain.user.SendMsgTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Singleton
public class ChatSimulator {

    private static ChatSimulator instance;
    private Blockchain blockchain;
    private String[] chats;

    private ChatSimulator(Blockchain blockchain) {
        this.blockchain = blockchain;
        this.chats = new String[]{
                "Hey, I'm first!",
                "It's not fair!",
                "You always will be first because it is your blockchain!",
                "Anyway, thank you for this amazing chat.",
                "You're welcome :)",
                "Hey Tom, nice chat"
        };
    }

    public static ChatSimulator getInstance(Blockchain blockchain) {
        if (instance == null) {
            instance = new ChatSimulator(blockchain);
            return instance;
        }
        return instance;
    }

    public void start() {
        int poolSize = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);

        for (int i=0; i<5; i++) {
            executor.submit(new SendMsgTask(blockchain,chats));
        }

        executor.shutdown();

//        try {
//            boolean terminated = executor.awaitTermination(60, TimeUnit.SECONDS);
//
//            if (terminated) {
//                System.out.println("The executor was successfully stopped");
//            } else {
//                System.out.println("Timeout elapsed before termination");
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
    }

//    @Override
//    public void run() {
//        try {
//            for (int i=0;i<chats.size();) {
//                if(blockchain.getAcceptingMsgStatus()) {
//                    blockchain.addToMsgQueue(chats.get(i));
//                    i++;
//                    Thread.sleep(100);
//                }
//            }
//        }
//        catch (Exception e) {
//            System.out.println("Chat simulator exception");
//        }
//    }
}