/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bala.cdi;


import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import org.glassfish.jersey.media.sse.EventOutput;

/**
 *
 * @author Prasanna Mac
 */@SessionScoped
public class player implements Serializable{
    private static final long version = 1L;
    private static final int MAXTRIES =10;
    
    private String playerId;
    private String playerName;
    private int playerScore;
    private int NoOfTries;

   // @EJB PlayerBean playerBean;
    
    public int getNoOfTries() {
        return NoOfTries;
    }

    public boolean setNoOfTries(int NoOfTries) {
        if(NoOfTries <= MAXTRIES){
        this.NoOfTries = NoOfTries;
        return true;}
        return false;
    }
    private EventOutput eo;

    public EventOutput getEo() {
        return eo;
    }

    public void setEo(EventOutput eo) {
        this.eo = eo;
    }

    public String getPlayerId() {
          System.out.println("get playerId>>"+playerId);
        return playerId;
    }

    public void setPlayerId(String playerId) {
          System.out.println("set playerId>>"+playerId);
        this.playerId = playerId;
    }

    public String getPlayerName() {
        System.out.println("getPlayerName>>"+playerName);
        return playerName;
    }

    public void setPlayerName(String playerName) {
        System.out.println("setPlayerName>>"+playerName);
        this.playerName = playerName;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }
   
    public player createCopy(){
       player p = new player();
       p.setEo(this.eo);
       p.setNoOfTries(this.NoOfTries);
       p.setPlayerId(this.playerId);
       p.setPlayerName(this.playerName);
       p.setPlayerScore(this.playerScore);
       return p;
    }
    
}
