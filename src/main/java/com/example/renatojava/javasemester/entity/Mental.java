package com.example.renatojava.javasemester.entity;

public class Mental extends Room{

    public Mental(String name, Doctor doctor, Integer roomID) {
        super(name, doctor.getId(), roomID);
    }
}
