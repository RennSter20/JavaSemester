package com.example.renatojava.javasemester.threads;

import com.example.renatojava.javasemester.entity.Hospital;

public class OccupyBed implements Runnable{

    private final Hospital hospital;

    public OccupyBed(Hospital hospital) {
        this.hospital = hospital;
    }

    @Override
    public void run() {
        hospital.occupyBed();
    }
}
