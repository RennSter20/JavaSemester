package com.example.renatojava.javasemester.entity;

import com.example.renatojava.javasemester.database.DoctorData;

import java.io.Serializable;

public class DoctorRoom implements Serializable {

    private String roomName;
    private Integer doctorID;
    private Integer roomID;
    private String doctorName;

    public DoctorRoom(String roomName, Integer doctorID, Integer roomID, String doctorName) {
        this.roomName = roomName;
        this.doctorID = doctorID;
        this.roomID = roomID;
        this.doctorName = doctorName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
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

    @Override
    public String toString() {
        return "Information about room: \n" +
                "Room name: " + roomName + "\n" +
                "Doctor name: " + DoctorData.getCertainDoctorFromId(doctorID).getDoctorFullName() + "\n";
    }
}
