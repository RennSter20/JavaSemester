package com.example.renatojava.javasemester.util;

import com.example.renatojava.javasemester.entity.ConsultingRoom;
import com.example.renatojava.javasemester.entity.Sickroom;

public class RoomChecker<T> {

    public T room;

    public RoomChecker(T room) {
        this.room = room;
    }

    public String roomType(){
        if(room instanceof ConsultingRoom){
            return "Consulting room";
        }else if(room instanceof Sickroom){
            return "Sickroom";
        }else{
            return "Casualty";
        }
    }
}
