/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package avalon.dev.j130;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import javafx.util.converter.LocalDateTimeStringConverter;


public class ClientThread extends Thread{
    private Socket clientSocket;
    ObjectOutputStream oos;
    private String clientHostPost;
    private MainForm mainForm;
    ArrayList<ClientThread> clientThreads;
    SrvThread srvThread;
    
        
    public ClientThread(Socket clientSocket, MainForm mainForm, SrvThread srvThread, ArrayList<ClientThread> clientThreads){
        super();
        this.clientSocket = clientSocket;
        this.mainForm = mainForm;
        this.srvThread = srvThread;
        this.clientThreads = clientThreads;
    }

    @Override
    public void run() {
            
        clientHostPost = String.format("%s,%s", clientSocket.getInetAddress(), clientSocket.getPort());
        mainForm.setLogs(clientHostPost + " connected.");
        
        try(ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())){
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            while(true){
                
                
                String line = (String) ois.readObject();
                LocalDateTime d = LocalDateTime.now();
                String time = "( " + d.getHour() + ":" + d.getMinute() + ":" + d.getSecond() + " )";
                mainForm.setLogs(clientHostPost + ", " +  line + ", " + time);
                srvThread.sendMessageToAll(line, time);
                                
            }
        } catch (Exception ex) {
            mainForm.setLogs(clientHostPost + " disconnected.");
            clientThreads.remove(this);
        }
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    
   
}
