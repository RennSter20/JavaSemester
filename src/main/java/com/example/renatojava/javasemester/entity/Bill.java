package com.example.renatojava.javasemester.entity;

import java.time.LocalDateTime;

public class Bill {

    private Patient patient;
    private LocalDateTime time;

    public Bill(Patient patient, LocalDateTime time) {
        this.patient = patient;
        this.time = time;
    }
}
