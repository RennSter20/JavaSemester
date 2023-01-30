package com.example.renatojava.javasemester.entity;

import java.time.LocalDate;

public class Patient extends Person {

    private Integer id;
    private double debt;
    private String procedures;
    private String oib;
    private LocalDate date;

    public Patient(Integer id, String name, String surname, String gender,double debt, String procedures, String oib, LocalDate date) {
        super(name, surname, gender);
        this.id = id;
        this.debt = debt;
        this.procedures = procedures;
        this.oib = oib;
        this.date = date;
    }

    @Override
    public String toString() {
        if(date == null){
            return "Information about patient: " + "\n" +
                    "Full name: " + getFullName() + "\n" +
                    "OIB: " + oib + "\n" +
                    "Birth date: "+ "-" + "\n" +
                    "Procedures: " + procedures + "\n" +
                    "Debt: " + debt;
        }else{
            return "Information about patient: " + "\n" +
                    "Full name: " + getFullName() + "\n" +
                    "OIB: " + oib + "\n" +
                    "Birth date: "+ date.toString() + "\n" +
                    "Procedures: " + procedures + "\n" +
                    "Debt: " + debt;
        }
    }

    public static class Builder{
        private Integer id;
        private String name;
        private String surname;
        private String gender;
        private String oib;
        private Double debt;
        private String procedures;
        private LocalDate date;

        public Patient build(){
            return new Patient(id, name, surname, gender,debt, procedures, oib, date);
        }
        public Builder withId(Integer id){
            this.id = id;
            return this;
        }
        public Builder withDate(LocalDate date){
            this.date = date;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOib() {
        return oib;
    }

    public void setOib(String oib) {
        this.oib = oib;
    }

    public String getFullName(){
        return getName() + " " + getSurname();
    }
}
