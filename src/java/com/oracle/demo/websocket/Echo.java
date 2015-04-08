/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oracle.demo.websocket;

/**
 *
 * @author Marcelo
 */
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
/**
 *
 * @author marcelo
 */
@ServerEndpoint(value = "/echo")
public class Echo{

    @OnOpen
    public void hi(Session remote) 
    {
      System.out.println(remote.getId() + " connected...");
    }
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Some [" + session.getId() + "] sent me this message: " + message);
        Set<Session> everyone = session.getOpenSessions();
        System.out.println("We have " + everyone.size() + " session(s) connected");
        
        try {
            Session[] sessions = new Session[everyone.size()];
            sessions = everyone.toArray(sessions);
            for (int i=0; i<sessions.length; i++){
                synchronized (sessions[i].getBasicRemote()){
                    sessions[i].getBasicRemote().sendText(message);
                }      
            }
        } catch (Exception ex) {
            Logger.getLogger(Echo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @OnClose
    public void bye(Session remote, CloseReason closeReason) 
    {
      System.out.println(remote.getId() + " is disconnecting... " + (closeReason != null ? "(Close code:" + closeReason.getCloseCode() + ", reason:" + closeReason.getReasonPhrase() + ")" : "No reason"));
      System.out.println("We have " + remote.getOpenSessions().size() + " session(s) left connected");
    } 
}