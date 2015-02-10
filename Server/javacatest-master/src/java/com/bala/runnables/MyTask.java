/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bala.runnables;

import java.util.concurrent.ScheduledFuture;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;

public class MyTask implements Runnable{

    private String gameName;
    private EventOutput eo;
    private ScheduledFuture<?> future;

    public ScheduledFuture<?> getFuture() {
        return future;
    }

    public void setFuture(ScheduledFuture<?> future) {
        this.future = future;
    }
    
    public MyTask(EventOutput eo1,String name) {
    gameName = name;
    eo = eo1;
    //src = src1;
    }

    public void run(){
       //if(eo.isClosed())
          OutboundEvent event = new OutboundEvent.Builder()
                 .data(String.class, "welcome to"+ gameName+ System.currentTimeMillis())
                 .build();
          if(eo.isClosed()){
             future.cancel(true);
              System.out.println("eo.shutdown() true>>>>"+eo.isClosed());
           }
          try{
           eo.write(event);
          }catch(Exception e){
            //future.cancel(true);
          }
        System.out.println(gameName+""+System.currentTimeMillis());
        System.out.println("eo.isClosed()"+eo.isClosed());
        
    }
}
