package blockchain;

public class User {
    private Blockchain blockchain;

    public User(Blockchain blockchain) {
        this.blockchain = blockchain;
    }

    public void sendMsg() {
        synchronized (User.class) {
            if (blockchain.getAcceptingMsgStatus()) {
                String msg = Thread.currentThread().getId() + ": blah blab";
                blockchain.addToMsgQueue(msg);
            }
        }
    }
}
