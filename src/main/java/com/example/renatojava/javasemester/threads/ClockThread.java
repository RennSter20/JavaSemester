package com.example.renatojava.javasemester.threads;

import com.example.renatojava.javasemester.Application;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ClockThread implements Runnable{


    @Override
    public void run() {

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String now = formatter.format(date);

        Application.getStage().setTitle("Hospital Java                                                                                                                                                                                  " + now);
    }
}
