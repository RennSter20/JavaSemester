package com.example.renatojava.javasemester.database;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.entity.Change;
import com.example.renatojava.javasemester.util.ChangeWriter;
import com.example.renatojava.javasemester.entity.Doctor;
import com.example.renatojava.javasemester.entity.DoctorRoom;
import com.example.renatojava.javasemester.exceptions.ObjectExistsException;
import com.example.renatojava.javasemester.util.CheckObjects;
import com.example.renatojava.javasemester.util.Notification;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface DoctorRoomData {

    static List<DoctorRoom> getAllRooms(){
        List<DoctorRoom> doctorRoomList = new ArrayList<>();

        try(Connection conn = Data.connectingToDatabase()) {
            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM HOSPITAL"
            );

            while(proceduresResultSet.next()){
                DoctorRoom newDoctorRoom = getRoom(proceduresResultSet);
                doctorRoomList.add(newDoctorRoom);
            }
        } catch (SQLException e) {
            Application.logger.info(e.getMessage(), e);
        } catch (IOException e) {
            Application.logger.info(e.getMessage(), e);
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

            Change change = new Change(new DoctorRoom("-", -1, -1), new DoctorRoom(roomName, doctorID, getRoomWithName(roomName).getRoomID()));
            ChangeWriter changeWriter = new ChangeWriter(change);
            changeWriter.addChange(Application.getLoggedUser().getRole());

            linkDoctorWithRoom(DoctorData.getCertainDoctor(doctorID), doctorID, roomName);

            Notification.addedSuccessfully("Room");

            conn.close();

        } catch (IOException | SQLException e) {
            Application.logger.info(e.getMessage(), e);
        }catch (ObjectExistsException e){
            Application.logger.info(e.getMessage(), e);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText(e.getMessage());
            alert.show();
        }
    }
    static void removeRoom(Integer id){
        try{
            Connection veza = Data.connectingToDatabase();

            DoctorRoom oldDoctorRoom = getRoomWithId(id);

            PreparedStatement stmnt = veza.prepareStatement("DELETE FROM HOSPITAL WHERE ID=" + id);
            stmnt.executeUpdate();

            Change change = new Change(new DoctorRoom(oldDoctorRoom.getRoomName(), oldDoctorRoom.getDoctorID(), oldDoctorRoom.getRoomID()), new DoctorRoom("-", -1, -1));
            ChangeWriter changeWriter = new ChangeWriter(change);
            changeWriter.addChange(Application.getLoggedUser().getRole());

            Notification.removedSuccessfully("Room");


            veza.close();
        } catch (SQLException e) {
            Application.logger.info(e.getMessage(), e);
        } catch (IOException e) {
            Application.logger.info(e.getMessage(), e);
        }
    }
    static DoctorRoom getRoomWithId(Integer id){
        DoctorRoom certainDoctorRoom = null;
        try(Connection conn = Data.connectingToDatabase()) {

            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM HOSPITAL WHERE ID=" + id
            );
            while(proceduresResultSet.next()){
                certainDoctorRoom = getRoom(proceduresResultSet);
            }

        } catch (SQLException | IOException e) {
            Application.logger.info(e.getMessage(), e);
        }
        return certainDoctorRoom;
    }
    static DoctorRoom getRoomWithName(String name){
        DoctorRoom certainDoctorRoom = null;
        try(Connection conn = Data.connectingToDatabase()) {

            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM HOSPITAL WHERE ROOM='" + name + "'"
            );
            while(proceduresResultSet.next()){
                certainDoctorRoom = getRoom(proceduresResultSet);
            }

        } catch (SQLException | IOException e) {
            Application.logger.info(e.getMessage(), e);
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
        try(Connection conn = Data.connectingToDatabase()) {
            PreparedStatement stmnt = conn.prepareStatement("UPDATE DOCTORS SET ROOM='" + roomName + "' WHERE ID=" + doctorID );
            stmnt.executeUpdate();

            Change change = new Change(oldDoctor, DoctorData.getCertainDoctor(doctorID));
            ChangeWriter changeWriter = new ChangeWriter(change);
            changeWriter.addChange(Application.getLoggedUser().getRole());

        } catch (SQLException | IOException e) {
            Application.logger.info(e.getMessage(), e);
        }
    }
    static void unlinkRoomFromDoctor(DoctorRoom doctorRoomToRemove){
        try(Connection conn = Data.connectingToDatabase()){
            Optional<Doctor> oldDoctor = DoctorData.getAllDoctors().stream().filter(doctor -> doctor.getRoom().equals(doctorRoomToRemove.getRoomName())).findFirst();
            if(oldDoctor.isPresent()){

                PreparedStatement stmnt = conn.prepareStatement("UPDATE DOCTORS SET ROOM='" + "Not yet set" + "' WHERE ID=" + doctorRoomToRemove.getDoctorID());
                stmnt.executeUpdate();

                Change change = new Change<>(oldDoctor.get(), DoctorData.getCertainDoctor(doctorRoomToRemove.getDoctorID()));
                ChangeWriter changeWriter = new ChangeWriter(change);
                changeWriter.addChange(Application.getLoggedUser().getRole());
            }
        } catch (SQLException e) {
            Application.logger.info(e.getMessage(), e);
        } catch (IOException e) {
            Application.logger.info(e.getMessage(), e);
        }
    }
    static void unlinkDoctorFromRoom(String roomName){
        try(Connection conn = Data.connectingToDatabase()){
            Optional<DoctorRoom> oldRoom = getAllRooms().stream().filter(doctorRoom -> doctorRoom.getRoomName().equals(roomName)).findFirst();
            if(oldRoom.isPresent()){

                PreparedStatement stmnt = conn.prepareStatement("UPDATE HOSPITAL SET DOCTORID= -1 WHERE ROOM='" + roomName + "'");
                stmnt.executeUpdate();

                Change change = new Change(oldRoom.get(), getRoomWithName(roomName));
                ChangeWriter changeWriter = new ChangeWriter(change);
                changeWriter.addChange(Application.getLoggedUser().getRole());
            }
        } catch (SQLException e) {
            Application.logger.info(e.getMessage(), e);
        } catch (IOException e) {
            Application.logger.info(e.getMessage(), e);
        }
    }
    static Boolean hasDoctorRoom(Integer id){
        Doctor doctorToCheck = null;
        doctorToCheck = DoctorData.getCertainDoctor(id);
        if(doctorToCheck.getRoom().equals("Not yet set")){
            return false;
        }else{
            return true;
        }
    }

}
