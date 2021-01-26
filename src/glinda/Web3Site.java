
package glinda;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public class Web3Site {
    private final int COST_CONSTANT = 100;
    private final String domain;
    private final String owner;
    private final String code;
    private final long inputTransaction;
    private final long inputTransactionAmount; // REMAIN WILL BE FEE ((INPUT - FEE) UTXO AGAIN) // CAN BE REFERENCED AND SPEND AGAIN
    private final long inputTransactionBlock;
    private final int fee;
    private final long outputTransaction;
    private final Long timestamp;
    private final String sign;
    public Web3Site(String privateKey, String domain, String owner, String code, long inputTransaction, long inputTransactionAmount, long inputTransactionBlock) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException, InvalidKeySpecException, IOException{
        this.domain = domain;
        this.owner = owner;
        this.code = code;
        this.inputTransaction = inputTransaction;
        this.inputTransactionAmount = inputTransactionAmount;
        this.inputTransactionBlock = inputTransactionBlock;
        this.timestamp = Time.getTime();
        this.fee = calculateCost();
        this.outputTransaction = inputTransactionAmount - getFee();
        this.sign = Cryptology.sign(Cryptology.stringToprivKey(privateKey),domain+owner+code+String.valueOf(inputTransaction)+String.valueOf(inputTransactionAmount)+String.valueOf(inputTransactionBlock)+String.valueOf(timestamp));
    }
    public String data(){
        return domain+":"+owner+":"+code+":"+fee+":"+":"+timestamp+":"+sign;
    }
    public int calculateCost(){
        return (int)(Math.ceil((new String(domain+owner+code+String.valueOf(inputTransaction)+String.valueOf(inputTransactionAmount)+String.valueOf(inputTransactionBlock)+String.valueOf(fee)+String.valueOf(timestamp)).getBytes().length)/COST_CONSTANT));
    }
    public int getFee(){
        return fee;
    }
}
