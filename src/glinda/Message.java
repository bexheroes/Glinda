
package glinda;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public class Message {
    private final int COST_CONSTANT = 100;
    private final String from;
    private final String to;
    private final String message;
    private final long inputTransaction;
    private final long inputTransactionAmount; // REMAIN WILL BE FEE ((INPUT - FEE) UTXO AGAIN) // CAN BE REFERENCED AND SPEND AGAIN
    private final long inputTransactionBlock;
    private final Integer fee;
    private final long outputTransaction;
    private final Long timestamp;
    private final String sign;
    public Message(String privateKey, String from, String to, String message, long inputTransaction, long inputTransactionAmount, long inputTransactionBlock) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException, UnsupportedEncodingException, IOException{
        this.from = from;
        this.to = to;
        this.message = message;
        this.inputTransaction = inputTransaction;
        this.inputTransactionAmount = inputTransactionAmount;
        this.inputTransactionBlock = inputTransactionBlock;
        this.timestamp = Time.getTime();
        this.fee = calculateCost();
        outputTransaction =  inputTransactionAmount - getFee();
        this.sign = Cryptology.sign(Cryptology.stringToprivKey(privateKey), from+to+message+String.valueOf(inputTransaction)+String.valueOf(inputTransactionAmount)+String.valueOf(inputTransactionBlock)+String.valueOf(timestamp));
    }
    public String data(){
        return from+":"+to+":"+message+":"+fee+":"+String.valueOf(timestamp)+":"+sign;
    }
    public int calculateCost(){
        return (int)(Math.ceil((new String(from+to+message+String.valueOf(inputTransaction)+String.valueOf(inputTransactionAmount)+String.valueOf(inputTransactionBlock)+String.valueOf(timestamp)).getBytes().length)/ COST_CONSTANT)) ;
    }
    public int getFee(){
        return fee;
    }
}
