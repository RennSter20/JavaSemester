package com.example.renatojava.javasemester.util;

import com.example.renatojava.javasemester.entity.RoomA;
import com.example.renatojava.javasemester.entity.RoomB;

public class RoomChecker<T> {

    public T room;

    public RoomChecker(T room) {

        this.room = room;

    }

    public String roomType(){
        if(room instanceof RoomA){
            return "A";
        }else if(room instanceof RoomB){
            return "B";
        }else{
            return "C";
        }
    }
}
