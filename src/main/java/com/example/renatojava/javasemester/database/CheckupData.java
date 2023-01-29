package com.example.renatojava.javasemester.database;

import com.example.renatojava.javasemester.entity.ActiveCheckup;
import com.example.renatojava.javasemester.entity.PatientRoom;
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
            String fullDateTime = dateTimeString.substring(8,10) + "-" + dateTimeString.substring(5,7) + "-" + dateTimeString.substring(0,4) + " " + dateTimeString.substring(11,13) + ":" + dateTimeString.substring(14,16);

            PreparedStatement stmnt = conn.prepareStatement("INSERT INTO ACTIVE_CHECKUPS(PROCEDURE_ID, PATIENT_ID, DATE, ROOM_TYPE) VALUES (" + procedureID + ", " + patientID + ",parsedatetime('" + fullDateTime + "', 'dd-MM-yyyy HH:mm'), '" + roomType + "');");
            stmnt.executeUpdate();

            Notification.addedSuccessfully("Checkup");

            conn.close();

        } catch (IOException | SQLException e) {
            System.out.println(e);
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
            Connection veza = Data.connectingToDatabase();

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
            Connection veza = Data.connectingToDatabase();

            PreparedStatement stmnt = veza.prepareStatement("DELETE FROM ACTIVE_CHECKUPS WHERE PATIENT_ID=" + patientID);
            stmnt.executeUpdate();

            veza.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
