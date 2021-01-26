
package glinda;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class Mining extends Thread{
    LinkedList<Transaction> transactions = new LinkedList<Transaction>();
    LinkedList<Web3Site> web3sites = new LinkedList<Web3Site>();
    LinkedList<Message> messages = new LinkedList<Message>();
    
    protected static int CLAIMED_BLOCK = 0;
    protected static int LAST_VALIDATED_BLOCK = 0;
    protected static boolean connected = false;
    protected static Connection BLOCK_PROVIDER;
    protected static BufferedWriter BLOCK_WRITER;
    protected static String lastBlock;
    private final int WAIT_FOR_CLAIMED_LENGTH_RESPOND = 2500;
    
    public Mining() throws IOException, InterruptedException{
        File blockFile = new File("./BLOCKS.dat");
        if(!blockFile.exists())
            blockFile.createNewFile();
        BLOCK_WRITER = new BufferedWriter(new FileWriter("./BLOCKS.dat"));
    }
    
    @Override
    public void run(){
        
        try{
            
            // SYNCRONIZE YOU TO NETWORK
            if(Network.connectedNodes.size() > 2){
                for(Connection c : Network.connectedNodes){
                    c.send("#BLOCK_LEN");
                    this.sleep(20);
                }

                sleep(WAIT_FOR_CLAIMED_LENGTH_RESPOND);
                
                // ASK THAT NODE TO SEND BLOCKS TO YOU
                if(BLOCK_PROVIDER != null){
                    BLOCK_PROVIDER.send("#SYNCH_REQ");
                    wait();
                    // BE NOTIFICIED WHEN SYNCH IS ACCOMPLISHED
                    // DIG ONTO IT
                }
                
            }else{
                // DIFFICULTY IS FIXED FOR SIMPLICITY
                String strToHash = null;
                int nonce = 0;
                Block b = new Block(0,"0",transactions,web3sites,messages);
                String bString = b.getBlock();
                long blockNonce = 0; // ######## GET LAST BLOCK'S NONCE ####### // UPDATE HERE AFTERWARDS
                long upperLimit = 1 << 62;
                long puzzle = -1;
                while(puzzle != -1){
                    for(nonce = 0; nonce < upperLimit; ++nonce){
                        strToHash = new String(Hash.sha256(bString)+Hash.sha256(String.valueOf(blockNonce))+Hash.sha256(String.valueOf(nonce)));
                        if(strToHash.substring(0, 10).contains("0000000000")){  // OR '<' DOESN'T MATTER MUCH 
                            puzzle = nonce;
                            break;
                        }
                    }
                    blockNonce += 1;
                }
                bString += "[" + strToHash + ":" + nonce + "]";
                if(Network.connectedNodes.size() > 2){
                    for(Connection c : Network.connectedNodes){
                        // OR c.propogate("#FOUND" + bString);
                        c.send("#FOUND" + bString);
                    }
                }
            }
            
        }catch(Exception e){
            System.out.println("#ERR /MINING .1");
        }
        
    }
}
