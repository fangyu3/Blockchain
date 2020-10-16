package blockchain.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;

public class KeyGeneratorUtil {
    private KeyPairGenerator keyGen;
    private KeyPair pair;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private long ownerId;
    private final String privateKeyFilePath;

    public KeyGeneratorUtil(int keylength, long ownerId) throws NoSuchAlgorithmException, NoSuchProviderException {
        this.keyGen = KeyPairGenerator.getInstance("RSA");
        this.keyGen.initialize(keylength);
        this.ownerId = ownerId;
        this.privateKeyFilePath = "KeyPair/privateKey" + ownerId;
    }

    private void createKeys() {
        this.pair = this.keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public String getPrivateKeyFilePath() {
        return privateKeyFilePath;
    }

    private PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    private void writeToFile(String path, byte[] key) throws IOException {
        File f = new File(path);
        f.getParentFile().mkdirs();

        FileOutputStream fos = new FileOutputStream(f);
        fos.write(key);
        fos.flush();
        fos.close();
    }

    public void generateKeys() {
        try {
            createKeys();
            writeToFile(privateKeyFilePath, getPrivateKey().getEncoded());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
