
package glinda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {
    public static long getTime() throws MalformedURLException, IOException {
        // // // // // // // // // // // // // // // 
        // NEED TO MAKE THIS DECENTRALIZED ALSO // //
        //// // // // // // // // // // // // // // // 
        
        String[] timeserverList = new String[]{"129.6.15.28","129.6.15.29","129.6.15.30","129.6.15.27","129.6.15.26"};
        Socket socket;
        BufferedReader bf;
        String line;
        String timestamp = null;
        Date datetimeInMilis = null;
        
        for(String ip : timeserverList){
            timestamp = "";
            socket = new Socket(ip,13);
            bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while((line = bf.readLine()) != null) timestamp+=line;
            timestamp = timestamp.trim();
            if(!timestamp.contains("denied") && (timestamp.equals("") || timestamp.equals(" ")))
                break;
        } 
        
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        try{
            datetimeInMilis = sdf.parse(timestamp.split(" ")[1]+" "+timestamp.split(" ")[2]);
        }catch(ParseException e){
            System.out.println("#ERR IN TIME CLASS");
        }
        
        return datetimeInMilis.getTime();
    }
}
