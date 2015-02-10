///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.bala.persistance;
//
//import java.io.Serializable;
//import javax.persistence.Basic;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.NamedQueries;
//import javax.persistence.NamedQuery;
//import javax.persistence.Table;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Size;
//import javax.xml.bind.annotation.XmlRootElement;
//
///**
// *
// * @author A0123057N
// */
//@Entity
//@Table(name = "gametable")
//@XmlRootElement
//@NamedQueries({
//    @NamedQuery(name = "Gametable.findAll", query = "SELECT g FROM Gametable g"),
//    @NamedQuery(name = "Gametable.findByGameid", query = "SELECT g FROM Gametable g WHERE g.gameid = :gameid"),
//    @NamedQuery(name = "Gametable.findByGamename", query = "SELECT g FROM Gametable g WHERE g.gamename = :gamename"),
//    @NamedQuery(name = "Gametable.findByGameHighScore", query = "SELECT g FROM Gametable g WHERE g.gameHighScore = :gameHighScore")})
//public class Gametable implements Serializable {
//    private static final long serialVersionUID = 1L;
//    @Id
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "gameid")
//    private Integer gameid;
//    @Size(max = 45)
//    @Column(name = "gamename")
//    private String gamename;
//    @Size(max = 45)
//    @Column(name = "gameHighScore")
//    private String gameHighScore;
//    @JoinColumn(name = "gameplayer", referencedColumnName = "playerid")
//    @ManyToOne
//    private Player gameplayer;
//
//    public Gametable() {
//    }
//
//    public Gametable(Integer gameid) {
//        this.gameid = gameid;
//    }
//
//    public Integer getGameid() {
//        return gameid;
//    }
//
//    public void setGameid(Integer gameid) {
//        this.gameid = gameid;
//    }
//
//    public String getGamename() {
//        return gamename;
//    }
//
//    public void setGamename(String gamename) {
//        this.gamename = gamename;
//    }
//
//    public String getGameHighScore() {
//        return gameHighScore;
//    }
//
//    public void setGameHighScore(String gameHighScore) {
//        this.gameHighScore = gameHighScore;
//    }
//
//    public Player getGameplayer() {
//        return gameplayer;
//    }
//
//    public void setGameplayer(Player gameplayer) {
//        this.gameplayer = gameplayer;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (gameid != null ? gameid.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof Gametable)) {
//            return false;
//        }
//        Gametable other = (Gametable) object;
//        if ((this.gameid == null && other.gameid != null) || (this.gameid != null && !this.gameid.equals(other.gameid))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "com.bala.persistance.Gametable[ gameid=" + gameid + " ]";
//    }
//    
//}
