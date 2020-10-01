package blockchain;

import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    private static int numZero=0;

    /* Applies Sha256 to a string and returns a hash. */
    public static String applySha256(String input) {
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

    public static boolean validateHashValue(String hashValue) {
        Pattern pattern = Pattern.compile("0{" + numZero + "}.*");
        Matcher matcher = pattern.matcher(hashValue);

        return matcher.matches();
    }

    public static void setValidNumZero(int validNumZero) {
        numZero = validNumZero;
    }

    public static int getValidNumZero() {return numZero;}
}
