package com.example.renatojava.javasemester.entity;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.exceptions.NoProceduresException;
import com.example.renatojava.javasemester.exceptions.ObjectExistsException;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public interface Data {

    String DATABASE_FILE = "database.properties";

    static Connection connectingToDatabase() throws IOException, SQLException {
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
        LocalDate date = procedureSet.getTimestamp("date").toLocalDateTime().toLocalDate();


        Patient patientToAdd = new Patient.Builder().withName(name).withSurname(surname).withGender(gender).withOIB(oib).withDebt(debt).withProcedures(procedures).withDate(date).build();

        return patientToAdd;

    }
    static void addPatient(String name, String surname, String gender, String oib, LocalDate date) throws SQLException, IOException {
        Connection veza = connectingToDatabase();

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

        PreparedStatement stmnt = veza.prepareStatement("INSERT INTO PATIENTS(NAME, SURNAME, GENDER, DEBT, PROCEDURES, OIB, DATE) VALUES(?,?,?,?,?,?,?)");
        stmnt.setString(1, name);
        stmnt.setString(2, surname);
        stmnt.setString(3, gender);
        stmnt.setString(4, "0");
        stmnt.setString(5, "");
        stmnt.setString(6, oib);
        stmnt.setDate(7, Date.valueOf(date));

        stmnt.executeUpdate();

        Stats currentStats = getCurrentStats();
        Integer currPatients = currentStats.patients();
        List<String> changesSQL = new ArrayList<>();
        changesSQL.add("PATIENTS=" + (++currPatients));
        StatsChanger.changeStats(changesSQL);

        ChangeWriter changeWriter = new ChangeWriter(new Patient("-", "-", "-", "-", 0, "-", "-", null), new Patient(null, name, surname, gender, 0.0, "", oib, date));
        changeWriter.addChange();
        addedSuccessfully("Patient");

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

        } catch (SQLException | IOException e) {
            Application.logger.info(String.valueOf(e.getStackTrace()));
        }
        return newPatient;
    }
    static void removePatient(String oib) throws SQLException, IOException {

        Connection veza = connectingToDatabase();
        Patient patientToRemove = getCertainPatient(oib);

        PreparedStatement stmnt = veza.prepareStatement("DELETE FROM PATIENTS WHERE OIB='" + oib + "'");
        stmnt.executeUpdate();

        Stats currentStats = getCurrentStats();
        Double currentDebt = currentStats.debt();
        Integer newPatientCount = currentStats.patients() - 1;
        List<String> changesSQL = new ArrayList<>();
        changesSQL.add("PATIENTS=" + (newPatientCount));
        changesSQL.add("DEBT=" + (currentDebt - patientToRemove.getDebt()) + "");
        StatsChanger.changeStats(changesSQL);

        ChangeWriter changeWriter = new ChangeWriter(new Patient(patientToRemove.getId(), patientToRemove.getName(), patientToRemove.getSurname(), patientToRemove.getGender(), patientToRemove.getDebt(), patientToRemove.getProcedures(), oib, patientToRemove.getDate()), new Patient("-", "-", "-", "-", 0, "-", "-", null));
        changeWriter.addChange();

        veza.close();

    }
    static void updatePatient(String newName, String newSurname,String newOib,Patient oldPatient){
        try(Connection conn = connectingToDatabase()) {

            //PreparedStatement stmnt = conn.prepareStatement("UPDATE PATIENTS SET NAME='" + newName + "', SURNAME='" + newSurname + "', OIB='" + newOib + "' WHERE DEBT=" + Integer.valueOf((int) oldPatient.getDebt()));
            PreparedStatement stmnt = conn.prepareStatement("UPDATE PATIENTS SET NAME='" + newName + "', SURNAME='" + newSurname + "' WHERE OIB='" + oldPatient.getOib() + "'");
            stmnt.executeUpdate();
            stmnt = conn.prepareStatement("UPDATE PATIENTS SET OIB='" + newOib + "' WHERE NAME='" + newName + "' AND SURNAME='" + newSurname + "' AND GENDER='" + oldPatient.getGender() + "'");
            stmnt.executeUpdate();

            ChangeWriter changeWriter = new ChangeWriter(oldPatient, Data.getCertainPatient(newOib));
            changeWriter.addChange();

        } catch (SQLException | IOException e) {
            System.out.println(e);
        }
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
        Double price = procedureSet.getDouble("price");


        return new Procedure(description, price);

    }
    static Procedure getProcedureFromDescription(String description){
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

        if(patient != null){
            try (Connection conn = connectingToDatabase()){

                Statement sqlStatement = conn.createStatement();
                ResultSet proceduresResultSet = sqlStatement.executeQuery(
                        "SELECT * FROM PATIENTS WHERE OIB='" + patient.getOib() + "'"
                );

                while(proceduresResultSet.next()){
                    procedureList += proceduresResultSet.getString("procedures");
                }

            } catch (SQLException | IOException e) {
                Application.logger.info(String.valueOf(e.getStackTrace()));
            }
        }

        return procedureList;
    }
    static void addProcedureToPatient(String oib, String procedure){

        Patient patientToUpdate = getCertainPatient(oib);
        String procedures = patientToUpdate.getProcedures();

        if(procedures.equals("")){
            procedures = procedure + ",";
        }else{
            procedures = procedures + procedure + ",";
        }

        try(Connection conn = connectingToDatabase()) {

            Patient oldPatient = getCertainPatient(oib);

            PreparedStatement updateProcedures = conn.prepareStatement("UPDATE PATIENTS SET PROCEDURES='" + procedures + "'" + "WHERE OIB='" + oib + "'");
            updateProcedures.executeUpdate();

            List<Procedure> allProcedures = getAllProcedures();
            Procedure procedureToFind = allProcedures.stream().filter(procedure1 -> procedure1.description().equals(procedure)).findAny().orElse(null);

            Double debtToAdd = procedureToFind.price();
            Double oldDebt = patientToUpdate.getDebt();
            Double newDebt = oldDebt + debtToAdd;

            PreparedStatement updateDebt = conn.prepareStatement("UPDATE PATIENTS SET DEBT=" + newDebt + "WHERE OIB='" + patientToUpdate.getOib() + "'");
            updateDebt.executeUpdate();

            Stats currentStats = getCurrentStats();
            List<String> changesSQL = new ArrayList<>();
            changesSQL.add("DEBT=" + (currentStats.debt() + debtToAdd) + "");
            StatsChanger.changeStats(changesSQL);

            Patient newPatient = getCertainPatient(oib);
            ChangeWriter changeWriter = new ChangeWriter(oldPatient,newPatient);
            changeWriter.addChange();


        } catch (SQLException | IOException e) {
            Application.logger.info(String.valueOf(e.getStackTrace()));
        }catch (NoProceduresException e){
            Application.logger.info(e.getMessage(), e.getStackTrace());
        }
    }
    static void removeProcedure(String procedureDescription, String oib, String currentProcedures){
        Patient patient = getCertainPatient(oib);
        Procedure procedure = getProcedureFromDescription(procedureDescription);

        List<String> currentProceduresSplitted = List.of(currentProcedures.split(","));;

        try (Connection conn = connectingToDatabase()){

            Boolean removed = false;
            String newProcedureString = "";
            for(String proc : currentProceduresSplitted){
                if(proc.equals(procedureDescription) && !removed){
                    removed = true;
                }else{
                    newProcedureString = newProcedureString + proc + ",";
                }
            }

            PreparedStatement updateProcedures = conn.prepareStatement("UPDATE PATIENTS SET PROCEDURES='" + newProcedureString + "'" + "WHERE OIB='" + oib + "'");
            updateProcedures.executeUpdate();

            Double newDebt = patient.getDebt() - procedure.price();
            PreparedStatement updateDebt = conn.prepareStatement("UPDATE PATIENTS SET DEBT=" + newDebt + "WHERE OIB='" + patient.getOib() + "'");
            updateDebt.executeUpdate();

            Stats currentStats = getCurrentStats();
            List<String> changesSQL = new ArrayList<>();
            changesSQL.add("DEBT=" + (currentStats.debt() - procedure.price()) + "");
            StatsChanger.changeStats(changesSQL);

            Patient newPatient = getCertainPatient(oib);
            ChangeWriter changeWriter = new ChangeWriter(patient,newPatient);
            changeWriter.addChange();


        } catch (SQLException | IOException e) {
            Application.logger.info(String.valueOf(e.getStackTrace()));
        }

    }


    static Set<Doctor> getAllDoctors() throws SQLException, IOException {
        Set<Doctor> doctorList = new HashSet<>();

        try(Connection conn = connectingToDatabase()) {
            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM DOCTORS"
            );

            while(proceduresResultSet.next()){
                Doctor newDoctor = getDoctor(proceduresResultSet);
                doctorList.add(newDoctor);
            }
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


    static Stats getCurrentStats(){
        try(Connection conn = connectingToDatabase()) {
            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM STATS WHERE ID=0"
            );
            Stats statsToReturn = null;
            while(proceduresResultSet.next()){

                statsToReturn = new Stats(proceduresResultSet.getInt("id"), proceduresResultSet.getInt("patients"), proceduresResultSet.getDouble("debt"), proceduresResultSet.getInt("doctors"), proceduresResultSet.getInt("bills"));

            }

            conn.close();
            return statsToReturn;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static User getUserFromResult(ResultSet procedureSet) throws SQLException{

        String id = procedureSet.getString("ID");
        String password = procedureSet.getString("PASSWORD");
        String name = procedureSet.getString("NAME");
        String surname = procedureSet.getString("SURNAME");
        String role = procedureSet.getString("ROLE");
        String oib = procedureSet.getString("OIB");


        return new User(id, password, name, surname, role, oib);

    }
}
