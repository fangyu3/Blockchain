package blockchain.user;

import java.io.File;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

public class Message {
    private volatile static int count=0;
    private List<byte[]> data;
    private int msgId;
    private PublicKey publicKey;

    public List<byte[]> getData() {
        return data;
    }

    public int getMsgId() {
        return msgId;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    //The constructor of Message class builds the list that will be written to the file.
    //The list consists of the message and the signature.
    public Message(String data, String keyFile, PublicKey publicKey) throws InvalidKeyException, Exception {
        this.data = new ArrayList<byte[]>();
        this.data.add(data.getBytes());
        this.data.add(sign(data, keyFile));
        this.publicKey = publicKey;
        this.msgId = ++count;
    }

    //The method that signs the data using the private key that is stored in keyFile path
    public byte[] sign(String data, String keyFile) throws InvalidKeyException, Exception{
        Signature rsa = Signature.getInstance("SHA1withRSA");
        rsa.initSign(getPrivate(keyFile));
        rsa.update(data.getBytes());
        return rsa.sign();
    }

    //Method to retrieve the Private Key from a file
    public PrivateKey getPrivate(String filename) throws Exception {
        byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }
}
