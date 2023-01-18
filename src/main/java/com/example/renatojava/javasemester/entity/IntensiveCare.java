package com.example.renatojava.javasemester.entity;

public class IntensiveCare extends Room{

    public IntensiveCare(String name, Doctor doctor, Integer roomID) {
        super(name, doctor.getId(), roomID);
    }
}
