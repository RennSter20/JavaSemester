package com.example.renatojava.javasemester.entity;

public class Surgical extends Room{

    public Surgical(String name, Doctor doctor, Integer roomID) {
        super(name, doctor.getId(), roomID);
    }
}
