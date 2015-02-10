/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bala.rest;


import com.bala.cdi.Game;
import com.bala.cdi.GameCollection;
import com.bala.cdi.GameGroups;
import com.bala.cdi.GameParticipants;
import com.bala.runnables.MyTask;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;

@Path("/gameService")
@RequestScoped
public class gameResource implements Serializable{
    private static final Long version =1L;
    @Inject GameCollection allGames;
    @Inject GameParticipants GRP;
    @Inject GameGroups gameGroups; 
    Game game;
    @Resource(mappedName = "concurrent/MyManageScheduleExecutorService") ManagedScheduledExecutorService src;
   //@Resource(mappedName = "MyManagedScheduleExecutor") ManagedScheduledExecutorService src;
   
    @GET
    @Path("/game")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@QueryParam("gameName") String gamename){
        
        if(!allGames.hasGame(gamename))
        {
            game = new Game();
            game.setGameId("id"+gamename);
            game.setGameName(gamename);
            game.setHishScore(0);
        // add the newly created name into game list..
        allGames.addGame(gamename, game);
        GRP.publish(gamename, gamename);
        }
        else{
            game = allGames.getAGame(gamename);
        }
        
        JsonArrayBuilder builder = Json.createArrayBuilder();
        
        if(allGames.getListOfGames().keySet().size()>0){
            for(String s : allGames.getListOfGames().keySet())
            {
               JsonObjectBuilder jsonObject = Json.createObjectBuilder();
               jsonObject.add("Id",allGames.getAGame(s).getGameId());
               jsonObject.add("Name",allGames.getAGame(s).getGameName());
               jsonObject.add("HighScore",allGames.getAGame(s).getHishScore());
               jsonObject.add("PlayersCount",allGames.getAGame(s).getPlayerList().size());
               System.out.println( allGames.getAGame(s).getGameName());
               builder.add(jsonObject);
            }
        }
        return Response.ok(builder.build()).build();
    }
    
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllGameInstance(){
        
        JsonArrayBuilder builder = Json.createArrayBuilder();
        
        if(allGames.getListOfGames().keySet().size()>0){
            for(String s : allGames.getListOfGames().keySet())
            {
               JsonObjectBuilder jsonObject = Json.createObjectBuilder();
               jsonObject.add("Id",allGames.getAGame(s).getGameId());
               jsonObject.add("Name",allGames.getAGame(s).getGameName());
               jsonObject.add("HighScore",allGames.getAGame(s).getHishScore());
               jsonObject.add("PlayersCount",allGames.getAGame(s).getPlayerList().size());
               System.out.println( allGames.getAGame(s).getGameName());
               builder.add(jsonObject);
            }
        }

        return Response.ok(builder.build()).build();
    }
    
     // for jquery mobile client
    @GET
    @Path("/getGames")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllGameInstanccdsce(){
        
        JsonArrayBuilder builder = Json.createArrayBuilder();
        
        if(allGames.getListOfGames().keySet().size()>0){
            for(String s : allGames.getListOfGames().keySet())
            {
               JsonObjectBuilder jsonObject = Json.createObjectBuilder();
               jsonObject.add("Id",allGames.getAGame(s).getGameId());
               jsonObject.add("Name",allGames.getAGame(s).getGameName());
               jsonObject.add("HighScore",allGames.getAGame(s).getHishScore());
               jsonObject.add("PlayersCount",allGames.getAGame(s).getPlayerList().size());
               //System.out.println( allGames.getAGame(s).getGameName());
               builder.add(jsonObject);
            }
        }

        return Response.ok(builder.build()).build();
    }
    
    // for jquery mobile client
    @GET
    @Path("/newgame")
    @Produces(MediaType.APPLICATION_JSON)
    public Response gegfvfgt(@QueryParam("gameName") String gamename){
         
        if(!allGames.hasGame(gamename))
        {
            game = new Game();
            game.setGameId("id"+gamename);
            game.setGameName(gamename);
            game.setHishScore(0);
           
            
        // add the newly created name into game list..
        allGames.addGame(gamename, game);
        GRP.publish(gamename, gamename);
        }
        else{
            game = allGames.getAGame(gamename);
             //game = null;
        }
        // for testing purpose only starts here
        List seq = game.getGameSequence();
            for (int i = 0; i < seq.size(); i++) {
                System.out.println("seq>>"+seq.get(i));  
            }
          // for testing purpose only ends here.
            
          JsonObjectBuilder jsonObject = Json.createObjectBuilder();
               jsonObject.add("Id",game.getGameId());
               jsonObject.add("Name",game.getGameName());
               jsonObject.add("HighScore",game.getHishScore());
               jsonObject.add("PlayersCount",game.getPlayerList().size());
               //System.out.println( game.getGameName());
           return Response.ok(jsonObject.build()).build();    
 }
    
    
    @GET
    @Path("/{game}")
    @Produces("text/event-stream")
    public Response getALink(@PathParam("game") String gamename)
    {
         EventOutput outputLink = new EventOutput();
         MyTask c = new MyTask(outputLink,gamename);
         ScheduledFuture<?> future = src.scheduleAtFixedRate(c, 1, 1, TimeUnit.SECONDS);
         
         c.setFuture(future);
         return Response.ok(outputLink).build();
    }
    
    // 

    
    
    @GET
    @Path("/dynamicRequest")
    @Produces("text/event-stream")
    public Response requestForAllGames(){    
//        JsonArrayBuilder builder = Json.createArrayBuilder();
//        if(allGames.getListOfGames().keySet().size()>0){
//            for(String s : allGames.getListOfGames().keySet())
//            {
//               JsonObjectBuilder jsonObject = Json.createObjectBuilder();
//               jsonObject.add("Id",allGames.getAGame(s).getGameId());
//               jsonObject.add("Name",allGames.getAGame(s).getGameName());
//               jsonObject.add("HighScore",allGames.getAGame(s).getHishScore());
//               jsonObject.add("PlayersCount",allGames.getAGame(s).getPlayerList().size());
//               System.out.println( allGames.getAGame(s).getGameName());
//               builder.add(jsonObject);
//            }
//        }
         EventOutput outputLink = new EventOutput();
         GRP.add(outputLink);
         //RCForAllGameslist c = new RCForAllGameslist(outputLink);
         //ScheduledFuture<?> future = src.scheduleAtFixedRate(c, 1, 1, TimeUnit.SECONDS);
         //c.setFuture(future);
         return Response.ok(outputLink).build();
    } 
    
  
}
