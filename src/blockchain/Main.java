package blockchain;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter how many zeros the hash must start with: ");
        String input = scanner.nextLine();

        int numZero = input.length()==0?0:Integer.parseInt(input);

        Util.setValidNumZero(numZero);

        for (int i=0; i<5; i++) {
            long start = System.currentTimeMillis();
            System.out.println(Blockchain.createBlock());
            long end = System.currentTimeMillis();
            System.out.println("Block was generating for " + (end-start)/1000 + " seconds\n");
        }
        scanner.close();
    }
}
