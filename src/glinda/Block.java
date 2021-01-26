
package glinda;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.LinkedList;

public class Block {
    private String blockString = " ";
    private Integer BLOCK_REWARD = 100;
    
    public Block(Integer blockId, String prevBlockHash,LinkedList<Transaction> transactions, LinkedList<Web3Site> web3sites, LinkedList<Message> messages) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException, IOException{
        
        /* # # # # # # # # # # # # # # # # # # # # # # # # # # #
        @ {header} [transactions&web3sites&messages] [blockhash&nonce]
         # # # # # # # # # # # # # # # # # # # # # # # # # # # */
        
        if(blockId == 0)
            prevBlockHash = "0000000000000000000000000000000000000000000000000000000000000000";
        
        for(Transaction t : transactions){
            BLOCK_REWARD += t.getFee();
            blockString += t.data() + "|";
        }
        blockString = blockString.substring(0, blockString.length()-1);
        blockString += "#";
        
        // + + + + + + + + + + + + + + + + + + + 
        // DON'T FORGET TO ADD ALSO OUT-INN(SPECIFIED) & MUST UPDATE
        // + + + + + + + + + + + + + + + + + + + 
        for(Web3Site w : web3sites){
            BLOCK_REWARD += w.getFee();
            blockString += w.data() + "|";
        }
        blockString = blockString.substring(0, blockString.length()-1);
        blockString += "#";
        
        // + + + + + + + + + + + + + + + + + + + 
        // DON'T FORGET TO ADD ALSO OUT-INN(SPECIFIED) & MUST UPDATE
        // + + + + + + + + + + + + + + + + + + + 
        for(Message t : messages){
            BLOCK_REWARD += t.getFee();
            blockString += t.data() + "|";
        }
        blockString = blockString.substring(0, blockString.length()-1);
        
        Transaction coinbaseTransaction = new Transaction(Wallet.privateKey, "0000000000000000000000000000000000000000000000000000000000000000", Wallet.publicKey, BLOCK_REWARD, 0, blockId);
        
        // / / / / / / / /
        // BLOCK HEADER //
        // / / / / / / / /
        blockString += "{" + blockId + ":" + prevBlockHash + ":" + Wallet.publicKey + "}?" + "[" + coinbaseTransaction.data() +blockString + "]";
        
    }
    public String getBlock(){
        return blockString;
    }
}
