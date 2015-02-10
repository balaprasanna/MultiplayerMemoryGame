/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bala.cdi;

import java.io.Serializable;
import java.util.HashMap;
import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import org.glassfish.jersey.media.sse.EventOutput;

@ApplicationScoped
public class GameGroups implements Serializable{
    private static final long version =1L;
private HashMap<String,GameParticipants> listOfGroups= new HashMap();

    public GameGroups() {
    }
  
     public void add(EventOutput eo, String room) {
        GameParticipants p = listOfGroups.get(room);
        if (null == p) {
           p = new GameParticipants();
           listOfGroups.put(room, p);           
        }
        p.add(eo);
    }
    
    public void publish(String msg, String game) {
        GameParticipants p = listOfGroups.get(game);
        if (null == p)
            return;
        p.publish(msg, game);
    }
    
    public void publishJsonMessage(JsonObject jsonmsg, String game) {
        GameParticipants p = listOfGroups.get(game);
        if (null == p)
            return;
        p.publishjson(jsonmsg, game);
    }
    
    public void addCV(EventOutput eo,String gameid)
    {
            GameParticipants p = listOfGroups.get(gameid);
        if (null == p) {
           p = new GameParticipants();
           listOfGroups.put(gameid, p);           
        }
        p.setCveo(eo);
        
        
    }
    
        public void publishCVmessage(JsonObject jsonmsg, String game) {
        GameParticipants p = listOfGroups.get(game);
        if (null == p)
            return;
        p.publishCVjson(jsonmsg, game);
      //  p.publish(msg, game);
    }

}
