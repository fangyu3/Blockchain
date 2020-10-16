package blockchain.util;

import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HashUtil {
    private static int numZero=0;

    /* Applies Sha256 to a string and returns a hash. */
    public String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            /* Applies sha256 to our input */
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte elem : hash) {
                String hex = Integer.toHexString(0xff & elem);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validateHashValue(String hashValue) {
        Pattern pattern = Pattern.compile("0{" + numZero + "}.*");
        Matcher matcher = pattern.matcher(hashValue);

        return matcher.matches();
    }

    public void updateValidNumZero(long timeElapsed) {

        if (numZero == 4) {
            numZero = 0;
            System.out.println("N was reset to 0");
        }
        else if (timeElapsed < 2)
            System.out.println("N was increased to " + ++numZero);
        else if (timeElapsed >4) {
            numZero -= 2;
            System.out.println("N was decreased to " + numZero);
        }
        else
            System.out.println("N stays the same");

        System.out.println("");

        return;
    }

    public int getNumZero() {
        return numZero;
    }
}