package com.example.renatojava.javasemester.util;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.database.*;
import com.example.renatojava.javasemester.entity.*;
import com.example.renatojava.javasemester.exceptions.ObjectExistsException;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public interface CheckObjects  {

    static void checkIfPatientExists(String oib) throws ObjectExistsException{
        List<Patient> patientsList = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/production", "student", "student");

            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM PATIENTS WHERE OIB='" + oib + "'"
            );

            while(proceduresResultSet.next()){
                Patient newPatient = PatientData.getPatient(proceduresResultSet);
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
                Doctor newDoctor = DoctorData.getDoctor(proceduresResultSet);
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
                foundDoctorRoom = DoctorRoomData.getRoom(roomResults);
            }

            if(foundDoctorRoom != null && foundDoctorRoom.getDoctorID() != -1){
                throw new ObjectExistsException("Another doctor is in this room!");
            }else if(foundDoctorRoom != null && foundDoctorRoom.getDoctorID() == -1){
                DoctorRoomData.removeRoom(foundDoctorRoom.getRoomID());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void checkIfProcedureExists(String description, Double price) throws ObjectExistsException{
        Procedure foundProcedure = null;
        try(Connection conn = Data.connectingToDatabase()){
            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResult = sqlStatement.executeQuery(
                    "SELECT * FROM PROCEDURES WHERE DESCRIPTION='" + description + "' AND PRICE=" + price
            );
            while(proceduresResult.next()){
                foundProcedure = ProcedureData.getProcedure(proceduresResult);
            }
            if(foundProcedure != null){
                throw new ObjectExistsException("Procedure already exists!");
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

    static Boolean checkIfPatientsHaveProcedure(String procedure){
        List<Patient> allPatients = PatientData.getAllPatients();
        List<ActiveCheckup> allCheckups = CheckupData.getAllActiveCheckups();

        for(Patient p : allPatients){
            if(p.getProcedures().contains(procedure)){
                return true;
            }
        }

        for(ActiveCheckup a : allCheckups){
            if(a.getProcedureID().equals(ProcedureData.getProcedureFromDescription(procedure).id())){
                return true;
            }
        }
        return false;
    }

    static Boolean checkCheckupTime(LocalDateTime localDateTime){
        List<ActiveCheckup> allCheckups = CheckupData.getAllActiveCheckups();
        for(ActiveCheckup checkup : allCheckups){
            long duration = Duration.between(checkup.getDateOfCheckup(), localDateTime).toMinutes();
            if(duration < 15 && duration > -15){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Error while adding checkup");
                alert.setContentText("Doctors are not available at this time, please select 15 minutes after or before other checkups!");
                alert.show();
                return false;
            }
        }
        return true;
    }

    static Boolean checkIfHospitalHasDoctors(){
        try{
            if(DoctorData.getAllDoctors().size() == 0){
                Alert noDoctors = new Alert(Alert.AlertType.ERROR);
                noDoctors.setTitle("ERROR");
                noDoctors.setHeaderText("Error while managing checkups!");
                noDoctors.setContentText("To schedule a checkup, please add doctors to system.");
                noDoctors.show();
                return false;
            }
        }catch (SQLException | IOException e){
            Application.logger.error(e.getMessage(), e);
        }
        return true;
    }

}
