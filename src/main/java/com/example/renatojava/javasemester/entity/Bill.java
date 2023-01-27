package com.example.renatojava.javasemester.entity;

import java.time.LocalDateTime;

public class Bill {

    private Patient patient;
    private LocalDateTime time;

    public Bill(Patient patient, LocalDateTime time) {
        this.patient = patient;
        this.time = time;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
