package com.example.renatojava.javasemester.entity;

import java.io.Serializable;

public class PatientRoom implements Serializable {

    private String roomType;

    public PatientRoom(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
}
