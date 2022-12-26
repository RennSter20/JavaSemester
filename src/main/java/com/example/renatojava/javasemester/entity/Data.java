package com.example.renatojava.javasemester.entity;

import com.example.renatojava.javasemester.Application;

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
        String debt = procedureSet.getString("debt");
        String procedures = procedureSet.getString("procedures");
        String oib = procedureSet.getString("oib");

        return new Patient(null, name,surname,gender, Double.valueOf(debt),procedures,oib);

    }

    static Patient getCertainPatient(String oib){
        Patient newPatient = null;
        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/production", "student", "student");

            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM PATIENTS WHERE OIB=" + oib
            );

             newPatient = getPatient(proceduresResultSet);

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

    static List<Procedure> getAllProceduresFromPatient(Patient patient){
        List<Procedure> procedureList = new ArrayList<>();

        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/production", "student", "student");

            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM PROCEDURES WHERE OIB=" + patient.getOib()
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

    static void addProcedureToPatient(String oib, String procedure){

        Patient patientToUpdate = getCertainPatient(oib);
        String procedures = patientToUpdate.getProcedures();

        if(procedures.equals("")){
            procedures += procedure + "/";
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/production", "student", "student");

            PreparedStatement sqlStatement = conn.prepareStatement("UPDATE PATIENTS SET PROCEDURES=" + procedures + "WHERE OIB=" + oib);
            sqlStatement.executeUpdate();

            conn.close();

        } catch (SQLException e) {
            Application.logger.info(String.valueOf(e.getStackTrace()));
        }
    }

}
