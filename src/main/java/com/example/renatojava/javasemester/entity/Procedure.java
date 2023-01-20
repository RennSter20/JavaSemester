package com.example.renatojava.javasemester.entity;

public record Procedure(Integer id, String description, Double price) {
    public Procedure(Integer id, String description, Double price) {
        this.description = description;
        this.price = price;
        this.id = id;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public Double price() {
        return price;
    }

    @Override
    public Integer id() {
        return id;
    }
}
