
package glinda;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Network {
    final static LinkedList<Connection> connectedNodes = new LinkedList<>();
    final static LinkedList<Connection> accessibleNodes = new LinkedList<>();
    
    public Network(){
        
        Server server = new Server(connectedNodes, accessibleNodes);
        server.start();
        
        Client client = new Client(connectedNodes, accessibleNodes);
        client.start();
        
        DetermineDisconnectedNodes determineDN = new DetermineDisconnectedNodes(connectedNodes, accessibleNodes);
        determineDN.start();
        
    }
    
}

class DetermineDisconnectedNodes extends Thread{
    final private LinkedList<Connection> connectedNodes;
    final private LinkedList<Connection> accessibleNodes;
    final private LinkedList<Connection> temporaryNodes = new LinkedList<>();
    final int SLEEP_FOR_NEXT_CHECK = 4800;
    public DetermineDisconnectedNodes(LinkedList<Connection> connectedNodes,LinkedList<Connection> accessibleNodes){
        this.connectedNodes = connectedNodes;
        this.accessibleNodes = accessibleNodes;
    }
    @Override
    public void run(){
        while(true){
            try{
                accessibleNodes.clear();
                sleep(SLEEP_FOR_NEXT_CHECK);
                for(Connection c : connectedNodes)
                    c.send("#AWAKE_SIGNAL");
                sleep(SLEEP_FOR_NEXT_CHECK);
                for(Connection c : connectedNodes){
                    if(!search(accessibleNodes, c)){
                        c.getSocket().close();
                        c.stop();
                    }else
                        temporaryNodes.add(c);
                }
                connectedNodes.clear();
                for(Connection c : temporaryNodes)
                    connectedNodes.add(c);
                temporaryNodes.clear();
            }catch (IOException ex) {
                System.out.println("#ERRCODE: 7085");
            } catch (InterruptedException ex) {
                System.out.println("#ERRCODE: 7087");
            }
        }
    }
    public boolean search(LinkedList<Connection> connectionList, Connection c){
        if(c.getByteInString().equals("localhost") || c.getByteInString().equals("127001")) return true;
        for(Connection item : connectionList){
            if(checkEquality(item, c)) return true;
        }
        return false;
    }
    public boolean checkEquality(Connection a, Connection b){
        return a.getByte().equals(b.getByte());
    }
}

class Connection extends Thread{
    private final Socket socket;
    private final byte[] IP;
    private final DataOutputStream out;
    private final DataInputStream in;
    private final LinkedList<Connection> accessibleNodes;
    public Connection(Socket socket, LinkedList<Connection> accessibleNodes) throws IOException{
        this.accessibleNodes = accessibleNodes;
        this.socket = socket;
        IP = ((Inet4Address) ((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress()).getAddress();
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());
    }
    public String getByteInString(){
        String IPstr = "";
        for(byte b : IP)
            IPstr += (b & 0xFF);
        return IPstr;
    }
    public byte[] getByte(){
        return IP;
    }
    public Socket getSocket(){
        return socket;
    }
    public void send(String str) throws IOException, InterruptedException{
        byte[] str_in_byte = str.getBytes();
        int str_in_byte_len = str_in_byte.length;
        out.writeInt(str_in_byte_len);
        out.write(str_in_byte);
        Integer random = (int)(Math.random() * 100);
        sleep(100+random);
    }
    public boolean propagate(String block){
        // UPDATE HERE AFTER SYNCHR
        return true;
    }
    @Override
    public void run(){
        try{
            // CONNECTION MESSAGE
            System.out.println("Connected to " + getByteInString());
            // VARIABLES
            int length;
            byte[] bytes;
            String message;
            
            while(true){
                // HOW MANY BITE WE WILL RECEIVE
                length = in.readInt();
                bytes = new byte[length];
                // READ THAT LENGTH STRING
                in.readFully(bytes,0,bytes.length);
                message = new String(bytes, StandardCharsets.US_ASCII);
                
                // DECLARE YOU ARE NOT DISCONNECTED
                if(message.contains("#AWAKE_SIGNAL"))
                    send("#I_AM_AWAKE");
                
                if(message.contains("#I_AM_AWAKE"))
                    accessibleNodes.add(this);
                
                if(message.contains("#SYNCH_REQ")){
                    File blocks = new File("./BLOCKS.dat");
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(blocks));
                    String block = bufferedReader.readLine();
                    if(!block.contains("#BLOCK")){
                        send("#SYNCH_RESPONSE_END");
                        sleep(50);
                    }
                    else{
                        send(block);
                        sleep(50);
                    }
                }
                
                if(message.contains("#BLOCK_")){
                    int blockId = Integer.valueOf(message.split(" ")[0].split("_")[1]);
                    if(blockId > Mining.LAST_VALIDATED_BLOCK){
                        Mining.BLOCK_WRITER.write(message+"\n");
                        Mining.lastBlock = message;
                    }
                }
                
                if(message.contains("#BLOCK_LEN")){
                    int CLAIMED_BLOCK_LEN = Integer.valueOf(message.split(" ")[1]);
                    if( CLAIMED_BLOCK_LEN > Mining.CLAIMED_BLOCK ){
                        Mining.CLAIMED_BLOCK = CLAIMED_BLOCK_LEN;
                        Mining.BLOCK_PROVIDER = this;
                    }
                }
                
                // INFORM ABOUT MESSAGE
                System.out.println("A message arrived: "+message);
                // DISCONNECT REQUES
                if(message.compareTo("end")==0) break;
            }
            // INFORM ABOUT DISCONNECTION
            System.out.println("Client Disconnected!");
        }catch(IOException e){
            System.out.println("#ERRCODE: 7081");
        } catch (InterruptedException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class Client extends Thread{
    LinkedList<Connection> connectedNodes;
    LinkedList<Connection> accessibleNodes;
    final short PORT = 4088;
    final String IP = "localhost";
    
    public Client(LinkedList<Connection> connectedNodes, LinkedList<Connection> accessibleNodes ){
        this.connectedNodes = connectedNodes;
        this.accessibleNodes = accessibleNodes;
    }
    @Override
    public void run(){
        try {
            Socket clientSocket = new Socket(IP, (int)PORT);
            Connection connection = new Connection(clientSocket, accessibleNodes);
            connectedNodes.add(connection);
            connection.start();
        } catch (IOException ex) {
            System.out.println("#ERRCODE: 7075");
        }
    }
}

class Server extends Thread{
    final short PORT = 4088;
    final short MAX_CONNECTED_NODE = 6; //  MAXIMUM 6 CONNECTION ALLOWED
    final short SLEEP_TIME_FOR_NEW_CONNECTION = 1000;   // 1 SECOND
    LinkedList<Connection> connectedSockets = new LinkedList<>();
    LinkedList<Connection> accessibleNodes = new LinkedList<>();
    public Server(LinkedList<Connection> connectedSockets, LinkedList<Connection> accessibleNodes ){
        this.connectedSockets = connectedSockets;
        this.accessibleNodes = accessibleNodes;
    }
    @Override
    public void run(){
        // LISTEN PORT
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket((int)PORT);
        } catch (IOException ex) {
            System.out.println("#ERRCODE: 7076");
        }
        
        try {
            // ACCEPT CONNECTIONS
            while(true){
                if(connectedSockets.size() > MAX_CONNECTED_NODE){
                    sleep(SLEEP_TIME_FOR_NEW_CONNECTION);
                    continue;
                }
                Socket socket = serverSocket.accept();
                Connection connection = new Connection(socket, accessibleNodes);
                System.out.println(connection.getByteInString());
                if(!connection.getByteInString().equals("127001") || !connection.getByteInString().equals("localhost"))
                    connectedSockets.add(connection);
                connection.start();
            }
        } catch (IOException ex) {
            System.out.println("#ERRCODE: 7077");
        } catch (InterruptedException ex) {
            System.out.println("#ERRCODE: 7078");
        }
    }
}