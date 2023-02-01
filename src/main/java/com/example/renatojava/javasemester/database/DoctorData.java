package com.example.renatojava.javasemester.database;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.entity.Change;
import com.example.renatojava.javasemester.util.ChangeWriter;
import com.example.renatojava.javasemester.entity.Doctor;
import com.example.renatojava.javasemester.entity.Stats;
import com.example.renatojava.javasemester.exceptions.ObjectExistsException;
import com.example.renatojava.javasemester.util.CheckObjects;
import com.example.renatojava.javasemester.util.Notification;
import com.example.renatojava.javasemester.util.StatsChanger;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface DoctorData {

    static Set<Doctor> getAllDoctors() throws SQLException, IOException {
        Set<Doctor> doctorList = new HashSet<>();

        try(Connection conn = Data.connectingToDatabase()) {
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

            Stats currentStats = StatsData.getCurrentStats();
            Integer currDoctors = currentStats.doctors();
            List<String> changesSQL = new ArrayList<>();
            changesSQL.add("DOCTORS=" + (++currDoctors));
            StatsChanger.changeStats(changesSQL);

            Change change = new Change(new Doctor.Builder().withName("-").withSurname("-").withGender("-").withRoom("-").withTitle("-").build(),doctor);
            ChangeWriter changeWriter = new ChangeWriter();
            changeWriter.addChange(Application.getLoggedUser().getRole());

            Notification.addedSuccessfully("Doctor");


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
        Connection veza = Data.connectingToDatabase();

        PreparedStatement stmnt = veza.prepareStatement("DELETE FROM DOCTORS WHERE ROOM='" + doctor.getRoom() + "'");
        stmnt.executeUpdate();

        Stats currentStats = StatsData.getCurrentStats();
        Integer oldCountDoctors = currentStats.doctors();
        Integer newCountDoctors = oldCountDoctors - 1;
        List<String> changesSQL = new ArrayList<>();
        changesSQL.add("DOCTORS=" + (newCountDoctors));
        StatsChanger.changeStats(changesSQL);

        Change change = new Change(doctor, new Doctor.Builder().withName("-").withSurname("-").withGender("-").withRoom("-").withTitle("-").build());
        ChangeWriter changeWriter = new ChangeWriter(change);
        changeWriter.addChange(Application.getLoggedUser().getRole());

        Notification.removedSuccessfully("Doctor");

        veza.close();
    }
    static void updateDoctor(Integer id, String newName, String newSurname,String newTitle, String newGender, Doctor oldDoctor){
        try(Connection conn = Data.connectingToDatabase()) {

            PreparedStatement stmnt = conn.prepareStatement("UPDATE DOCTORS SET NAME='" + newName + "', SURNAME='" + newSurname + "', TITLE='" + newTitle + "', GENDER='" + newGender + "' WHERE ID=" + id);
            stmnt.executeUpdate();

            Change change = new Change(oldDoctor, DoctorData.getCertainDoctor(id));
            ChangeWriter changeWriter = new ChangeWriter();
            changeWriter.addChange(Application.getLoggedUser().getRole());

            Notification.updatedSuccessfully("Doctor");

        } catch (SQLException | IOException e) {
            Application.logger.error(e.getMessage(), e);
        }
    }
    static Doctor getCertainDoctor(Integer id){

        Doctor newDoctor = new Doctor.Builder().withName("-1").withSurname("").build();

        try(Connection conn = Data.connectingToDatabase()) {


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

}
