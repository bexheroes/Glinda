
package glinda;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public class Transaction {
    private final int COST_CONSTANT = 100;
    private final String from;
    private final String to;
    private final Integer amount;
    private final Integer fee;
    private final long reference_transaction;
    private final long reference_block;
    private final Long timestamp;
    private String sign;
    public Transaction(String privateKey, String from, String to, Integer amount, long reference_transaction, long reference_block) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException, UnsupportedEncodingException, IOException{
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.reference_transaction = reference_transaction;
        this.reference_block = reference_block;
        this.timestamp = Time.getTime();
        this.fee = calculateCost();
        this.sign = Cryptology.sign(Cryptology.stringToprivKey(privateKey), this.from + this.to + String.valueOf(this.amount) + String.valueOf(this.fee) + String.valueOf(reference_transaction) +  String.valueOf(reference_block) + String.valueOf(timestamp));
    }
    public String data(){
        return from+":"+to+":"+String.valueOf(amount)+":"+String.valueOf(fee)+":"+String.valueOf(reference_transaction)+":"+String.valueOf(reference_block)+":"+String.valueOf(timestamp)+":"+sign;
    }
    public int calculateCost(){
        return (int)(Math.ceil((new String(from+to+String.valueOf(amount)+String.valueOf(reference_transaction)+String.valueOf(reference_block)+String.valueOf(timestamp)).getBytes().length) / COST_CONSTANT));
    }
    public int getFee() throws NoSuchAlgorithmException, InvalidKeySpecException{
        return this.fee;
    }
}
