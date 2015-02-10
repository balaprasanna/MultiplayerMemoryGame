/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bala.rest;

import com.bala.cdi.Game;
import com.bala.cdi.GameCollection;
import com.bala.cdi.GameGroups;
import com.bala.ejb.PlayerBean;
import com.bala.persistance.PersistantPlayer;
import com.bala.util.MyJsonBuilder;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.SseFeature;

/**
 *
 * @author A
 * 
 **/
@Path("/CommonView")
@RequestScoped
public class CommonViewResource {
    
    
    
    @EJB PlayerBean playerBean;
    @Inject GameGroups gameGroups;
    @Inject GameCollection games;
    MyJsonBuilder myJsonBuilder=new MyJsonBuilder();
    
    @GET
    @Path("/getRanklist")
    @Produces(MediaType.APPLICATION_JSON)
   public Response gettopPlayer()
   {
      List<PersistantPlayer> list= playerBean.topPlayers();
      JsonObject jsonbject=myJsonBuilder.getRankListJson(list);
        return Response.ok(jsonbject).build();
    }
   
   
     @GET
    @Path("/{game}")
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public Response getCVLink(@PathParam("game") String gameid) {
        EventOutput eo = new EventOutput();
        System.out.println(">>> new Comman Viwe");
        //gameGroups.addCommanview(eo, id);
       gameGroups.addCV(eo, gameid);
        System.out.println(">>>>");
         System.out.println(">>> game id"+gameid);
    Game game =  games.getListOfGames().get(gameid);
game.setGameCVEO(eo);
        System.out.println(">>>> null"+game.getGameCVEO());
       return (Response.ok(eo).build());
    }
    
    
    
    @GET
    @Path("/gamesequnce/{game}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGameSeq (@PathParam ("game") String gameid)
    {
      Game a=games.getAGame(gameid);
     JsonObject object=myJsonBuilder.BuildGameJsonObject(a,a.getGameSequence(), a.getWinningSequence());
    return Response.ok(object).build();
    }
   
   
   
   
   
   
   
   
}
