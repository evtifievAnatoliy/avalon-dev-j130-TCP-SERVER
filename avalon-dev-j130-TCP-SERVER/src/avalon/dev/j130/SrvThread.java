/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package avalon.dev.j130;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SrvThread extends Thread{
    
    MainForm mainForm;
    
    ArrayList<ClientThread> clientThreads = new ArrayList<ClientThread>();
    
    String log;

    public SrvThread(MainForm mainForm) {
        this.mainForm = mainForm;
    }
    
    
    @Override
    public void run() {
     try (ServerSocket srvSock = new ServerSocket(7_020)){
            
            mainForm.setLogs("Сервер LocalHost:" + srvSock.getLocalPort() + " запущен.");
            
            while (true){
                Socket cliSocket = srvSock.accept();
                ClientThread clientThread = new ClientThread(cliSocket, mainForm, this);
                clientThread.start();
                clientThreads.add(clientThread);
            }
        } catch (IOException ex) {
            mainForm.setLogs(ex.toString());
        }
    
    }

    public ArrayList<ClientThread> getClientThreads() {
        return clientThreads;
    }

    public String getLog() {
        return log;
    }
    
    public void sendMessageToAll(String line, String time) throws IOException{
        
        for (ClientThread clientThread : clientThreads){
                    //try (ObjectOutputStream oos1 = new ObjectOutputStream(clientThread.getClientSocket().getOutputStream())){
                        clientThread.getOos().writeObject(new Object[]{line, time});
                    
                    //}
                }
    }

    
    
    
    
}
