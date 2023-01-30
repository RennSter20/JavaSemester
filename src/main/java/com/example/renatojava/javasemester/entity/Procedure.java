package com.example.renatojava.javasemester.entity;

import java.io.Serializable;

public record Procedure(Integer id, String description, Double price) implements Serializable {
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

    @Override
    public String toString() {
        return "Information about procedure:\n" +
            "Description: " + description + "\n" +
            "Price: " + price;
    }
}
