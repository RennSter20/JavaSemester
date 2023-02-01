package com.example.renatojava.javasemester.threads;

import com.example.renatojava.javasemester.entity.Hospital;

public class FreeBed implements Runnable {

    private final Hospital hospital;

    public FreeBed(Hospital hospital) {
        this.hospital = hospital;
    }

    @Override
    public void run() {
        hospital.freeBed();
    }

}
