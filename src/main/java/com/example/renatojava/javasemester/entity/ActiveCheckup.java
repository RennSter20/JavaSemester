package com.example.renatojava.javasemester.entity;

import com.example.renatojava.javasemester.database.PatientData;
import com.example.renatojava.javasemester.database.ProcedureData;
import com.example.renatojava.javasemester.util.DateFormatter;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ActiveCheckup<T extends PatientRoom> implements Serializable {

    private Integer id;
    private LocalDateTime dateOfCheckup;
    private Integer patientID;
    private Integer procedureID;
    private T room;

    public ActiveCheckup(Integer id, LocalDateTime dateOfCheckup, Integer patientID, Integer procedureID, T room) {
        this.dateOfCheckup = dateOfCheckup;
        this.patientID = patientID;
        this.procedureID = procedureID;
        this.room = room;
        this.id = id;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDateOfCheckup() {
        return dateOfCheckup;
    }

    public void setDateOfCheckup(LocalDateTime dateOfCheckup) {
        this.dateOfCheckup = dateOfCheckup;
    }

    public Integer getPatientID() {
        return patientID;
    }

    public void setPatientID(Integer patientID) {
        this.patientID = patientID;
    }

    public Integer getProcedureID() {
        return procedureID;
    }

    public void setProcedureID(Integer procedureID) {
        this.procedureID = procedureID;
    }

    public T getRoom() {
        return room;
    }

    public void setRoom(T room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "Information about checkup:\n" +
                "Date of checkup: " + DateFormatter.getDateTimeFormatted(dateOfCheckup.toString()) + "\n" +
                "Patient: " + PatientData.getPatientWithID(patientID).getFullName() + "\n" +
                "Procedure: " + ProcedureData.getProcedureFromId(procedureID) + "\n" +
                "Room: " + room.getRoomType();
    }
}
