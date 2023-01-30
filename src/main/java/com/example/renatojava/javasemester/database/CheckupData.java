package com.example.renatojava.javasemester.database;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.entity.ActiveCheckup;
import com.example.renatojava.javasemester.entity.PatientRoom;
import com.example.renatojava.javasemester.util.DateFormatter;
import com.example.renatojava.javasemester.util.Notification;
import com.example.renatojava.javasemester.util.RoomChecker;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface CheckupData {

    static void addNewActiveCheckup(Integer procedureID, Integer patientID, LocalDateTime time, PatientRoom room){
        try{
            Connection conn = Data.connectingToDatabase();

            String roomType = new RoomChecker(room).roomType();

            String dateTimeString = time.toString();
            String fullDateTime = DateFormatter.getDateTimeFormatted(dateTimeString);

            PreparedStatement stmnt = conn.prepareStatement("INSERT INTO ACTIVE_CHECKUPS(PROCEDURE_ID, PATIENT_ID, DATE, ROOM_TYPE) VALUES (" + procedureID + ", " + patientID + ",parsedatetime('" + fullDateTime + "', 'dd-MM-yyyy HH:mm'), '" + roomType + "');");
            stmnt.executeUpdate();

            Notification.addedSuccessfully("Checkup");

            conn.close();

        } catch (IOException | SQLException e) {
            Application.logger.info(e.getMessage(), e);
        }
    }
    static List<ActiveCheckup> getAllActiveCheckups(){
        List<ActiveCheckup> activeCheckupList = new ArrayList<>();

        try(Connection conn = Data.connectingToDatabase()) {
            Statement sqlStatement = conn.createStatement();
            ResultSet activeCheckupsResults = sqlStatement.executeQuery(
                    "SELECT * FROM ACTIVE_CHECKUPS"
            );

            while(activeCheckupsResults.next()){
                ActiveCheckup checkup = getCheckup(activeCheckupsResults);
                activeCheckupList.add(checkup);
            }
        } catch (SQLException e) {
            Application.logger.info(e.getMessage(), e);
        } catch (IOException e) {
            Application.logger.info(e.getMessage(), e);
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
            Connection veza = Data.connectingToDatabase();

            PreparedStatement stmnt = veza.prepareStatement("DELETE FROM ACTIVE_CHECKUPS WHERE ID=" + id);
            stmnt.executeUpdate();

            Notification.removedSuccessfully("Checkup");

            veza.close();
        } catch (SQLException e) {
            Application.logger.info(e.getMessage(), e);
        } catch (IOException e) {
            Application.logger.info(e.getMessage(), e);
        }
    }
    static void removeAllActiveCheckupsFromPatient(Integer patientID){
        try{
            Connection veza = Data.connectingToDatabase();

            PreparedStatement stmnt = veza.prepareStatement("DELETE FROM ACTIVE_CHECKUPS WHERE PATIENT_ID=" + patientID);
            stmnt.executeUpdate();

            veza.close();
        } catch (SQLException e) {
            Application.logger.info(e.getMessage(), e);
        } catch (IOException e) {
            Application.logger.info(e.getMessage(), e);
        }
    }
    static void updateActiveCheckup(Integer id, LocalDateTime dateTimeValue, PatientRoom selectedItem) {

        try(Connection conn = Data.connectingToDatabase()){

            ActiveCheckup checkup = getCheckupFromId(id);

            PreparedStatement stmnt = conn.prepareStatement("DELETE FROM ACTIVE_CHECKUPS WHERE ID=" + id);
            stmnt.executeUpdate();

            stmnt = conn.prepareStatement("INSERT INTO ACTIVE_CHECKUPS(PROCEDURE_ID, PATIENT_ID, DATE, ROOM_TYPE) VALUES (" + checkup.getProcedureID() + ", " + checkup.getPatientID() + ",parsedatetime('" + DateFormatter.getDateTimeFormatted(dateTimeValue.toString()) + "', 'dd-MM-yyyy HH:mm'), '" + selectedItem.getRoomType() + "');");
            stmnt.executeUpdate();

            Notification.updatedSuccessfully("Checkup");
        }catch (SQLException | IOException e){
            Application.logger.error(e.getMessage(), e);
        }

    }

    static ActiveCheckup getCheckupFromId(Integer id){
        ActiveCheckup checkup = null;

        try(Connection conn = Data.connectingToDatabase()) {


            Statement sqlStatement = conn.createStatement();
            ResultSet checkupResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM ACTIVE_CHECKUPS WHERE ID=" + id
            );

            while(checkupResultSet.next()){
                checkup = getCheckup(checkupResultSet);
            }

        } catch (SQLException | IOException e) {
            Application.logger.error(e.getMessage(), e);
        }
        return checkup;
    }
}
