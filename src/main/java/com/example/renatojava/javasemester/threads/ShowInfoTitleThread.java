package com.example.renatojava.javasemester.threads;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.entity.Hospital;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ShowInfoTitleThread implements Runnable{

    private final Hospital hospital;

    public ShowInfoTitleThread(Hospital hospital) {
        this.hospital = hospital;
    }

    @Override
    public void run() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String now = formatter.format(date);

        Integer occupiedBeds = hospital.getOccupiedBeds();

        Application.getStage().setTitle("Hospital Java                                                                                                                                                     " + now + " ---- Occupied beds: " + occupiedBeds);
    }
}
