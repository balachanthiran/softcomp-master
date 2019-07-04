/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.common.events;

import java.io.Serializable;

/**
 *
 * @author Mads
 */
public class Event implements Serializable{
    private EventType type;
    
    public Event(EventType type){
        this.type = type;
    }
    
    public void setType(EventType type){
        this.type = type;
    }
    
    public EventType getType(){
        return type;
    }
}
