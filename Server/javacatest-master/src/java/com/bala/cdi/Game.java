/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bala.cdi;

import com.bala.util.SequenceGenerator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.SessionScoped;
import javax.json.JsonObject;
import org.glassfish.jersey.media.sse.EventOutput;

@SessionScoped
public class Game implements Serializable{
private static final Long version = 1L;

private EventOutput gameCVEO;

  
private String GameId;
private String GameName;
private int HighScore =0;
private List gameSequence;
private List winningSequence;

    public List getWinningSequence() {
        return winningSequence;
    }

    public void setWinningSequence(List winningSequence) {
        this.winningSequence = winningSequence;
    }

    public List getGameSequence() {
        return gameSequence;
    }

    public Game() {
        this.gameSequence = generateGameSequence();
        this.winningSequence = new ArrayList();
    }

private HashMap<String,player> playerList = new HashMap<String,player>();

    public HashMap<String, player> getPlayerList() {
        return playerList;
    }
    
    public void AddPlayer(player p){
       if(!hasPlayer(p.getPlayerId())){
           // check here....
           System.out.println("game object class");
        System.out.println(p.getPlayerName() +">> player name "+ p.getPlayerId()+">> player name");
        playerList.put(p.getPlayerId(), p);
       }
    }
    
    public void RemovePlayer(player p){
        if(hasPlayer(p.getPlayerId())){
            playerList.remove(p);
        }
    }

    public boolean hasPlayer(String playername){
       if(playerList.containsKey(playername)){
           return true;
       }
       return false;
    }
    public String getGameId() {
        return GameId;
    }

    public void setGameId(String GameId) {
        this.GameId = GameId;
    }

    public String getGameName() {
        return GameName;
    }

    public void setGameName(String GameName) {
        this.GameName = GameName;
    }

    public int getHishScore() {
        return HighScore;
    }

    public void setHishScore(int HishScore) {
        this.HighScore = HishScore;
    }
    
    private List generateGameSequence()
    {
    SequenceGenerator sq = new SequenceGenerator();
    return sq.getGameSequence();
    }
    
     public void updateHishScore() {
         // initially get the current score
       int score = this.HighScore;
       // iterate to find the score of all players
         for(String key : playerList.keySet()){
             //if the score is greater than high score, then update the high score with a the high score
           if(score < playerList.get(key).getPlayerScore())
           {
           // replace the high score with the highest score of any player
               score = playerList.get(key).getPlayerScore();
           }
          }
         this.HighScore = score;
    }

    @Override
    public String toString() {
        return "Game{" + "GameId=" + GameId + ", GameName=" + GameName + ", HighScore=" + HighScore + ", gameSequence=" + gameSequence + ", winningSequence=" + winningSequence + ", playerList=" + playerList + '}';
    }
      public EventOutput getGameCVEO() {
        return gameCVEO;
    }

    public void setGameCVEO(EventOutput gameCVEO) {
        this.gameCVEO = gameCVEO;
    }
}
