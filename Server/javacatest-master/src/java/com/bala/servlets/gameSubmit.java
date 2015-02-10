/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bala.servlets;

import com.bala.cdi.Game;
import com.bala.cdi.GameCollection;
import com.bala.cdi.GameGroups;
import com.bala.cdi.player;
import com.bala.ejb.PlayerBean;
import com.bala.persistance.PersistantPlayer;
import com.bala.util.MyJsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author A0123057N
 */
@WebServlet("/gamesubmit")
public class gameSubmit extends HttpServlet{
  @Inject GameCollection allGames;
 @Inject GameGroups gameGroups;
 Game myGame;
 @Inject player Currentplayer;
  @EJB PlayerBean mybean;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
       String playerid = req.getParameter("uid").toString();
       String gameName = req.getParameter("gameName").toString();
       String pos1 = req.getParameter("pos1").toString();
       String pos2 = req.getParameter("pos2").toString();
       String card1 = req.getParameter("card1").toString();
       String card2 = req.getParameter("card2").toString();
        System.out.println(">>ccard1"+card1+">>crad2"+card2);
        System.out.println("uid"+playerid+"name"+gameName);
       int val1 = Integer.parseInt(pos1);
       int val2 = Integer.parseInt(pos2);
       
       //check weather the two cards are same or not
       String result = (val1==val2)?"ok":"notok";
       System.out.println("Game name"+gameName);
       // get the game object
       myGame = allGames.getAGame(gameName);
       
        System.out.println("Game Properties >>>"+myGame.toString());
       // for testing...start
        for (int i = 0; i < myGame.getPlayerList().size(); i++) {
            for (String key : myGame.getPlayerList().keySet()) {
                player obj = myGame.getPlayerList().get(key);
                System.out.println("for this key"+key +">>>> "+ obj.getPlayerId() +">>>> " + obj.getPlayerName()+">>>> "+ obj.getPlayerScore());
            }

        }
       // for testing ..end
       
        //fecth the player form db to update score and no of tries
        PersistantPlayer persistantPlayer =  mybean.findPlayer(playerid);
        System.out.println(">>>> first request player info >> bala "+ persistantPlayer.getPlayerid() +">>>>"+ persistantPlayer.getPlayername());
       // get the current player..
       Currentplayer = myGame.getPlayerList().get(playerid);
       //test start
        System.out.println(">>>bala >>> currentplayer"+Currentplayer);
        if(persistantPlayer.getNumberofTries()<=10){
            // decise what to do when maximum 
        }
        Currentplayer.setNoOfTries(Currentplayer.getNoOfTries()+1);
       persistantPlayer.setNumberofTries(persistantPlayer.getNumberofTries()+1);
        
        System.out.println("for this current player object"+Currentplayer.getPlayerId() +">>>> "+ Currentplayer.getPlayerId() +">>>> " + Currentplayer.getPlayerName()+">>>> "+ Currentplayer.getPlayerScore());
       //test end
       if(result=="ok")
       {  // means two cards are same...

            //set player score
            Currentplayer.setPlayerScore(Currentplayer.getPlayerScore()+1);
            persistantPlayer.setScore(persistantPlayer.getScore()+1);
            // set high score
            myGame.updateHishScore();

             // update gameWinnning sequence
             List newWinningSeq = myGame.getWinningSequence();
             newWinningSeq.add(val1);
             //newWinningSeq.add(val2);
             myGame.setWinningSequence(newWinningSeq);
             // generate card info..
             // this call help creating a json object which will be sent back to the clients at later time as messages...
             MyJsonBuilder myBuilder = new MyJsonBuilder();
             JsonObject createdJsonObject = myBuilder.BuildGameJsonObject(myGame, myGame.getGameSequence(), myGame.getWinningSequence());
             
             // send broadcast messages to all clients..
             gameGroups.publishJsonMessage(createdJsonObject, gameName);
              /* So all the clients who are all playing on the same game will receive this message*/
             
             // create a success object for common view
            JsonObject SuccessJsonObject =  myBuilder.BuildSuccessObjectForCV(card1, card2);
             // sending the success object to common view...
             gameGroups.publishCVmessage(SuccessJsonObject, gameName);
            
              resp.setStatus(resp.SC_OK);
              
       }
       else
       {
       // means two cards are not same / which is different..
         MyJsonBuilder myBuilder = new MyJsonBuilder();
         JsonObject createdFailureJsonObject = myBuilder.BuildFailureJsonObject(gameName,pos1,pos2,result);
         /*This json object will be returned only to the respective client wheneven two cards doesn't matched*/
          try(PrintWriter writer = resp.getWriter())
          {
              resp.setStatus(resp.SC_OK);
              resp.setContentType("application/json");
              writer.println(createdFailureJsonObject);
              
          }
            // create a failure object for common view
            JsonObject FailureJsonObject =  myBuilder.BuildFailureObjectForCV(card1, card2);
            
             // sending the success object to common view...
             gameGroups.publishCVmessage(FailureJsonObject, gameName);
            
        }
      mybean.updatePlayer(persistantPlayer);
       
    }

}
