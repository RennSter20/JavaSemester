package com.example.renatojava.javasemester.entity;

import java.util.List;

public class Hospital<T> {

    private List<T> rooms;

    public Hospital(List<T> rooms) {
        this.rooms = rooms;
    }

    public List<T> getRooms() {
        return rooms;
    }

    public void setRooms(List<T> rooms) {
        this.rooms = rooms;
    }
}
