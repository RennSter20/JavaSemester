package com.example.renatojava.javasemester.database;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.entity.ActiveCheckup;
import com.example.renatojava.javasemester.entity.Change;
import com.example.renatojava.javasemester.entity.PatientRoom;
import com.example.renatojava.javasemester.util.ChangeWriter;
import com.example.renatojava.javasemester.util.DateFormatter;
import com.example.renatojava.javasemester.util.Notification;
import com.example.renatojava.javasemester.util.RoomChecker;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface CheckupData {

    static void addNewActiveCheckup(Integer procedureID, Integer patientID, LocalDateTime time, PatientRoom room, String patientName, String procedure){
        try(Connection conn = Data.connectingToDatabase()){

            String roomType = new RoomChecker(room).roomType();

            String dateTimeString = time.toString();
            String fullDateTime = DateFormatter.getDateTimeFormatted(dateTimeString);

            PreparedStatement stmnt = conn.prepareStatement("INSERT INTO ACTIVE_CHECKUPS(PROCEDURE_ID, PATIENT_ID, DATE, ROOM_TYPE, PATIENT_NAME, PROCEDURE) VALUES (" + procedureID + ", " + patientID + ",parsedatetime('" + fullDateTime + "', 'dd-MM-yyyy HH:mm'), '" + roomType + "', '" + patientName + "', '" + procedure + "');");
            stmnt.executeUpdate();

            Change change = new Change(new ActiveCheckup(0, LocalDateTime.now(), 0, 0, new PatientRoom("-"), PatientData.getPatientWithID(patientID).getFullName(), "-"),
                    new ActiveCheckup(null, time, patientID, procedureID, room, patientName, procedure));
            ChangeWriter changeWriter = new ChangeWriter(change);
            changeWriter.addChange(Application.getLoggedUser().getRole());

            Notification.addedSuccessfully("Checkup");

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
        } catch (SQLException |IOException e) {
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
        String patientName = set.getString("PATIENT_NAME");
        String procedure = set.getString("PROCEDURE");

        return new ActiveCheckup(id,date, patientID, procedureID, new PatientRoom(roomType), patientName, procedure);
    }
    static void removeActiveCheckup(Integer id){
        try(Connection veza = Data.connectingToDatabase()){

            ActiveCheckup oldCheckup = getCheckupFromId(id);

            PreparedStatement stmnt = veza.prepareStatement("DELETE FROM ACTIVE_CHECKUPS WHERE ID=" + id);
            stmnt.executeUpdate();

            Change change = new Change(oldCheckup, new ActiveCheckup(0, LocalDateTime.now(), 0, 0, new PatientRoom("-"), "-", "-"));
            ChangeWriter changeWriter = new ChangeWriter(change);
            changeWriter.addChange(Application.getLoggedUser().getRole());

            Notification.removedSuccessfully("Checkup");

        } catch (SQLException | IOException e) {
            Application.logger.info(e.getMessage(), e);
        }
    }
    static void removeAllActiveCheckupsFromPatient(Integer patientID){
        try(Connection veza = Data.connectingToDatabase()){

            PreparedStatement stmnt = veza.prepareStatement("DELETE FROM ACTIVE_CHECKUPS WHERE PATIENT_ID=" + patientID);
            stmnt.executeUpdate();

        } catch (SQLException | IOException e) {
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

            Change change = new Change(checkup, getCheckupFromId(id));
            ChangeWriter changeWriter = new ChangeWriter(change);
            changeWriter.addChange(Application.getLoggedUser().getRole());

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
