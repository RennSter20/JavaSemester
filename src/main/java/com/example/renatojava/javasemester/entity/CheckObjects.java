package com.example.renatojava.javasemester.entity;

import com.example.renatojava.javasemester.RegisterPatientScreenController;
import com.example.renatojava.javasemester.exceptions.ObjectExistsException;

import java.io.IOException;
import java.sql.*;
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
        List<Doctor> patientsList = new ArrayList<>();
        try {
            Connection conn = Data.connectingToDatabase();

            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM DOCTORS WHERE ROOM='" + doctor.getRoom() + "'"
            );

            while(proceduresResultSet.next()){
                Doctor newDoctor = Data.getDoctor(proceduresResultSet);
                patientsList.add(newDoctor);
            }

            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(patientsList.size() > 0){
            throw new ObjectExistsException("Doctor already exists in system!");
        }
    }

}
