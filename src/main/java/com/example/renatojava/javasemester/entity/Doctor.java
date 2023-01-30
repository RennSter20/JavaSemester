package com.example.renatojava.javasemester.entity;

public class Doctor extends Person{

    private Integer id;
    private String room;
    private String title;

    public Doctor(Integer id, String name, String surname, String gender, String room, String title) {
        super(name, surname,gender);
        this.room = room;
        this.title = title;
        this.id = id;
    }

    public String getDoctorFullName(){
        return getName() + " " + getSurname();
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

        private Integer id;
        private String room;
        private String title;
        private String name,surname, gender;
        public Doctor build() {
            return new Doctor(id, name, surname,gender,room,title);
        }
        public Builder withName(String name){
            this.name = name;
            return this;
        }
        public Builder withId(Integer id){
            this.id = id;
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

    @Override
    public String toString() {
        return "Information about doctor: \n" +
                "Full name: " + title + " " + getDoctorFullName() + "\n" +
                "Gender: " + getGender() + "\n" +
                "Room: " + room + "\n";

    }
}
