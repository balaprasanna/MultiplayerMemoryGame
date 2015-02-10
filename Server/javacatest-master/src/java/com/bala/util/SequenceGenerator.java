/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bala.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author A0123057N
 */
// this
public class SequenceGenerator {
    private List gameSequence;

    public List getGameSequence() {
        return gameSequence;
    }


    public SequenceGenerator() {
        this.gameSequence = generateGameSequence();
    }
    
    private List generateGameSequence(){
    
    List list = new ArrayList();
    Set listof10Items = new HashSet();
    List listof20items = new ArrayList();
    for (int i=1;i<53;i++)
    {
     list.add(i);
    }
     // System.out.println(">>>>>>>>>>>>>>>>>>>>");  
    // get a random of 10 items...
    Collections.shuffle(list);
    int itemsize = 10;
     for(int i=0;i<itemsize;i++)
     {
            listof10Items.add(Integer.parseInt(list.get(i).toString()));   
    // System.out.println(i+">>>"+Integer.parseInt(list.get(i).toString()));
     }
 
      // populate the 10 items to 20 items...
     int size =listof10Items.size();
         System.out.println("size--"+size);
     
       for(Object item : listof10Items){
           listof20items.add(item);
           listof20items.add(item);
       }
        for (Object listof20item : listof20items) {
          //System.out.println(">>>"+"listof20item>>>"+Integer.parseInt(listof20item.toString()));  
       }
 
     // shuffle the 20 items..
        Collections.shuffle(listof20items);
        for (int j = 0; j < listof20items.size(); j++) {
            // System.out.println(">>Welcome>>>"+listof20items.get(j));
        }    
    return listof20items;
    }
    
}
