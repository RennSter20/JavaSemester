package com.example.renatojava.javasemester.entity;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.exceptions.NoProceduresException;
import com.example.renatojava.javasemester.exceptions.ObjectExistsException;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public interface Data {

    String DATABASE_FILE = "database.properties";

    private static Connection connectingToDatabase() throws IOException, SQLException {
        Properties properties = new Properties();
        properties.load(new FileReader(DATABASE_FILE));
        String url = properties.getProperty("url");
        String user = properties.getProperty("user");
        String pass = properties.getProperty("pass");
        Connection conn = DriverManager.getConnection(url,
                user,pass);
        return conn;
    }


    static List<Patient> getAllPatients(){

        List<Patient> patientList = new ArrayList<>();

        try (Connection conn = connectingToDatabase()){

            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM PATIENTS"
            );

            while(proceduresResultSet.next()){
                Patient newPatient = getPatient(proceduresResultSet);
                patientList.add(newPatient);
            }

            conn.close();

        } catch (SQLException | IOException e) {
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
    static void addPatient(String name, String surname, String gender, String oib) throws SQLException, IOException {
        Connection veza = connectingToDatabase();

        PreparedStatement stmnt = veza.prepareStatement("INSERT INTO PATIENTS(NAME, SURNAME, GENDER, DEBT, PROCEDURES, OIB) VALUES(?,?,?,?,?,?)");
        stmnt.setString(1, name);
        stmnt.setString(2, surname);
        stmnt.setString(3, gender);
        stmnt.setString(4, "0");
        stmnt.setString(5, "");
        stmnt.setString(6, oib);

        try{
            CheckObjects.checkIfPatientExists(oib);
        } catch (ObjectExistsException e) {
            Application.logger.info(e.getMessage(), e.getStackTrace());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText(e.getMessage());
            alert.show();
            veza.close();
            return;
        }

        addedSuccessfully("Patient");

        stmnt.executeUpdate();
        veza.close();
    }
    static Patient getCertainPatient(String oib){
        Patient newPatient = null;
        try(Connection conn = connectingToDatabase()) {


            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM PATIENTS WHERE OIB='" + oib + "'"
            );

            while(proceduresResultSet.next()){
                newPatient = getPatient(proceduresResultSet);
            }

            conn.close();

        } catch (SQLException | IOException e) {
            Application.logger.info(String.valueOf(e.getStackTrace()));
        }
        return newPatient;
    }
    static void removePatient(String oib) throws SQLException, IOException {
        Connection veza = connectingToDatabase();

        PreparedStatement stmnt = veza.prepareStatement("DELETE FROM PATIENTS WHERE OIB='" + oib + "'");

        stmnt.executeUpdate();
        veza.close();

    }


    static List<Procedure> getAllProcedures() throws SQLException, IOException {
        List<Procedure> procedureList = new ArrayList<>();

        try (Connection conn = connectingToDatabase()){

            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM PROCEDURES"
            );

            while(proceduresResultSet.next()){
                Procedure newProcedure = getProcedure(proceduresResultSet);
                procedureList.add(newProcedure);
            }
            if(procedureList.size() == 0){
                throw new NoProceduresException("No procedures found in database!");
            }
        }

        return procedureList;
    }
    static Procedure getProcedure(ResultSet procedureSet) throws SQLException{

        String description = procedureSet.getString("description");
        String price = procedureSet.getString("price");


        return new Procedure(description, Double.valueOf(price));

    }
    static Procedure getProcedureBasedOnDescription(String description){
        Procedure procedure = null;
        try{
            List<Procedure> allProcedures = getAllProcedures();
            procedure = allProcedures.stream().filter(procedure1 -> procedure1.description().equals(description)).findAny().orElse(null);
        }catch (SQLException | IOException e) {
            Application.logger.info(String.valueOf(e.getStackTrace()));
        }catch (NoProceduresException e){
            Application.logger.info(e.getMessage(), e.getStackTrace());
        }
        return procedure;
    }
    static String getAllProceduresFromPatient(Patient patient){
        String procedureList = "";

        try (Connection conn = connectingToDatabase()){

            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM PATIENTS WHERE OIB='" + patient.getOib() + "'"
            );

            while(proceduresResultSet.next()){
                procedureList += proceduresResultSet.getString("procedures");
            }

            conn.close();

        } catch (SQLException | IOException e) {
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

        try(Connection conn = connectingToDatabase()) {
            PreparedStatement updateProcedures = conn.prepareStatement("UPDATE PATIENTS SET PROCEDURES='" + procedures + "'" + "WHERE OIB='" + oib + "'");
            updateProcedures.executeUpdate();

            List<Procedure> allProcedures = getAllProcedures();
            Procedure procedureToFind = allProcedures.stream().filter(procedure1 -> procedure1.description().equals(procedure)).findAny().orElse(null);
            double debtToAdd = procedureToFind.price();

            double oldDebt = patientToUpdate.getDebt();
            double newDebt = oldDebt + debtToAdd;

            PreparedStatement updateDebt = conn.prepareStatement("UPDATE PATIENTS SET DEBT=" + newDebt + "WHERE OIB='" + patientToUpdate.getOib() + "'");
            updateDebt.executeUpdate();

        } catch (SQLException | IOException e) {
            Application.logger.info(String.valueOf(e.getStackTrace()));
        }catch (NoProceduresException e){
            Application.logger.info(e.getMessage(), e.getStackTrace());
        }
    }
    static void removeProcedure(String procedureDescription, String oib, String currentProcedures){
        Patient patient = getCertainPatient(oib);
        Procedure procedure = getProcedureBasedOnDescription(procedureDescription);

        List<String> currentProceduresSplitted = List.of(currentProcedures.split("/"));;

        try (Connection conn = connectingToDatabase()){

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

        } catch (SQLException | IOException e) {
            Application.logger.info(String.valueOf(e.getStackTrace()));
        }

    }


    static List<Doctor> getAllDoctors() throws SQLException, IOException {
        List<Doctor> doctorList = new ArrayList<>();

        try(Connection conn = connectingToDatabase()) {
            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM DOCTORS"
            );

            while(proceduresResultSet.next()){
                Doctor newDoctor = getDoctor(proceduresResultSet);
                doctorList.add(newDoctor);
            }

            conn.close();

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

    static Boolean confirmEdit(){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRMATION");
        alert.setHeaderText("Are you sure you want to make this edit in database?");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
        alert.setContentText("");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            return true;
        }else{
            return false;
        }
    }
    static void addedSuccessfully(String type){
        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setTitle("INFORMATION");
        success.setHeaderText("Success!");
        success.setContentText(type + " successfully added to the system!");
        success.show();
    }
}
