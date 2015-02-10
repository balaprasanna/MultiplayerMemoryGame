/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bala.util;

import com.bala.cdi.Game;
import com.bala.cdi.player;
import com.bala.persistance.PersistantPlayer;
import java.math.BigDecimal;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author A0123057N
 */
public class MyJsonBuilder {

    
    public MyJsonBuilder() {
    }
    
    public JsonObject BuildGameJsonObject(Game currentGame,List gameSeq,List gameWinSeq){
     
        
         JsonObjectBuilder jsonObject = Json.createObjectBuilder();
               jsonObject.add("Id",currentGame.getGameId());
               jsonObject.add("Name",currentGame.getGameName());
               jsonObject.add("HighScore",currentGame.getHishScore());
               jsonObject.add("PlayersCount",currentGame.getPlayerList().size());
               
               JsonArrayBuilder jsonArraybuilder = Json.createArrayBuilder();
                for(String key : currentGame.getPlayerList().keySet())
                {
                    System.out.println("My json builder");
        
                    System.out.println("key"+key);
                   player player = (player) currentGame.getPlayerList().get(key);
                   if(player==null){
                       System.out.println("null..player");
                   }
                    System.out.println("player id =="+player.getPlayerId());
                     System.out.println("player name =="+player.getPlayerName());
                      System.out.println("player score=="+player.getPlayerScore());
                    JsonObjectBuilder jsonObjectPlayer = Json.createObjectBuilder();
                    //needs a change.. player id to imee number
                    jsonObjectPlayer.add("playerid", player.getPlayerName());
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
              
                for(Object item :gameWinSeq)
                {
                    int val = Integer.parseInt(item.toString());
                    jsonArraybuilderForWinningSeq.add(val);
                }
                jsonObject.add("WinningSequence", jsonArraybuilderForWinningSeq);
        
    return jsonObject.build();
 }   
    
    public JsonObject BuildFailureJsonObject(String gameName,String pos1,String pos2,String result){
        
         JsonObjectBuilder jsonObject = Json.createObjectBuilder();
               jsonObject.add("Id","resultObject");
               jsonObject.add("Name",gameName);
               jsonObject.add("pos1",pos1);
               jsonObject.add("pos2",pos2);
               jsonObject.add("result",result);
        return jsonObject.build();
    }
    public JsonObject BuildPlayerObject(PersistantPlayer p){
        
         JsonObjectBuilder jsonObject = Json.createObjectBuilder();
               jsonObject.add("name",p.getPlayername());
               jsonObject.add("numberoftries",p.getNumberofTries());
               jsonObject.add("score",p.getScore());
        return jsonObject.build();
    
    }
    
    public JsonObject getRankListJson(List<PersistantPlayer> list)
    {
        JsonObjectBuilder PlayerListjsonObject=Json.createObjectBuilder();
        for(PersistantPlayer p : list)
        {
            if(p!=null)
            PlayerListjsonObject.add(p.getPlayername(), p.getScore());
        }
         return PlayerListjsonObject.build();
        
    }
     public JsonObject BuildSuccessObjectForCV(String card1,String card2){
        
         JsonObjectBuilder jsonObject = Json.createObjectBuilder();
               jsonObject.add("Id","SuccessObject");
               jsonObject.add("card1",card1);
               jsonObject.add("card2",card2);
        return jsonObject.build();
    
    }  
     public JsonObject BuildFailureObjectForCV(String card1,String card2){
        
         JsonObjectBuilder jsonObject = Json.createObjectBuilder();
               jsonObject.add("Id","FailureObject");
               jsonObject.add("card1",card1);
               jsonObject.add("card2",card2);
        return jsonObject.build();
    
    }
}
