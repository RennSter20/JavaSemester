package com.example.renatojava.javasemester.entity;

public record Procedure(String description, Double price) {
    public Procedure(String description, Double price) {
        this.description = description;
        this.price = price;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public Double price() {
        return price;
    }
}
