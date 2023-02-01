package com.example.renatojava.javasemester.database;


import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.entity.Change;
import com.example.renatojava.javasemester.util.ChangeWriter;
import com.example.renatojava.javasemester.entity.Patient;
import com.example.renatojava.javasemester.entity.Stats;
import com.example.renatojava.javasemester.exceptions.ObjectExistsException;
import com.example.renatojava.javasemester.util.CheckObjects;
import com.example.renatojava.javasemester.util.Notification;
import com.example.renatojava.javasemester.util.StatsChanger;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface PatientData {

    static List<Patient> getAllPatients(){

        List<Patient> patientList = new ArrayList<>();

        try (Connection conn = Data.connectingToDatabase()){

            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM PATIENTS"
            );

            while(proceduresResultSet.next()){
                Patient newPatient = getPatient(proceduresResultSet);
                patientList.add(newPatient);
            }

        } catch (SQLException | IOException e) {
            Application.logger.error(e.getMessage(), e);
        }

        return patientList;
    }
    static Patient getPatient(ResultSet procedureSet) throws SQLException{

        Integer id = procedureSet.getInt("id");
        String name = procedureSet.getString("name");
        String surname = procedureSet.getString("surname");
        String gender = procedureSet.getString("gender");
        double debt = procedureSet.getDouble("debt");
        String procedures = procedureSet.getString("procedures");
        String oib = procedureSet.getString("oib");
        LocalDate date = procedureSet.getTimestamp("date").toLocalDateTime().toLocalDate();


        Patient patientToAdd = new Patient.Builder().withId(id).withName(name).withSurname(surname).withGender(gender).withOIB(oib).withDebt(debt).withProcedures(procedures).withDate(date).build();

        return patientToAdd;

    }
    static void addPatient(String name, String surname, String gender, String oib, LocalDate date) throws SQLException, IOException {
        Connection veza = Data.connectingToDatabase();

        try{
            CheckObjects.checkIfPatientExists(oib);

        } catch (ObjectExistsException e) {

            Application.logger.error(e.getMessage(), e);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText(e.getMessage());
            alert.show();
            veza.close();
            return;
        }

        PreparedStatement stmnt = veza.prepareStatement("INSERT INTO PATIENTS(NAME, SURNAME, GENDER, DEBT, PROCEDURES, OIB, DATE) VALUES(?,?,?,?,?,?,?)");
        stmnt.setString(1, name);
        stmnt.setString(2, surname);
        stmnt.setString(3, gender);
        stmnt.setString(4, "0");
        stmnt.setString(5, "");
        stmnt.setString(6, oib);
        stmnt.setDate(7, Date.valueOf(date));

        stmnt.executeUpdate();

        Stats currentStats = StatsData.getCurrentStats();
        Integer currPatients = currentStats.patients();
        List<String> changesSQL = new ArrayList<>();
        changesSQL.add("PATIENTS=" + (++currPatients));
        StatsChanger.changeStats(changesSQL);

        Change change = new Change(new Patient(-1, "-", "-", "-", 0, "-", "-", null), new Patient(PatientData.getPatientWithOib(oib).getId(), name, surname, gender, 0.0, "", oib, date));
        ChangeWriter changeWriter = new ChangeWriter(change);
        changeWriter.addChange(Application.getLoggedUser().getRole());
        Notification.addedSuccessfully("Patient");

        veza.close();
    }
    static Patient getPatientWithOib(String oib){
        Patient newPatient = null;
        try(Connection conn = Data.connectingToDatabase()) {


            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM PATIENTS WHERE OIB='" + oib + "'"
            );

            while(proceduresResultSet.next()){
                newPatient = getPatient(proceduresResultSet);
            }

        } catch (SQLException | IOException e) {
            Application.logger.error(e.getMessage(), e);
        }
        return newPatient;
    }

    static Patient getPatientWithID(Integer id){
        Patient newPatient = null;
        try(Connection conn = Data.connectingToDatabase()) {


            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM PATIENTS WHERE ID='" + id + "'"
            );

            while(proceduresResultSet.next()){
                newPatient = getPatient(proceduresResultSet);
            }

        } catch (SQLException | IOException e) {
            Application.logger.error(e.getMessage(), e);
        }
        return newPatient;
    }
    static void removePatient(Integer id) throws SQLException, IOException {

        Connection veza = Data.connectingToDatabase();
        Patient patientToRemove = getPatientWithID(id);

        PreparedStatement stmnt = veza.prepareStatement("DELETE FROM PATIENTS WHERE ID=" + id);
        stmnt.executeUpdate();

        Stats currentStats = StatsData.getCurrentStats();
        Double currentDebt = currentStats.debt();
        Integer newPatientCount = currentStats.patients() - 1;
        List<String> changesSQL = new ArrayList<>();
        changesSQL.add("PATIENTS=" + (newPatientCount));
        changesSQL.add("DEBT=" + (currentDebt - patientToRemove.getDebt()) + "");
        StatsChanger.changeStats(changesSQL);

        CheckupData.removeAllActiveCheckupsFromPatient(id);

        Change change = new Change(new Patient(patientToRemove.getId(), patientToRemove.getName(), patientToRemove.getSurname(), patientToRemove.getGender(), patientToRemove.getDebt(), patientToRemove.getProcedures(), patientToRemove.getOib(), patientToRemove.getDate()), new Patient(-1, "-", "-", "-", 0, "-", "-", null));
        ChangeWriter changeWriter = new ChangeWriter(change);
        changeWriter.addChange(Application.getLoggedUser().getRole());

        veza.close();

    }
    static void updatePatient(String newName, String newSurname,String newOib,Patient oldPatient){

        try(Connection conn = Data.connectingToDatabase()) {

            PreparedStatement stmnt = conn.prepareStatement("UPDATE PATIENTS SET NAME='" + newName + "', SURNAME='" + newSurname + "' WHERE OIB='" + oldPatient.getOib() + "'");
            stmnt.executeUpdate();
            stmnt = conn.prepareStatement("UPDATE PATIENTS SET OIB='" + newOib + "' WHERE NAME='" + newName + "' AND SURNAME='" + newSurname + "' AND GENDER='" + oldPatient.getGender() + "'");
            stmnt.executeUpdate();

            Change change = new Change(oldPatient, PatientData.getPatientWithOib(newOib));
            ChangeWriter changeWriter = new ChangeWriter(change);
            changeWriter.addChange(Application.getLoggedUser().getRole());

            Notification.updatedSuccessfully("Patient");

        } catch (SQLException | IOException e) {
            Application.logger.error(e.getMessage(), e);
        }
    }

}
