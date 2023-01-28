package com.example.renatojava.javasemester.database;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.entity.*;
import com.example.renatojava.javasemester.exceptions.NoProceduresException;
import com.example.renatojava.javasemester.exceptions.ObjectExistsException;
import com.example.renatojava.javasemester.util.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public interface Data {

    String DATABASE_FILE = "database.properties";

    static Connection connectingToDatabase() throws IOException, SQLException {
        Connection conn;
        try{
            Properties properties = new Properties();
            properties.load(new FileReader(DATABASE_FILE));
            String url = properties.getProperty("url");
            String user = properties.getProperty("user");
            String pass = properties.getProperty("pass");
            conn = DriverManager.getConnection(url, user,pass);
        }catch (IOException e){
            throw new IOException("Error while reading properties file for DB.", e);
        }catch (SQLException e){
            throw new SQLException("Error while connecting to database.", e);
        }
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
        Connection veza = connectingToDatabase();

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

        Stats currentStats = getCurrentStats();
        Integer currPatients = currentStats.patients();
        List<String> changesSQL = new ArrayList<>();
        changesSQL.add("PATIENTS=" + (++currPatients));
        StatsChanger.changeStats(changesSQL);

        ChangeWriter changeWriter = new ChangeWriter(new Patient(-1, "-", "-", "-", 0, "-", "-", null), new Patient(Data.getPatientWithOib(oib).getId(), name, surname, gender, 0.0, "", oib, date));
        changeWriter.addChange(Application.getLoggedUser().getRole());
        addedSuccessfully("Patient");

        veza.close();
    }
    static Patient getPatientWithOib(String oib){
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
            Application.logger.error(e.getMessage(), e);
        }
        return newPatient;
    }

    static Patient getPatientWithID(Integer id){
        Patient newPatient = null;
        try(Connection conn = connectingToDatabase()) {


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

        Connection veza = connectingToDatabase();
        Patient patientToRemove = getPatientWithID(id);

        PreparedStatement stmnt = veza.prepareStatement("DELETE FROM PATIENTS WHERE ID=" + id);
        stmnt.executeUpdate();

        Stats currentStats = getCurrentStats();
        Double currentDebt = currentStats.debt();
        Integer newPatientCount = currentStats.patients() - 1;
        List<String> changesSQL = new ArrayList<>();
        changesSQL.add("PATIENTS=" + (newPatientCount));
        changesSQL.add("DEBT=" + (currentDebt - patientToRemove.getDebt()) + "");
        StatsChanger.changeStats(changesSQL);

        removeAllActiveCheckupsFromPatient(id);

        ChangeWriter changeWriter = new ChangeWriter(new Patient(patientToRemove.getId(), patientToRemove.getName(), patientToRemove.getSurname(), patientToRemove.getGender(), patientToRemove.getDebt(), patientToRemove.getProcedures(), patientToRemove.getOib(), patientToRemove.getDate()), new Patient(-1, "-", "-", "-", 0, "-", "-", null));
        changeWriter.addChange(Application.getLoggedUser().getRole());

        veza.close();

    }
    static void updatePatient(String newName, String newSurname,String newOib,Patient oldPatient){

        try(Connection conn = connectingToDatabase()) {

            PreparedStatement stmnt = conn.prepareStatement("UPDATE PATIENTS SET NAME='" + newName + "', SURNAME='" + newSurname + "' WHERE OIB='" + oldPatient.getOib() + "'");
            stmnt.executeUpdate();
            stmnt = conn.prepareStatement("UPDATE PATIENTS SET OIB='" + newOib + "' WHERE NAME='" + newName + "' AND SURNAME='" + newSurname + "' AND GENDER='" + oldPatient.getGender() + "'");
            stmnt.executeUpdate();

            ChangeWriter changeWriter = new ChangeWriter(oldPatient, Data.getPatientWithOib(newOib));
            changeWriter.addChange(Application.getLoggedUser().getRole());

            updatedSuccessfully("Patient");

        } catch (SQLException | IOException e) {
            Application.logger.error(e.getMessage(), e);
        }
    }


    static List<Procedure> getAllProcedures() throws SQLException, IOException, NoProceduresException {
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
            try (Connection conn = connectingToDatabase()){

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

        Patient patientToUpdate = getPatientWithID(id);
        String procedures = patientToUpdate.getProcedures();

        if(procedures.equals("")){
            procedures = procedure;
        }else{
            procedures = procedures + "," + procedure;
        }

        try(Connection conn = connectingToDatabase()) {

            Patient oldPatient = getPatientWithID(id);

            PreparedStatement updateProcedures = conn.prepareStatement("UPDATE PATIENTS SET PROCEDURES='" + procedures + "'" + "WHERE ID=" + id);
            updateProcedures.executeUpdate();

            Procedure procedureToAdd = getAllProcedures().stream().filter(procedure1 -> procedure1.description().equals(procedure)).findAny().orElse(null);

            Double newDebt = patientToUpdate.getDebt() + procedureToAdd.price();
            PreparedStatement updateDebt = conn.prepareStatement("UPDATE PATIENTS SET DEBT=" + newDebt + "WHERE ID=" + patientToUpdate.getId());
            updateDebt.executeUpdate();

            Stats currentStats = getCurrentStats();
            List<String> changesSQL = new ArrayList<>();
            changesSQL.add("DEBT=" + (currentStats.debt() + procedureToAdd.price()) + "");
            StatsChanger.changeStats(changesSQL);

            Patient newPatient = getPatientWithID(id);
            ChangeWriter changeWriter = new ChangeWriter(oldPatient,newPatient);
            changeWriter.addChange(Application.getLoggedUser().getRole());


        } catch (SQLException | IOException |NoProceduresException e) {
            Application.logger.error(e.getMessage(), e);
        }
    }
    static void removeProcedure(String procedureDescription, Integer id, String currentProcedures){
        Patient patient = getPatientWithID(id);
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

            PreparedStatement updateProcedures = conn.prepareStatement("UPDATE PATIENTS SET PROCEDURES='" + newProcedureString + "'" + "WHERE ID=" + id);
            updateProcedures.executeUpdate();

            PreparedStatement updateDebt = conn.prepareStatement("UPDATE PATIENTS SET DEBT=" + (patient.getDebt() - procedure.price()) + "WHERE ID=" + id);
            updateDebt.executeUpdate();

            Stats currentStats = getCurrentStats();
            List<String> changesSQL = new ArrayList<>();
            changesSQL.add("DEBT=" + (currentStats.debt() - procedure.price()));
            StatsChanger.changeStats(changesSQL);

            Patient newPatient = getPatientWithID(id);
            ChangeWriter changeWriter = new ChangeWriter(patient,newPatient);
            changeWriter.addChange(Application.getLoggedUser().getRole());


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

        Integer id = procedureSet.getInt("id");
        String gender = procedureSet.getString("gender");
        String name = procedureSet.getString("name");
        String surname = procedureSet.getString("surname");
        String room = procedureSet.getString("room");
        String title = procedureSet.getString("title");


        return new Doctor.Builder().withName(name).withSurname(surname).withGender(gender).withRoom(room).withTitle(title).withId(id).build();

    }
    static void addDoctor(Doctor doctor){
        try(Connection conn = Data.connectingToDatabase()){
            CheckObjects.checkIfDoctorExists(doctor);

            PreparedStatement stmnt = conn.prepareStatement("INSERT INTO DOCTORS(NAME, SURNAME, GENDER, TITLE, ROOM) VALUES(?,?,?,?,?)");
            stmnt.setString(1, doctor.getName());
            stmnt.setString(2, doctor.getSurname());
            stmnt.setString(3, doctor.getGender());
            stmnt.setString(4, doctor.getTitle());
            stmnt.setString(5, doctor.getRoom());
            stmnt.executeUpdate();

            Stats currentStats = getCurrentStats();
            Integer currDoctors = currentStats.doctors();
            List<String> changesSQL = new ArrayList<>();
            changesSQL.add("DOCTORS=" + (++currDoctors));
            StatsChanger.changeStats(changesSQL);

            ChangeWriter changeWriter = new ChangeWriter(new Doctor.Builder().withName("-").withSurname("-").withGender("-").withRoom("-").withTitle("-").build(),doctor);
            changeWriter.addChange(Application.getLoggedUser().getRole());

            addedSuccessfully("Doctor");


        } catch (IOException | SQLException e) {

            Application.logger.error(e.getMessage(), e);

        }catch (ObjectExistsException e){

            Application.logger.error(e.getMessage(), e);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText(e.getMessage());
            alert.show();
        }
    }
    static void removeDoctor(Doctor doctor) throws SQLException, IOException {
        Connection veza = connectingToDatabase();

        PreparedStatement stmnt = veza.prepareStatement("DELETE FROM DOCTORS WHERE ROOM='" + doctor.getRoom() + "'");
        stmnt.executeUpdate();

        Stats currentStats = getCurrentStats();
        Integer oldCountDoctors = currentStats.doctors();
        Integer newCountDoctors = oldCountDoctors - 1;
        List<String> changesSQL = new ArrayList<>();
        changesSQL.add("DOCTORS=" + (newCountDoctors));
        StatsChanger.changeStats(changesSQL);

        ChangeWriter changeWriter = new ChangeWriter(doctor, new Doctor.Builder().withName("-").withSurname("-").withGender("-").withRoom("-").withTitle("-").build());
        changeWriter.addChange(Application.getLoggedUser().getRole());

        removedSuccessfully("Doctor");

        veza.close();
    }
    static void updateDoctor(Integer id, String newName, String newSurname,String newTitle, String newGender, Doctor oldDoctor){
        try(Connection conn = connectingToDatabase()) {

            PreparedStatement stmnt = conn.prepareStatement("UPDATE DOCTORS SET NAME='" + newName + "', SURNAME='" + newSurname + "', TITLE='" + newTitle + "', GENDER='" + newGender + "' WHERE ID=" + id);
            stmnt.executeUpdate();

            ChangeWriter changeWriter = new ChangeWriter(oldDoctor, Data.getCertainDoctor(id));
            changeWriter.addChange(Application.getLoggedUser().getRole());

            updatedSuccessfully("Doctor");

        } catch (SQLException | IOException e) {
            Application.logger.error(e.getMessage(), e);
        }
    }
    static Doctor getCertainDoctor(Integer id){

        Doctor newDoctor = new Doctor.Builder().withName("-1").withSurname("").build();

        try(Connection conn = connectingToDatabase()) {


            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM DOCTORS WHERE ID=" + id
            );

            while(proceduresResultSet.next()){
                newDoctor = getDoctor(proceduresResultSet);
            }

        } catch (SQLException | IOException e) {
            Application.logger.error(e.getMessage(), e);
        }
        return newDoctor;
    }

    static Boolean confirmEdit(){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRMATION");
        alert.setHeaderText("Are you sure you want to make this change in system?");
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
    static void removedSuccessfully(String type){
        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setTitle("INFORMATION");
        success.setHeaderText("Success!");
        success.setContentText(type + " successfully removed from the system!");
        success.show();
    }
    static void updatedSuccessfully(String type){
        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setTitle("INFORMATION");
        success.setHeaderText("Success!");
        success.setContentText(type + " successfully updated in system!");
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


    static List<DoctorRoom> getAllRooms(){
        List<DoctorRoom> doctorRoomList = new ArrayList<>();

        try(Connection conn = connectingToDatabase()) {
            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM HOSPITAL"
            );

            while(proceduresResultSet.next()){
                DoctorRoom newDoctorRoom = getRoom(proceduresResultSet);
                doctorRoomList.add(newDoctorRoom);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return doctorRoomList;
    }
    static void addRoom(String roomName, Integer doctorID){

        try{
            CheckObjects.checkIfRoomExists(roomName);

            Connection conn = Data.connectingToDatabase();

            PreparedStatement stmnt = conn.prepareStatement("INSERT INTO HOSPITAL(ROOM, DOCTORID) VALUES(?,?)");
            stmnt.setString(1, roomName);
            stmnt.setInt(2, doctorID);
            stmnt.executeUpdate();

            ChangeWriter changeWriter = new ChangeWriter(new DoctorRoom("-", -1, -1), new DoctorRoom(roomName, doctorID, getRoomWithName(roomName).getRoomID()));
            changeWriter.addChange(Application.getLoggedUser().getRole());

            linkDoctorWithRoom(getCertainDoctor(doctorID), doctorID, roomName);

            addedSuccessfully("Room");

            conn.close();

        } catch (IOException | SQLException e) {
            Application.logger.info(e.getMessage(), e.getStackTrace());
        }catch (ObjectExistsException e){
            Application.logger.info(e.getMessage(), e.getStackTrace());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText(e.getMessage());
            alert.show();
        }
    }
    static void removeRoom(Integer id){
        try{
            Connection veza = connectingToDatabase();

            DoctorRoom oldDoctorRoom = getRoomWithId(id);

            PreparedStatement stmnt = veza.prepareStatement("DELETE FROM HOSPITAL WHERE ID=" + id);
            stmnt.executeUpdate();

            ChangeWriter changeWriter = new ChangeWriter( new DoctorRoom(oldDoctorRoom.getRoomName(), oldDoctorRoom.getDoctorID(), oldDoctorRoom.getRoomID()), new DoctorRoom("-", -1, -1));
            changeWriter.addChange(Application.getLoggedUser().getRole());

            removedSuccessfully("Room");


            veza.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static DoctorRoom getRoomWithId(Integer id){
        DoctorRoom certainDoctorRoom = null;
        try(Connection conn = connectingToDatabase()) {

            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM HOSPITAL WHERE ID=" + id
            );
            while(proceduresResultSet.next()){
                certainDoctorRoom = getRoom(proceduresResultSet);
            }

        } catch (SQLException | IOException e) {
            Application.logger.info(String.valueOf(e.getStackTrace()));
        }
        return certainDoctorRoom;
    }
    static DoctorRoom getRoomWithName(String name){
        DoctorRoom certainDoctorRoom = null;
        try(Connection conn = connectingToDatabase()) {

            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM HOSPITAL WHERE ROOM='" + name + "'"
            );
            while(proceduresResultSet.next()){
                certainDoctorRoom = getRoom(proceduresResultSet);
            }

        } catch (SQLException | IOException e) {
            Application.logger.info(String.valueOf(e.getStackTrace()));
        }
        return certainDoctorRoom;
    }
    static DoctorRoom getRoom(ResultSet procedureSet) throws SQLException{

        Integer id = procedureSet.getInt("id");
        String roomName = procedureSet.getString("room");
        Integer doctorID = procedureSet.getInt("doctorid");

        return new DoctorRoom(roomName, doctorID, id);

    }
    static void linkDoctorWithRoom(Doctor oldDoctor, Integer doctorID, String roomName){
        try(Connection conn = connectingToDatabase()) {
            PreparedStatement stmnt = conn.prepareStatement("UPDATE DOCTORS SET ROOM='" + roomName + "' WHERE ID=" + doctorID );
            stmnt.executeUpdate();

            ChangeWriter changeWriter = new ChangeWriter(oldDoctor, Data.getCertainDoctor(doctorID));
            changeWriter.addChange(Application.getLoggedUser().getRole());

        } catch (SQLException | IOException e) {
            System.out.println(e);
        }
    }
    static void unlinkRoomFromDoctor(DoctorRoom doctorRoomToRemove){
        try(Connection conn = connectingToDatabase()){
            Optional<Doctor> oldDoctor = getAllDoctors().stream().filter(doctor -> doctor.getRoom().equals(doctorRoomToRemove.getRoomName())).findFirst();
            if(oldDoctor.isPresent()){

                PreparedStatement stmnt = conn.prepareStatement("UPDATE DOCTORS SET ROOM='" + "Not yet set" + "' WHERE ID=" + doctorRoomToRemove.getDoctorID());
                stmnt.executeUpdate();

                ChangeWriter changeWriter = new ChangeWriter(oldDoctor.get(), Data.getCertainDoctor(doctorRoomToRemove.getDoctorID()));
                changeWriter.addChange(Application.getLoggedUser().getRole());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static void unlinkDoctorFromRoom(String roomName){
        try(Connection conn = connectingToDatabase()){
            Optional<DoctorRoom> oldRoom = getAllRooms().stream().filter(doctorRoom -> doctorRoom.getRoomName().equals(roomName)).findFirst();
            if(oldRoom.isPresent()){

                PreparedStatement stmnt = conn.prepareStatement("UPDATE HOSPITAL SET DOCTORID= -1 WHERE ROOM='" + roomName + "'");
                stmnt.executeUpdate();

                ChangeWriter changeWriter = new ChangeWriter(oldRoom.get(), getRoomWithName(roomName));
                changeWriter.addChange(Application.getLoggedUser().getRole());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static Boolean hasDoctorRoom(Integer id){
        Doctor doctorToCheck = null;
        doctorToCheck = getCertainDoctor(id);
        if(doctorToCheck.getRoom().equals("Not yet set")){
            return false;
        }else{
            return true;
        }
    }



    static void addNewActiveCheckup(Integer procedureID, Integer patientID, LocalDateTime time, PatientRoom room){
        try{
            Connection conn = Data.connectingToDatabase();

            String roomType = new RoomChecker(room).roomType();

            String dateTimeString = time.toString();
            String fullDateTime = dateTimeString.substring(8,10) + "-" + dateTimeString.substring(5,7) + "-" + dateTimeString.substring(0,4) + " " + dateTimeString.substring(11,13) + ":" + dateTimeString.substring(14,16);

            PreparedStatement stmnt = conn.prepareStatement("INSERT INTO ACTIVE_CHECKUPS(PROCEDURE_ID, PATIENT_ID, DATE, ROOM_TYPE) VALUES (" + procedureID + ", " + patientID + ",parsedatetime('" + fullDateTime + "', 'dd-MM-yyyy HH:mm'), '" + roomType + "');");
            stmnt.executeUpdate();

            addedSuccessfully("Checkup");

            conn.close();

        } catch (IOException | SQLException e) {
            System.out.println(e);
        }
    }
    static List<ActiveCheckup> getAllActiveCheckups(){
        List<ActiveCheckup> activeCheckupList = new ArrayList<>();

        try(Connection conn = connectingToDatabase()) {
            Statement sqlStatement = conn.createStatement();
            ResultSet activeCheckupsResults = sqlStatement.executeQuery(
                    "SELECT * FROM ACTIVE_CHECKUPS"
            );

            while(activeCheckupsResults.next()){
                ActiveCheckup checkup = getCheckup(activeCheckupsResults);
                activeCheckupList.add(checkup);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return activeCheckupList;
    }
    static ActiveCheckup getCheckup(ResultSet set) throws SQLException {
        Integer procedureID = set.getInt("PROCEDURE_ID");
        Integer patientID = set.getInt("PATIENT_ID");
        LocalDateTime date = set.getTimestamp("DATE").toLocalDateTime();
        String roomType = set.getString("ROOM_TYPE");
        Integer id = set.getInt("ID");

        return new ActiveCheckup(id,date, patientID, procedureID, new PatientRoom(roomType));
    }
    static void removeActiveCheckup(Integer id){
        try{
            Connection veza = connectingToDatabase();

            PreparedStatement stmnt = veza.prepareStatement("DELETE FROM ACTIVE_CHECKUPS WHERE ID=" + id);
            stmnt.executeUpdate();

            veza.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static void removeAllActiveCheckupsFromPatient(Integer patientID){
        try{
            Connection veza = connectingToDatabase();

            PreparedStatement stmnt = veza.prepareStatement("DELETE FROM ACTIVE_CHECKUPS WHERE PATIENT_ID=" + patientID);
            stmnt.executeUpdate();

            veza.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void createBill(Patient patient, LocalDateTime time){
        try{
            Connection conn = Data.connectingToDatabase();

            String billCreated = DateFormatter.getDateTimeFormatted(time.toString());
            String birthDay = DateFormatter.getDateFormatted(patient.getDate().toString());

            PreparedStatement stmnt = conn.prepareStatement("INSERT INTO BILLS(NAME, SURNAME, OIB, GENDER, DEBT, PROCEDURES, DATE, BIRTH_DATE) VALUES ('" + patient.getName() + "', '" + patient.getSurname() + "', '" + patient.getOib() + "', '" + patient.getGender() +"', " + patient.getDebt() + ", '" + patient.getProcedures() + "', parsedatetime('" + billCreated + "', 'dd-MM-yyyy HH:mm'), parsedatetime('" + birthDay + "', 'dd-MM-yyyy'))");
            stmnt.executeUpdate();

            removePatient(patient.getId());

            addedSuccessfully("Bill");

            conn.close();

        } catch (IOException | SQLException e) {
            Application.logger.info(e.getMessage(), e.getStackTrace());
        }
    }
    static List<Bill> getAllBills(){
        List<Bill> billList = new ArrayList<>();

        try(Connection conn = connectingToDatabase()) {
            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM BILLS"
            );

            while(proceduresResultSet.next()){
                Bill newBill = getBill(proceduresResultSet);
                billList.add(newBill);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return billList;
    }

    static Bill getBill(ResultSet set) throws SQLException {
        String name = set.getString("NAME");
        String surname = set.getString("SURNAME");
        String oib = set.getString("OIB");
        String gender = set.getString("GENDER");
        Double debt = set.getDouble("DEBT");
        String procedures = set.getString("PROCEDURES");
        LocalDateTime date = set.getTimestamp("DATE").toLocalDateTime();
        LocalDate birth_date = set.getTimestamp("BIRTH_DATE").toLocalDateTime().toLocalDate();

        return new Bill(new Patient(null, name, surname, gender, debt, procedures, oib, birth_date), date);
    }


    static void createProcedure(String description, String price){
        try{
            Connection conn = connectingToDatabase();

            PreparedStatement stmnt = conn.prepareStatement("INSERT INTO PROCEDURES(DESCRIPTION, PRICE) VALUES('" + description + "', " + price + ")");
            stmnt.executeUpdate();

            addedSuccessfully("Procedure");

            conn.close();

        } catch (IOException | SQLException e) {
            Application.logger.info(e.getMessage(), e.getStackTrace());
        }
    }

    static void deleteProcedure(String desc){
        try(Connection conn = connectingToDatabase()){
            PreparedStatement stmnt = conn.prepareStatement("DELETE FROM PROCEDURES WHERE DESCRIPTION='" + desc + "'");
            stmnt.executeUpdate();

            removedSuccessfully("Procedure");
        }catch (IOException | SQLException e){

        }
    }

    static void updateProcedure(Procedure procedure){
        try(Connection conn = connectingToDatabase()) {

            PreparedStatement stmnt = conn.prepareStatement("UPDATE PROCEDURES SET DESCRIPTION='" + procedure.description() + "', PRICE=" + procedure.price() + " WHERE ID=" + procedure.id());
            stmnt.executeUpdate();

            updatedSuccessfully("Procedure");

        } catch (SQLException | IOException e) {
            Application.logger.error(e.getMessage(), e);
        }
    }
}
