package com.example.renatojava.javasemester.database;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.entity.ChangeWriter;
import com.example.renatojava.javasemester.entity.Patient;
import com.example.renatojava.javasemester.entity.Procedure;
import com.example.renatojava.javasemester.entity.Stats;
import com.example.renatojava.javasemester.exceptions.NoProceduresException;
import com.example.renatojava.javasemester.util.Notification;
import com.example.renatojava.javasemester.util.StatsChanger;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public interface ProcedureData {

    static List<Procedure> getAllProcedures() throws SQLException, IOException, NoProceduresException {
        List<Procedure> procedureList = new ArrayList<>();

        try (Connection conn = Data.connectingToDatabase()){

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

        Integer id = procedureSet.getInt("id");
        String description = procedureSet.getString("description");
        Double price = procedureSet.getDouble("price");


        return new Procedure(id, description, price);

    }
    static Procedure getProcedureFromDescription(String description){
        Procedure procedure = null;
        try{
            List<Procedure> allProcedures = getAllProcedures();
            procedure = allProcedures.stream().filter(procedure1 -> procedure1.description().equals(description)).findAny().orElse(null);

        }catch (SQLException | IOException | NoProceduresException e) {
            Application.logger.error(e.getMessage(), e);
        }
        return procedure;
    }
    static Procedure getProcedureFromId(Integer id){
        Procedure procedure = null;
        try{
            List<Procedure> allProcedures = getAllProcedures();
            procedure = allProcedures.stream().filter(procedure1 -> procedure1.id().equals(id)).findAny().orElse(null);

        }catch (SQLException | IOException | NoProceduresException e) {
            Application.logger.error(e.getMessage(), e);
        }
        return procedure;
    }
    static String getAllProceduresFromPatientString(Patient patient){
        String procedureList = "";

        if(patient != null){
            try (Connection conn = Data.connectingToDatabase()){

                Statement sqlStatement = conn.createStatement();
                ResultSet proceduresResultSet = sqlStatement.executeQuery(
                        "SELECT * FROM PATIENTS WHERE OIB='" + patient.getOib() + "'"
                );

                while(proceduresResultSet.next()){
                    procedureList += proceduresResultSet.getString("procedures");
                }

            } catch (SQLException | IOException e) {
                Application.logger.error(e.getMessage(), e);
            }
        }

        return procedureList;
    }
    static void addProcedureToPatient(Integer id, String procedure){

        Patient patientToUpdate = PatientData.getPatientWithID(id);
        String procedures = patientToUpdate.getProcedures();

        if(procedures.equals("")){
            procedures = procedure;
        }else{
            procedures = procedures + "," + procedure;
        }

        try(Connection conn = Data.connectingToDatabase()) {

            Patient oldPatient = PatientData.getPatientWithID(id);

            PreparedStatement updateProcedures = conn.prepareStatement("UPDATE PATIENTS SET PROCEDURES='" + procedures + "'" + "WHERE ID=" + id);
            updateProcedures.executeUpdate();

            Procedure procedureToAdd = getAllProcedures().stream().filter(procedure1 -> procedure1.description().equals(procedure)).findAny().orElse(null);

            Double newDebt = patientToUpdate.getDebt() + procedureToAdd.price();
            PreparedStatement updateDebt = conn.prepareStatement("UPDATE PATIENTS SET DEBT=" + newDebt + "WHERE ID=" + patientToUpdate.getId());
            updateDebt.executeUpdate();

            Stats currentStats = StatsData.getCurrentStats();
            List<String> changesSQL = new ArrayList<>();
            changesSQL.add("DEBT=" + (currentStats.debt() + procedureToAdd.price()) + "");
            StatsChanger.changeStats(changesSQL);

            Patient newPatient = PatientData.getPatientWithID(id);
            ChangeWriter changeWriter = new ChangeWriter(oldPatient,newPatient);
            changeWriter.addChange(Application.getLoggedUser().getRole());


        } catch (SQLException | IOException |NoProceduresException e) {
            Application.logger.error(e.getMessage(), e);
        }
    }
    static void removeProcedure(String procedureDescription, Integer id, String currentProcedures){
        Patient patient = PatientData.getPatientWithID(id);
        Procedure procedure = getProcedureFromDescription(procedureDescription);

        List<String> currentProceduresSplitted = List.of(currentProcedures.split(","));;

        try (Connection conn = Data.connectingToDatabase()){

            Boolean removed = false;
            String newProcedureString = "";
            for(String proc : currentProceduresSplitted){
                if(proc.equals(procedureDescription) && !removed){
                    removed = true;
                }else{
                    newProcedureString = newProcedureString + proc + ",";
                }
            }

            PreparedStatement updateProcedures = conn.prepareStatement("UPDATE PATIENTS SET PROCEDURES='" + newProcedureString + "'" + "WHERE ID=" + id);
            updateProcedures.executeUpdate();

            PreparedStatement updateDebt = conn.prepareStatement("UPDATE PATIENTS SET DEBT=" + (patient.getDebt() - procedure.price()) + "WHERE ID=" + id);
            updateDebt.executeUpdate();

            Stats currentStats = StatsData.getCurrentStats();
            List<String> changesSQL = new ArrayList<>();
            changesSQL.add("DEBT=" + (currentStats.debt() - procedure.price()));
            StatsChanger.changeStats(changesSQL);

            Patient newPatient = PatientData.getPatientWithID(id);
            ChangeWriter changeWriter = new ChangeWriter(patient,newPatient);
            changeWriter.addChange(Application.getLoggedUser().getRole());


        } catch (SQLException | IOException e) {
            Application.logger.info(String.valueOf(e.getStackTrace()));
        }

    }


    static void createProcedure(String description, String price){
        try{
            Connection conn = Data.connectingToDatabase();

            PreparedStatement stmnt = conn.prepareStatement("INSERT INTO PROCEDURES(DESCRIPTION, PRICE) VALUES('" + description + "', " + price + ")");
            stmnt.executeUpdate();

            Notification.addedSuccessfully("Procedure");

            conn.close();

        } catch (IOException | SQLException e) {
            Application.logger.info(e.getMessage(), e.getStackTrace());
        }
    }

    static void deleteProcedure(String desc){
        try(Connection conn = Data.connectingToDatabase()){
            PreparedStatement stmnt = conn.prepareStatement("DELETE FROM PROCEDURES WHERE DESCRIPTION='" + desc + "'");
            stmnt.executeUpdate();

            Notification.removedSuccessfully("Procedure");
        }catch (IOException | SQLException e){

        }
    }

    static void updateProcedure(Procedure procedure){
        try(Connection conn = Data.connectingToDatabase()) {

            PreparedStatement stmnt = conn.prepareStatement("UPDATE PROCEDURES SET DESCRIPTION='" + procedure.description() + "', PRICE=" + procedure.price() + " WHERE ID=" + procedure.id());
            stmnt.executeUpdate();

            Notification.updatedSuccessfully("Procedure");

        } catch (SQLException | IOException e) {
            Application.logger.error(e.getMessage(), e);
        }
    }

}
