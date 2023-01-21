package com.example.renatojava.javasemester.util;

import com.example.renatojava.javasemester.entity.Data;
import com.example.renatojava.javasemester.entity.Doctor;
import com.example.renatojava.javasemester.entity.DoctorRoom;
import com.example.renatojava.javasemester.entity.Patient;
import com.example.renatojava.javasemester.exceptions.ObjectExistsException;
import com.example.renatojava.javasemester.patientControllers.RegisterPatientScreenController;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public sealed interface CheckObjects permits RegisterPatientScreenController {

    static void checkIfPatientExists(String oib) throws ObjectExistsException{
        List<Patient> patientsList = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/production", "student", "student");

            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM PATIENTS WHERE OIB='" + oib + "'"
            );

            while(proceduresResultSet.next()){
                Patient newPatient = Data.getPatient(proceduresResultSet);
                patientsList.add(newPatient);
            }

            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(patientsList.size() > 0){
            throw new ObjectExistsException("Patient already exists in system!");
        }
    }

    static void checkIfDoctorExists(Doctor doctor) throws ObjectExistsException{
        List<Doctor> doctorsList = new ArrayList<>();
        try {
            Connection conn = Data.connectingToDatabase();

            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM DOCTORS WHERE NAME='" + doctor.getName() + "' AND SURNAME='" + doctor.getSurname() + "'"
            );

            while(proceduresResultSet.next()){
                Doctor newDoctor = Data.getDoctor(proceduresResultSet);
                doctorsList.add(newDoctor);
            }

            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(doctorsList.size() > 0){
            throw new ObjectExistsException("Doctor already exists in system!");
        }
    }

    static void checkIfRoomExists(String roomName) throws ObjectExistsException{
        DoctorRoom foundDoctorRoom = null;
        try(Connection conn = Data.connectingToDatabase()){
            Statement sqlStatement = conn.createStatement();
            ResultSet roomResults = sqlStatement.executeQuery(
                    "SELECT * FROM HOSPITAL WHERE ROOM='" + roomName + "'"
            );
            while(roomResults.next()){
                foundDoctorRoom = Data.getRoom(roomResults);
            }

            if(foundDoctorRoom != null && foundDoctorRoom.getDoctorID() != -1){
                throw new ObjectExistsException("Another doctor is in this room!");
            }else if(foundDoctorRoom != null && foundDoctorRoom.getDoctorID() == -1){
                Data.removeRoom(foundDoctorRoom.getRoomID());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static boolean isValidTime(String input, DateTimeFormatter format) {
        try {
            LocalDate time = LocalDate.parse(input, format);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    static Boolean isBeforeToday(LocalDateTime dateTimeValue){
        if(dateTimeValue.isBefore(LocalDateTime.now())){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INFORMATION");
            alert.setHeaderText("Date is not valid!");
            alert.setContentText("Checkup date needs to be after today's date!");
            alert.show();
            return true;
        }else{
            return false;
        }

    }

}
