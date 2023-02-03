package com.example.renatojava.javasemester.database;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.entity.User;
import com.example.renatojava.javasemester.exceptions.ObjectExistsException;
import com.example.renatojava.javasemester.util.ChangeWriter;
import com.example.renatojava.javasemester.util.CheckObjects;
import com.example.renatojava.javasemester.util.Notification;
import javafx.scene.control.Alert;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public interface UserData {

    String USERS_SERIALIZATION_FILE_NAME = "dat\\users.txt";

    static User getUserFromResult(ResultSet procedureSet) throws SQLException {

        String id = procedureSet.getString("ID");
        String password = procedureSet.getString("PASSWORD");
        String name = procedureSet.getString("NAME");
        String surname = procedureSet.getString("SURNAME");
        String role = procedureSet.getString("ROLE");


        return new User(id, password, name, surname, role);
    }

    static void createNewUser(String id, String password, String name, String surname, String role){

        try{
            CheckObjects.checkIfUserExists(id);
        }catch (ObjectExistsException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(e.getMessage());
            alert.show();
            Application.logger.error(e.getMessage(), e);
        }

        try(Connection conn = Data.connectingToDatabase()){

            PreparedStatement stmnt = conn.prepareStatement("INSERT INTO USERS(ID, PASSWORD, NAME, SURNAME, ROLE) VALUES(?,?,?,?,?)");
            stmnt.setString(1, id);
            stmnt.setString(2, password);
            stmnt.setString(3, name);
            stmnt.setString(4, surname);
            stmnt.setString(5, role);
            stmnt.executeUpdate();

            writeAllUsers(allUsersDatabase());
            ChangeWriter changeWriter = new ChangeWriter();
            User addeduser = UserData.getUserFromId(id);
            changeWriter.addUserChange(addeduser, "user created");

            Notification.addedSuccessfully("User");

        }catch (IOException | SQLException e){
            Application.logger.error(e.getMessage(), e);
        }
    }

    static void updateUser(String id, String password, String name, String surname, String role){
        try(Connection conn = Data.connectingToDatabase()) {

            PreparedStatement stmnt = conn.prepareStatement("UPDATE USERS SET PASSWORD='" + password + "', NAME='" + name + "', SURNAME='" + surname + "', ROLE='" + role + "' WHERE ID='" + id + "'");
            stmnt.executeUpdate();

            writeAllUsers(allUsersDatabase());
            ChangeWriter changeWriter = new ChangeWriter();
            changeWriter.addUserChange(UserData.getUserFromId(id), "user updated");

            Notification.updatedSuccessfully("User");

        } catch (SQLException | IOException e) {
            Application.logger.error(e.getMessage(), e);
        }
    }

    static void deleteUser(String id){
        try(Connection conn = Data.connectingToDatabase()) {

            User deletedUser = UserData.getUserFromId(id);

            PreparedStatement stmnt = conn.prepareStatement("DELETE FROM USERS WHERE ID='" + id + "'");
            stmnt.executeUpdate();

            writeAllUsers(allUsersDatabase());
            ChangeWriter changeWriter = new ChangeWriter();
            changeWriter.addUserChange(deletedUser, "user deleted");

            Notification.removedSuccessfully("User");

        } catch (SQLException | IOException e) {
            Application.logger.error(e.getMessage(), e);
        }
    }

    static void writeAllUsers(List<User> users){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_SERIALIZATION_FILE_NAME));
            for(User user : users){
                writer.write(user.getId()+"\n");
                writer.write(DigestUtils.sha1Hex(user.getPassword()) + "\n");
            }
            writer.close();
        }catch (IOException e){
            Application.logger.error(e.getMessage(), e);
        }
    }

    static List<User> allUsersDatabase(){
        List<User> userList = new ArrayList<>();

        try(Connection conn = Data.connectingToDatabase()) {


            Statement sqlStatement = conn.createStatement();
            ResultSet resultSet = sqlStatement.executeQuery(
                    "SELECT * FROM USERS"
            );

            while(resultSet.next()){
                userList.add(getUserFromResult(resultSet));
            }

        } catch (SQLException | IOException e) {
            Application.logger.error(e.getMessage(), e);
        }
        return userList;
    }

    static User getUserFromId(String id){
        User user = null;

        try(Connection conn = Data.connectingToDatabase()) {

            Statement sqlStatement = conn.createStatement();
            ResultSet resultSet = sqlStatement.executeQuery(
                    "SELECT * FROM USERS WHERE ID='" + id +"'"
            );

            while(resultSet.next()){
                user = getUserFromResult(resultSet);
            }

        } catch (SQLException | IOException e) {
            Application.logger.error(e.getMessage(), e);
        }
        return user;
    }
}
