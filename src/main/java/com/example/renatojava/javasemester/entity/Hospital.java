package com.example.renatojava.javasemester.entity;

import com.example.renatojava.javasemester.database.PatientData;

public class Hospital {

    private static Integer occupiedBeds;

    public Hospital() {
        occupiedBeds = PatientData.getAllPatients().size();
        System.out.println("Occupied beds: " + occupiedBeds);
    }

    public void occupyBed() {
        synchronized (this){
            occupiedBeds++;
        }
        System.out.println("After occupying");
        System.out.println("Occupied beds: " + occupiedBeds);
    }
    public void freeBed() {
        synchronized (this){
            occupiedBeds--;
        }
        System.out.println("After freeing");
        System.out.println("Occupied beds: " + occupiedBeds);
    }

    public Integer getOccupiedBeds(){
        synchronized (this){
            return occupiedBeds;
        }
    }
}
