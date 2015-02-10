/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bala.rest;

import com.bala.cdi.Game;
import com.bala.cdi.GameCollection;
import com.bala.cdi.GameGroups;
import com.bala.cdi.player;
import com.bala.ejb.PlayerBean;
import com.bala.persistance.PersistantPlayer;
import com.bala.runnables.HandleNewGameRequest;
import com.bala.util.MyJsonBuilder;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.context.RequestScoped;
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

@Path("/gamePlay")
@RequestScoped
public class Gameplay {
    @Inject GameCollection allGames;
 @Inject GameGroups gameGroups;
 Game currentGame;
 @Inject player Currentplayer;
 @EJB PlayerBean mybean;
 
    @Resource(mappedName = "concurrent/MyManageScheduleExecutorService") ManagedScheduledExecutorService src;
   // @Resource(mappedName = "MyManagedScheduleExecutor") ManagedScheduledExecutorService src;
   
    
    @GET
    @Path("/{gamename}/{playerid}")
  //  @Produces("text/event-stream")
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public Response startgame(@PathParam("gamename") String name,@PathParam("playerid") String playerid){
        System.out.println(name+">> game name "+ playerid+">> player name");
        // get the game instance
        PersistantPlayer persistantPlayer = mybean.findPlayer(playerid);
        
        //fecth p[layer from db///
        
         EventOutput eo = new EventOutput();
         
         currentGame = allGames.getAGame(name);
        
        if(currentGame.hasPlayer(playerid))
        
        {
            Currentplayer = currentGame.getPlayerList().get(playerid);
        Currentplayer.setPlayerId(persistantPlayer.getPlayerid());
        Currentplayer.setPlayerName(persistantPlayer.getPlayername());
      //  Currentplayer.setPlayerScore(Currentplayer.getPlayerScore());
        Currentplayer.setNoOfTries(persistantPlayer.getNumberofTries());
        Currentplayer.setEo(eo);
        }
        else
        {
         Currentplayer.setPlayerId(persistantPlayer.getPlayerid());
        Currentplayer.setPlayerName(persistantPlayer.getPlayername());
       Currentplayer.setPlayerScore(0);
        Currentplayer.setNoOfTries(persistantPlayer.getNumberofTries());
        Currentplayer.setEo(eo);
        }
        // cdi objects needs to use createCopy
        currentGame.AddPlayer(Currentplayer.createCopy());
        
        // for testing...start
        for (int i = 0; i < currentGame.getPlayerList().size(); i++) {
            for (String key : currentGame.getPlayerList().keySet()) {
                player obj = currentGame.getPlayerList().get(key);
                System.out.println(">>inside game play....");
                System.out.println("for this key"+key +">>>> "+ obj.getPlayerId() +">>>> " + obj.getPlayerName()+">>>> "+ obj.getPlayerScore());
            }

        }
       // for testing ..end
        
        System.out.println("game play jax-rs");
        System.out.println(Currentplayer.getPlayerName() +">> player name "+ Currentplayer.getPlayerId()+">> player id");
        gameGroups.add(eo, name);
        
        // name change request fromgame sequence to card sequence
       List gameSeq = currentGame.getGameSequence();
       List WinningSeq = currentGame.getWinningSequence();  
         
        
         JsonObjectBuilder jsonObject = Json.createObjectBuilder();
               jsonObject.add("Id",currentGame.getGameId());
               jsonObject.add("Name",currentGame.getGameName());
               jsonObject.add("HighScore",currentGame.getHishScore());
               jsonObject.add("PlayersCount",currentGame.getPlayerList().size());
               
               JsonArrayBuilder jsonArraybuilder = Json.createArrayBuilder();
                for(String key : currentGame.getPlayerList().keySet())
                {
                   player player = currentGame.getPlayerList().get(key);
                    JsonObjectBuilder jsonObjectPlayer = Json.createObjectBuilder();
                    jsonObjectPlayer.add("playerid", player.getPlayerId());
                    jsonObjectPlayer.add("playerName", player.getPlayerName());
                    jsonObjectPlayer.add("playerScore", player.getPlayerScore());
                    //jsonObjectPlayer.build();
                    jsonArraybuilder.add(jsonObjectPlayer);
                  
                   // builder.
                }
                jsonObject.add("players", jsonArraybuilder);
               JsonArrayBuilder jsonArraybuilderForSeq = Json.createArrayBuilder();
              
                for(Object item :gameSeq)
                {
                    int val = Integer.parseInt(item.toString());
                    jsonArraybuilderForSeq.add(val);
                }
                jsonObject.add("gameSequence", jsonArraybuilderForSeq);
               
                JsonArrayBuilder jsonArraybuilderForWinningSeq = Json.createArrayBuilder();
              //for testing..
                //WinningSeq.add(gameSeq.get(0));
               // WinningSeq.add(gameSeq.get(1));
                //
                for(Object item :WinningSeq)
                {
                    int val = Integer.parseInt(item.toString());
                    jsonArraybuilderForWinningSeq.add(val);
                }
                
                jsonObject.add("WinningSequence", jsonArraybuilderForWinningSeq);
                // jsonObject.add(, builder)
               // builder.add(jsonObject);
                
               //System.out.println( allGames.getAGame(s).getGameName());
             //jsonObject.build()
        //JsonObject jsonoutput = jsonObject.build();
                     //  System.out.println("json object to string"+jsonObject.build());

       HandleNewGameRequest x = new HandleNewGameRequest(eo, jsonObject.build(),name);
        
       src.submit(x);
        //x.setFuture(future);
        // ExecutorService pool = Executors.newFixedThreadPool(1);
        // pool.execute(x);
        //x.setFuture(future);
        
        
      //  gameGroups.publishJsonMessage(jsonoutput, name);
//                OutboundEvent event = new OutboundEvent.Builder()
//                 .mediaType(MediaType.APPLICATION_JSON_TYPE)
//                 //.name(name)
//                 .data(Json.class, jsonObject.build())
//                 .build();
//                try{
//                eo.write(event);
//                }catch(Exception e){
//                    e.printStackTrace();
//                }                
        return Response.ok(eo).build();
       
    }
            
     @GET
     @Path("/play")
    @Produces(MediaType.APPLICATION_JSON)
    public Response playgame(){
       
        return Response.ok().build();
    }
    
      @GET
     @Path("/leave")
    @Produces(MediaType.APPLICATION_JSON)
    public Response leavegame(){

        return Response.ok().build();
    }
    
    @GET
    @Path("/checkoldplayer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkPlayer(@QueryParam("uid") String uid){
    PersistantPlayer p = mybean.findPlayer(uid);
    if(p!=null){
       MyJsonBuilder builder = new MyJsonBuilder();
       JsonObject result = builder.BuildPlayerObject(p);
       return Response.ok(result).build();
    }
    else
    {
        Response.ResponseBuilder obj = Response.status(Response.Status.BAD_REQUEST);                   
        return obj.build();
    }
    //return Response.ok().build();
    }
    
    @GET
    @Path("/register")
    public Response registerNewPlayer(@QueryParam("uid") String uid,@QueryParam("name")String name){
    PersistantPlayer p = mybean.findPlayer(uid);
    if(p==null){
         p = new PersistantPlayer(uid, name, 0, 0);
         mybean.createPlayer(p);
         System.out.println(">> new player created in db with name"+p.getPlayername()+">> id"+p.getPlayerid());
 
    }
    else{
         Response.ResponseBuilder obj = Response.status(Response.Status.BAD_REQUEST);
         System.out.println(">>>> player not created. loop..");
        return obj.build(); 
    }
    
    return Response.ok().build();
    }    
}

