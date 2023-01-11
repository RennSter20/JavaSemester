package com.example.renatojava.javasemester.entity;

import com.example.renatojava.javasemester.Application;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public interface Data {

    static List<Patient> getAllPatients(){

        List<Patient> patientList = new ArrayList<>();

        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/production", "student", "student");

            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM PATIENTS"
            );

            while(proceduresResultSet.next()){
                Patient newPatient = getPatient(proceduresResultSet);
                patientList.add(newPatient);
            }

            conn.close();

        } catch (SQLException e) {
            Application.logger.info(String.valueOf(e.getStackTrace()));
        }

        return patientList;
    }

    static Patient getPatient(ResultSet procedureSet) throws SQLException{

        String name = procedureSet.getString("name");
        String surname = procedureSet.getString("surname");
        String gender = procedureSet.getString("gender");
        double debt = procedureSet.getDouble("debt");
        String procedures = procedureSet.getString("procedures");
        String oib = procedureSet.getString("oib");


        Patient patientToAdd = new Patient(null, name, surname, gender, debt, procedures, oib);

        return patientToAdd;

    }

    static Patient getCertainPatient(String oib){
        Patient newPatient = null;
        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/production", "student", "student");

            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM PATIENTS WHERE OIB='" + oib + "'"
            );

            while(proceduresResultSet.next()){
                newPatient = getPatient(proceduresResultSet);
            }

            conn.close();

        } catch (SQLException e) {
            Application.logger.info(String.valueOf(e.getStackTrace()));
        }
        return newPatient;
    }

    static List<Procedure> getAllProcedures(){
        List<Procedure> procedureList = new ArrayList<>();

        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/production", "student", "student");

            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM PROCEDURES"
            );

            while(proceduresResultSet.next()){
                Procedure newProcedure = getProcedure(proceduresResultSet);
                procedureList.add(newProcedure);
            }

            conn.close();

        } catch (SQLException e) {
            Application.logger.info(String.valueOf(e.getStackTrace()));
        }

        return procedureList;
    }
    static Procedure getProcedure(ResultSet procedureSet) throws SQLException{

        String description = procedureSet.getString("description");
        String price = procedureSet.getString("price");


        return new Procedure(description, Double.valueOf(price));

    }
    static Procedure getProcedureBasedOnDescription(String description){
        List<Procedure> allProcedures = getAllProcedures();
        Procedure procedure = allProcedures.stream().filter(procedure1 -> procedure1.description().equals(description)).findAny().orElse(null);

        return procedure;
    }

    static String getAllProceduresFromPatient(Patient patient){
        String procedureList = "";

        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/production", "student", "student");

            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM PATIENTS WHERE OIB='" + patient.getOib() + "'"
            );

            while(proceduresResultSet.next()){
                procedureList += proceduresResultSet.getString("procedures");
            }

            conn.close();

        } catch (SQLException e) {
            Application.logger.info(String.valueOf(e.getStackTrace()));
        }

        return procedureList;
    }

    static void addProcedureToPatient(String oib, String procedure){

        Patient patientToUpdate = getCertainPatient(oib);
        String procedures = patientToUpdate.getProcedures();

        if(procedures.equals("")){
            procedures = procedure + "/";
        }else{
            procedures = procedures + procedure + "/";
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/production", "student", "student");

            PreparedStatement updateProcedures = conn.prepareStatement("UPDATE PATIENTS SET PROCEDURES='" + procedures + "'" + "WHERE OIB='" + oib + "'");
            updateProcedures.executeUpdate();

            List<Procedure> allProcedures = getAllProcedures();
            Procedure procedureToFind = allProcedures.stream().filter(procedure1 -> procedure1.description().equals(procedure)).findAny().orElse(null);
            double debtToAdd = procedureToFind.price();

            double oldDebt = patientToUpdate.getDebt();
            double newDebt = oldDebt + debtToAdd;

            PreparedStatement updateDebt = conn.prepareStatement("UPDATE PATIENTS SET DEBT=" + newDebt + "WHERE OIB='" + patientToUpdate.getOib() + "'");
            updateDebt.executeUpdate();

            conn.close();

        } catch (SQLException e) {
            Application.logger.info(String.valueOf(e.getStackTrace()));
        }
    }

    static void addPatient(String name, String surname, String gender, String oib) throws SQLException {
        Connection veza = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/production", "student", "student");

        PreparedStatement stmnt = veza.prepareStatement("INSERT INTO PATIENTS(NAME, SURNAME, GENDER, DEBT, PROCEDURES, OIB) VALUES(?,?,?,?,?,?)");
        stmnt.setString(1, name);
        stmnt.setString(2, surname);
        stmnt.setString(3, gender);
        stmnt.setString(4, "0");
        stmnt.setString(5, "");
        stmnt.setString(6, oib);

        if(!CheckObjects.checkIfPatientExists(oib)){
            stmnt.executeUpdate();
            Alert alertConfirmation = new Alert(Alert.AlertType.INFORMATION);
            alertConfirmation.setTitle("Success");
            alertConfirmation.setHeaderText("Patient is now registered in system.");
            alertConfirmation.show();
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText("Patient already exists in system.");
            alert.show();
        }
        veza.close();
    }

    static void removeProcedure(String procedureDescription, String oib, String currentProcedures){
        Patient patient = getCertainPatient(oib);
        Procedure procedure = getProcedureBasedOnDescription(procedureDescription);

        List<String> currentProceduresSplitted = List.of(currentProcedures.split("/"));;

        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/production", "student", "student");

            Boolean removed = false;
            String newProcedureString = "";
            for(String proc : currentProceduresSplitted){
                if(proc.equals(procedureDescription) && !removed){
                    removed = true;
                }else{
                    newProcedureString = newProcedureString + proc + "/";
                }
            }

            PreparedStatement updateProcedures = conn.prepareStatement("UPDATE PATIENTS SET PROCEDURES='" + newProcedureString + "'" + "WHERE OIB='" + oib + "'");
            updateProcedures.executeUpdate();

            double newDebt = patient.getDebt() - procedure.price();
            PreparedStatement updateDebt = conn.prepareStatement("UPDATE PATIENTS SET DEBT=" + newDebt + "WHERE OIB='" + patient.getOib() + "'");
            updateDebt.executeUpdate();

            conn.close();

        } catch (SQLException e) {
            Application.logger.info(String.valueOf(e.getStackTrace()));
        }

    }

    static List<Doctor> getAllDoctors(){
        List<Doctor> doctorList = new ArrayList<>();

        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/production", "student", "student");

            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM DOCTORS"
            );

            while(proceduresResultSet.next()){
                Doctor newDoctor = getDoctor(proceduresResultSet);
                doctorList.add(newDoctor);
            }

            conn.close();

        } catch (SQLException e) {
            Application.logger.info(String.valueOf(e.getStackTrace()));
        }

        return doctorList;
    }

    static Doctor getDoctor(ResultSet procedureSet) throws SQLException{

        String gender = procedureSet.getString("gender");
        String name = procedureSet.getString("name");
        String surname = procedureSet.getString("surname");
        String room = procedureSet.getString("room");
        String title = procedureSet.getString("title");


        return new Doctor.Builder().withName(name).withSurname(surname).withGender(gender).withRoom(room).withTitle(title).build();

    }
}
