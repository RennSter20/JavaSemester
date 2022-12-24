package com.example.renatojava.javasemester.entity;

public class User {

    private String id,password,name,surname,role, oib;

    public User(String id, String password, String name, String surname, String role, String oib) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.oib = oib;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getOib() {
        return oib;
    }

    public void setOib(String oib) {
        this.oib = oib;
    }
}
