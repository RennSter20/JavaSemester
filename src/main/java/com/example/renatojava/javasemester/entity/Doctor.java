package com.example.renatojava.javasemester.entity;

public class Doctor extends Person{

    private String room;
    private String title;

    public Doctor(String name, String surname, String gender, String room, String title) {
        super(name, surname,gender);
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

    public static class Builder {

        private String room;
        private String title;
        private String name,surname, gender;
        public Doctor build() {
            return new Doctor(name, surname,gender,room,title);
        }
        public Builder withName(String name){
            this.name = name;
            return this;
        }
        public Builder withGender(String gender){
            this.gender = gender;
            return this;
        }
        public Builder withSurname(String surname){
            this.surname = surname;
            return this;
        }
        public Builder withRoom(String room){
            this.room = room;
            return this;
        }
        public Builder withTitle(String title){
            this.title = title;
            return this;
        }
    }
}
