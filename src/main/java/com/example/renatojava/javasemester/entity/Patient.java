package com.example.renatojava.javasemester.entity;

import java.util.ArrayList;
import java.util.List;

public class Patient extends Person {

    private String id;
    private double debt;
    private String procedures;
    private String oib;

    public Patient(String id, String name, String surname, String gender,double debt, String procedures, String oib) {
        super(name, surname, gender);
        this.id = id;
        this.debt = debt;
        this.procedures = procedures;
        this.oib = oib;
    }

    public static class Builder{
        private String id;
        private String name;
        private String surname;
        private String gender;
        private String oib;
        private Double debt;
        private String procedures;

        public Patient build(){
            return new Patient(id, name, surname, gender,debt, procedures, oib);
        }
        public Builder withId(String id){
            this.id = id;
            return this;
        }
        public Builder withName(String name){
            this.name = name;
            return this;
        }
        public Builder withSurname(String surname){
            this.surname = surname;
            return this;
        }
        public Builder withGender(String gender){
            this.gender = gender;
            return this;
        }
        public Builder withOIB(String oib){
            this.oib = oib;
            return this;
        }
        public Builder withDebt(Double debt){
            this.debt = debt;
            return this;
        }
        public Builder withProcedures(String proc){
            this.procedures = proc;
            return this;
        }
    }

    public double getDebt() {
        return debt;
    }

    public void setDebt(double debt) {
        this.debt = debt;
    }

    public String getProcedures() {
        return procedures;
    }

    public void setProcedures(String procedures) {
        this.procedures = procedures;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOib() {
        return oib;
    }

    public void setOib(String oib) {
        this.oib = oib;
    }
}
