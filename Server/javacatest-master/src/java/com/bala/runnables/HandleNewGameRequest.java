/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bala.runnables;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;

public class HandleNewGameRequest implements Runnable{

    private JsonObject jsonObjectbuild;
    private EventOutput eo;
   private String name;
    
    
    public HandleNewGameRequest(EventOutput eo1,JsonObject jsonObject,String name1) {
    jsonObjectbuild = jsonObject;
    eo = eo1;
    name=name1;
    }

    public void run(){

          OutboundEvent event = new OutboundEvent.Builder()
                 .mediaType(MediaType.APPLICATION_JSON_TYPE)
                  .name(name)
                 .data(jsonObjectbuild)
                 .build();

          try{
               System.out.println("Successfully write...");
           eo.write(event);
            System.out.println("Successfully write finished.....");
          }catch(Exception e){ 
              System.err.println("bala error>>>>"+e.getMessage());
          }
    }
}
