package blockchain.util;

import java.security.PublicKey;
import java.security.Signature;

public class MessageVerificationUtil {

    //Method for signature verification that initializes with the Public Key,
    //updates the data to be verified and then verifies them using the signature
    public static boolean verifySignature(byte[] data, byte[] signature, PublicKey publicKey) throws Exception {
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(publicKey);
        sig.update(data);

        return sig.verify(signature);
    }
}
