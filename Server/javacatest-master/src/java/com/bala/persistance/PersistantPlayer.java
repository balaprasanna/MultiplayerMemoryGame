/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bala.persistance;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author A0123057N
 */
@Entity
@NamedQueries({
@NamedQuery(name = "findPlayer",query = "select p from PersistantPlayer p where p.playerid like :playerid"),
@NamedQuery(name= "TopPlayerList",query="select p from PersistantPlayer p order by p.Score desc")})
public class PersistantPlayer implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String playerid;
    private String playername;
 
    private int numberofTries;
    private int Score;

    public int getScore() {
        return Score;
    }

    public void setScore(int Score) {
        this.Score = Score;
    }

    public PersistantPlayer() {
    }

    public PersistantPlayer(String playerid, String playername, int numberofTries, int Score) {
        this.playerid = playerid;
        this.playername = playername;
        this.numberofTries = numberofTries;
        this.Score = Score;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.playerid);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PersistantPlayer other = (PersistantPlayer) obj;
        if (!Objects.equals(this.playerid, other.playerid)) {
            return false;
        }
        return true;
    }

    
    
    public String getPlayerid() {
        return playerid;
    }

    public void setPlayerid(String playerid) {
        this.playerid = playerid;
    }

    public String getPlayername() {
        return playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }

    public int getNumberofTries() {
        return numberofTries;
    }

    public void setNumberofTries(int numberofTries) {
        this.numberofTries = numberofTries;
    }

   
    
    
}
