package blockchain;

public class ReceiveMsgTask implements Runnable{
    private final User user;

    public ReceiveMsgTask(Blockchain blockchain) {this.user = new User(blockchain);}

    @Override
    public void run() {
        try {
//            while(true) {
                user.sendMsg();
//                System.out.println("sending");
//            }
//            for (int i=0; i<=2; i++)
//                user.sendMsg();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
