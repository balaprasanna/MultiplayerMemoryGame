/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bala.cdi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseBroadcaster;

@ApplicationScoped
public class GameParticipants {
   @Inject GameCollection games;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private SseBroadcaster broadcaster = new SseBroadcaster();
   private EventOutput Cveo;

    public EventOutput getCveo() {
        return Cveo;
    }

    public void setCveo(EventOutput Cveo) {
        this.Cveo = Cveo;
    }
    
    public void add(EventOutput eo) {
        Lock l = lock.writeLock();
        l.lock();
        try {
            broadcaster.add(eo);
        } finally {
            l.unlock();
        }
    }
    
    public void publish(String name, String game) {
        OutboundEvent event = new OutboundEvent.Builder()
                .data(String.class, name)
                //.name(game)
                .build();
        Lock l = lock.readLock();
        l.lock();
        try {
            broadcaster.broadcast(event);
        } finally {
            l.unlock();
        }
    }
    
     public void publishjson(JsonObject name, String game) {
        OutboundEvent event = new OutboundEvent.Builder()
                .data(JsonObject.class, name)
                .name(game)
                .build();
        Lock l = lock.readLock();
        l.lock();
        try {
            broadcaster.broadcast(event);
        } finally {
            l.unlock();
        }
    }
     
     
//     // method for completeing the handshake purpose
//     public void addCV(EventOutput eo) 
//     {
//    
//            Cveo=eo;
//            System.out.println("Insode the add EO for CV ");
//      }
     
     
     public void publishCVjson(JsonObject name,String game)
     {
         
         
         System.out.println("CV Eo object"+ getCveo());
           OutboundEvent event = new OutboundEvent.Builder()
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(JsonObject.class, name)
                .name(game)
                .build();
//         Lock l = lock.readLock();
//         l.lock();
        try {
          // getCveo().write(event);
            System.out.println(">>>");
         //   Game mygame =  games.getListOfGames().get(game);
          //  System.out.println(">> game object in publishcvjason"+mygame);
  //  game.setGameCVEO(eo);
           //mygame.getGameCVEO().write(event);
           getCveo().write(event);
        // games.getListOfGames().get(game).getGameCVEO().write(event);
            System.out.println(">>>> message sent to client");
        } catch (IOException ex) {
                 System.out.println("Problems in while sending the comman View cv");
            Logger.getLogger(GameParticipants.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
    //  System.out.println(">>>> message sent to client for "+Cveo.toString());
        }
         
     }
     
     
}

