package com.example.renatojava.javasemester.entity;

public class Room {

    private String name;
    private Doctor doctor;

    public Room(String name, Doctor doctor) {
        this.name = name;
        this.doctor = doctor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
