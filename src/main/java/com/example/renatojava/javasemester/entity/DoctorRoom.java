package com.example.renatojava.javasemester.entity;

import java.io.Serializable;

public class DoctorRoom implements Serializable {

    private String roomName;
    private Integer doctorID;
    private Integer roomID;

    public DoctorRoom(String roomName, Integer doctorID, Integer roomID) {
        this.roomName = roomName;
        this.doctorID = doctorID;
        this.roomID = roomID;
    }

    public void setDoctorID(Integer doctorID) {
        this.doctorID = doctorID;
    }

    public Integer getRoomID() {
        return roomID;
    }

    public void setRoomID(Integer roomID) {
        this.roomID = roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Integer getDoctorID() {
        return doctorID;
    }

    public void setDoctor(Integer doctorID) {
        this.doctorID = doctorID;
    }
}
