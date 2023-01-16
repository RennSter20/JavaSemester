package com.example.renatojava.javasemester.entity;

public record Stats(Integer id, Integer patients, double debt, Integer doctors, Integer bills) {

    public Stats(Integer id, Integer patients, double debt,Integer doctors, Integer bills) {
        this.id = id;
        this.patients = patients;
        this.debt = debt;
        this.doctors = doctors;
        this.bills = bills;
    }

    @Override
    public Integer id() {
        return id;
    }

    @Override
    public Integer patients() {
        return patients;
    }

    @Override
    public double debt() {
        return debt;
    }

    @Override
    public Integer doctors() {
        return doctors;
    }

    @Override
    public Integer bills() {
        return bills;
    }
}
