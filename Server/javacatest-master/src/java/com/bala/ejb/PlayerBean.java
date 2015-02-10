/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bala.ejb;

import com.bala.persistance.PersistantPlayer;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

@Stateless
public class PlayerBean {
@PersistenceContext EntityManager em;
public void createPlayer(PersistantPlayer p){
    em.persist(p);
    em.flush();
}
public void updatePlayer(PersistantPlayer p){
   
   PersistantPlayer p1 =  em.find(PersistantPlayer.class, p.getPlayerid());
   p1.setPlayername(p.getPlayername());
   p1.setNumberofTries(p.getNumberofTries());
   p1.setScore(p.getScore());
   em.flush();
}

public PersistantPlayer findPlayer(String uid)
{
    return em.createNamedQuery("findPlayer",PersistantPlayer.class).setParameter("playerid", uid)
            .getSingleResult();
}
public List<PersistantPlayer> topPlayers()
{
    List<PersistantPlayer> list=  em.createNamedQuery("TopPlayerList",PersistantPlayer.class).getResultList();
    return list;
}

}
