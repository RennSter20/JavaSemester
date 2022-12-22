package com.example.renatojava.javasemester.entity;

public class Doctor extends Person{

    private String room;
    private String title;

    public Doctor(String id, String name, String surname, String room, String title) {
        super(id, name, surname);
        this.room = room;
        this.title = title;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
