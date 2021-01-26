
package glinda;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Stack;

public class Wallet {
    public static String publicKey;
    public static String privateKey;
    public static HashMap<String, Double> privateKeyList = new HashMap<String, Double>();
    public Wallet() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException{
        KeyPair kp = Cryptology.createKeyPair();
        publicKey = Cryptology.pubKeyToString(kp.getPublic());
        privateKey = Cryptology.privKeyToString(kp.getPrivate());
        privateKeyList.put(privateKey, 0.0);
    }
    public void generate() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException{
        KeyPair kp = Cryptology.createKeyPair();
        publicKey = Cryptology.pubKeyToString(kp.getPublic());
        privateKey = Cryptology.privKeyToString(kp.getPrivate());
        privateKeyList.put(privateKey, 0.0);
    }
    public void clearAll() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException{
        privateKeyList = new HashMap<>();
        generate();
    }
    public void clear(String privateKey) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException{
        privateKeyList.remove((String)privateKey);
        generate();
    }
    public boolean income(String privateKey, Double amount){    // CHECK NEW MINED BLOCKS AND UPDATE BALANCE IN WALLET IF ANY INCOME
        Double balance = privateKeyList.get((String)privateKey);
        balance += amount;
        privateKeyList.put(privateKey, balance);
        return true;
    }
    public boolean outcome(String privateKey, Double amount){    // IF YOU SEND ANY MONEY UPDATE YOUR BALANCE IN WALLET
        Double balance = privateKeyList.get((String)privateKey);
        balance -= amount;
        if(balance < 0)
            return false;   // INVALID TRANSACTION
        privateKeyList.put(privateKey, balance);
        return true;
    }
    public boolean check(String privateKey){ // APPLY FOR ALL TRANSACTIONS IN THE BLOCK
        return privateKeyList.containsKey(privateKey);
    }
}
