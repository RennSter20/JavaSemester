package com.example.renatojava.javasemester.entity;

import com.example.renatojava.javasemester.database.PatientData;

public class Hospital {

    private static Integer occupiedBeds;

    public Hospital() {
        occupiedBeds = PatientData.getAllPatients().size();
    }

    public synchronized void occupyBed() {
        occupiedBeds++;
    }
    public synchronized void freeBed() {
        occupiedBeds--;
    }

    public synchronized Integer getOccupiedBeds(){
        return occupiedBeds;
    }
}
