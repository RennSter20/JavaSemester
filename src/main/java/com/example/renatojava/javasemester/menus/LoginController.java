package com.example.renatojava.javasemester.menus;

import com.example.renatojava.javasemester.database.Data;
import com.example.renatojava.javasemester.entity.User;
import com.example.renatojava.javasemester.exceptions.UserNotFoundException;
import com.example.renatojava.javasemester.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LoginController {

    private static final String USERS_SERIALIZATION_FILE_NAME = "dat\\users.txt";

    Map<String, String> users = new HashMap<>();

    @FXML
    private TextField idTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Text errorText;

    @FXML
    public void initialize(){

        try(Scanner scanner = new Scanner(new File(USERS_SERIALIZATION_FILE_NAME))){
            while(scanner.hasNextLine()){

            String id = scanner.nextLine();
            String password = scanner.nextLine();

            users.put(id, password);

            }
        } catch (FileNotFoundException e) {
            Application.logger.info("Message: " + e.getMessage() + " Stack trace: " + e.getStackTrace());
        }

    }
    public void login() {

        String inputIdText = idTextField.getText();
        String inputPasswordText = passwordTextField.getText();


        String hashedPassword = DigestUtils.sha1Hex(inputPasswordText);
        System.out.println(hashedPassword);
        try{
            if(users.containsKey(inputIdText) && users.get(inputIdText).equals(hashedPassword)){
                errorText.setText("");
                Application.setLoggedUser(getUser(inputIdText));
                BorderPane root;
                try {
                    root = FXMLLoader.load(
                            getClass().getResource("/fxml/menuScreen.fxml"));
                    Application.setMainPage(root);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                throw new UserNotFoundException("User not found!");
            }
        }catch (UserNotFoundException e){
            Application.logger.info("Message: " + e.getMessage() + " Stack trace: " + e.getStackTrace());
            errorText.setText("User not found!");
            idTextField.setText("");
            passwordTextField.setText("");
        }



    }
    public User getUser(String id){
        User userToSet = null;
        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/production", "student", "student");

            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM USERS WHERE ID='" + id + "'"
            );

            while(proceduresResultSet.next()){
                userToSet = Data.getUserFromResult(proceduresResultSet);
            }

            conn.close();

        } catch (SQLException e) {
            Application.logger.info(String.valueOf(e.getStackTrace()));
        }

        return userToSet;
    }


}