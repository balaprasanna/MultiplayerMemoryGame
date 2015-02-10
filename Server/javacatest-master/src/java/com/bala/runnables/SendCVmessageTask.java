/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bala.runnables;

import com.bala.cdi.GameGroups;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;

/**
 *
 * @author A0123057N
 */
public class SendCVmessageTask implements Runnable{
    
    
      private JsonObject jsonObjectbuild;
    private EventOutput eo;
   private String name;
   @Inject GameGroups groups;
    
    public SendCVmessageTask(JsonObject jsonObject,String gameName) {
    jsonObjectbuild = jsonObject;
      name=gameName;
    }

      @Override
    public void run(){

          try{
               System.out.println("Successfully write...");
           groups.publishCVmessage(jsonObjectbuild, name);
            System.out.println("Successfully write finished.....");
          }catch(Exception e){ 
              System.err.println(" error>>>>"+e.getMessage());
          }
    }
    
}
