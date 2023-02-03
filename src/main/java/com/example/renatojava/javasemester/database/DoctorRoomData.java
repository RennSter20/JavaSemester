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
            ResultSet roomsResultSet = sqlStatement.executeQuery("SELECT * FROM HOSPITAL");

            while(roomsResultSet.next()){
                DoctorRoom newDoctorRoom = getRoom(roomsResultSet);
                doctorRoomList.add(newDoctorRoom);
            }
        } catch (SQLException | IOException e) {
            Application.logger.info(e.getMessage(), e);
        }
        return doctorRoomList;
    }
    static void addRoom(String roomName, Integer doctorID){

        try(Connection conn = Data.connectingToDatabase()){
            CheckObjects.checkIfRoomExists(roomName);

            PreparedStatement stmnt = conn.prepareStatement("INSERT INTO HOSPITAL(ROOM, DOCTORID, DOCTOR_NAME) VALUES(?,?,?)");
            stmnt.setString(1, roomName);
            stmnt.setInt(2, doctorID);
            stmnt.setString(3, DoctorData.getCertainDoctorFromId(doctorID).getDoctorFullName());
            stmnt.executeUpdate();

            Change change = new Change(new DoctorRoom("-", -1, -1, "-"), new DoctorRoom(roomName, doctorID, getRoomWithName(roomName).getRoomID(), DoctorData.getCertainDoctorFromId(doctorID).getDoctorFullName()));
            ChangeWriter changeWriter = new ChangeWriter(change);
            changeWriter.addChange(Application.getLoggedUser().getRole());

            linkDoctorWithRoom(DoctorData.getCertainDoctorFromId(doctorID), doctorID, roomName);

            Notification.addedSuccessfully("Room");


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
        try(Connection veza = Data.connectingToDatabase()){

            DoctorRoom oldDoctorRoom = getRoomWithId(id);

            PreparedStatement stmnt = veza.prepareStatement("DELETE FROM HOSPITAL WHERE ID=" + id);
            stmnt.executeUpdate();

            Change change = new Change(new DoctorRoom(oldDoctorRoom.getRoomName(), oldDoctorRoom.getDoctorID(), oldDoctorRoom.getRoomID(), DoctorData.getCertainDoctorFromId(oldDoctorRoom.getDoctorID()).getDoctorFullName()), new DoctorRoom("-", -1, -1, "-"));
            ChangeWriter changeWriter = new ChangeWriter(change);
            changeWriter.addChange(Application.getLoggedUser().getRole());

            Notification.removedSuccessfully("Room");

        } catch (SQLException | IOException e) {
            Application.logger.info(e.getMessage(), e);
        }
    }
    static void updateRoom(String newRoomName, Doctor newDoctor, DoctorRoom oldRoom){
        try(Connection conn = Data.connectingToDatabase()) {


            PreparedStatement stmnt = conn.prepareStatement("UPDATE HOSPITAL SET ROOM='" + newRoomName + "', DOCTORID=" + newDoctor.getId() + ", DOCTOR_NAME='" + newDoctor.getDoctorFullName() + "' WHERE ID=" + oldRoom.getRoomID());
            stmnt.executeUpdate();

            if(!oldRoom.getDoctorName().equals(newDoctor.getDoctorFullName())){

                Doctor oldDoctor = DoctorData.getCertainDoctorFromId(oldRoom.getDoctorID());

                PreparedStatement changeDoctorOld = conn.prepareStatement("UPDATE DOCTORS SET ROOM='Not yet set.' WHERE ID=" + oldRoom.getDoctorID());
                changeDoctorOld.executeUpdate();

                PreparedStatement changeDoctorNew = conn.prepareStatement("UPDATE DOCTORS SET ROOM='" + newRoomName + "' WHERE ID=" + newDoctor.getId());
                changeDoctorNew.executeUpdate();


                Change roomChange = new Change(oldRoom, DoctorRoomData.getRoomWithId(oldRoom.getRoomID()));
                ChangeWriter roomChangeWriter = new ChangeWriter(roomChange);
                roomChangeWriter.addChange(Application.getLoggedUser().getRole());


                Change doctorChange = new Change(oldDoctor, DoctorData.getCertainDoctorFromId(oldRoom.getDoctorID()));
                ChangeWriter doctorChangeWriter = new ChangeWriter(doctorChange);
                doctorChangeWriter.addChange(Application.getLoggedUser().getRole());

                Change newDoctorChange = new Change(newDoctor, DoctorData.getCertainDoctorFromId(newDoctor.getId()));
                ChangeWriter newDoctorChangeWriter = new ChangeWriter(newDoctorChange);
                newDoctorChangeWriter.addChange(Application.getLoggedUser().getRole());

            }

            if(!oldRoom.getRoomName().equals(newRoomName)){
                PreparedStatement updateDoctor = conn.prepareStatement("UPDATE DOCTORS SET ROOM='" + newRoomName + "' WHERE ID=" + newDoctor.getId());
                updateDoctor.executeUpdate();

                Change roomChange = new Change(oldRoom, DoctorRoomData.getRoomWithId(oldRoom.getRoomID()));
                ChangeWriter roomChangeWriter = new ChangeWriter(roomChange);
                roomChangeWriter.addChange(Application.getLoggedUser().getRole());
            }

            Notification.updatedSuccessfully("Room");

        } catch (SQLException | IOException e) {
            Application.logger.error(e.getMessage(), e);
        }
    }
    static DoctorRoom getRoomWithId(Integer id){
        DoctorRoom certainDoctorRoom = null;
        try(Connection conn = Data.connectingToDatabase()) {

            Statement sqlStatement = conn.createStatement();
            ResultSet roomResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM HOSPITAL WHERE ID=" + id
            );
            while(roomResultSet.next()){
                certainDoctorRoom = getRoom(roomResultSet);
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
            ResultSet roomResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM HOSPITAL WHERE ROOM='" + name + "'"
            );
            while(roomResultSet.next()){
                certainDoctorRoom = getRoom(roomResultSet);
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
        String doctorName = procedureSet.getString("doctor_name");

        return new DoctorRoom(roomName, doctorID, id, doctorName);
    }
    static void linkDoctorWithRoom(Doctor oldDoctor, Integer doctorID, String roomName){

        try(Connection conn = Data.connectingToDatabase()) {
            PreparedStatement stmnt = conn.prepareStatement("UPDATE DOCTORS SET ROOM='" + roomName + "' WHERE ID=" + doctorID );
            stmnt.executeUpdate();

            Change change = new Change(oldDoctor, DoctorData.getCertainDoctorFromId(doctorID));
            ChangeWriter changeWriter = new ChangeWriter(change);
            changeWriter.addChange(Application.getLoggedUser().getRole());

        } catch (SQLException | IOException e) {
            Application.logger.info(e.getMessage(), e);
        }
    }
    static void unlinkRoomFromDoctor(DoctorRoom doctorRoomToRemove) {
        try (Connection conn = Data.connectingToDatabase()) {

            Optional<Doctor> oldDoctor = DoctorData.getAllDoctors().stream().filter(doctor -> doctor.getRoom().equals(doctorRoomToRemove.getRoomName())).findFirst();
            if (oldDoctor.isPresent()) {

                PreparedStatement stmnt = conn.prepareStatement("UPDATE DOCTORS SET ROOM='" + "Not yet set" + "' WHERE ID=" + doctorRoomToRemove.getDoctorID());
                stmnt.executeUpdate();

                Change change = new Change<>(oldDoctor.get(), DoctorData.getCertainDoctorFromId(doctorRoomToRemove.getDoctorID()));
                ChangeWriter changeWriter = new ChangeWriter(change);
                changeWriter.addChange(Application.getLoggedUser().getRole());
            }
        } catch (SQLException | IOException e) {
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
        } catch (SQLException | IOException e) {
            Application.logger.info(e.getMessage(), e);
        }
    }
    static Boolean hasDoctorRoom(Integer id){
        Doctor doctorToCheck;
        doctorToCheck = DoctorData.getCertainDoctorFromId(id);
        if(doctorToCheck.getRoom().equals("Not yet set")){
            return false;
        }else{
            return true;
        }
    }

}
