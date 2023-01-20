package com.example.renatojava.javasemester.entity;

public class PatientRoom {

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
