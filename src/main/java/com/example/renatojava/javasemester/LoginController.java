package com.example.renatojava.javasemester;

import com.example.renatojava.javasemester.entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class LoginController {

    private static final String USERS_SERIALIZATION_FILE_NAME = "dat\\users.txt";

    Map<String, String> users = new HashMap<>();

    @FXML
    private TextField idTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Button loginButton;
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
            throw new RuntimeException(e);
        }

    }
    public void login(){

        String inputIdText = idTextField.getText();
        String inputPasswordText = passwordTextField.getText();

        if(users.containsKey(inputIdText) && users.get(inputIdText).equals(inputPasswordText)){
            errorText.setText("");
            Application.setLoggedUser(getUser(inputPasswordText));
            BorderPane root;
            try {
                root = FXMLLoader.load(
                        getClass().getResource("menuScreen.fxml"));
                Application.setMainPage(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            errorText.setText("User not found!");
            idTextField.setText("");
            passwordTextField.setText("");
        }



    }
    public User getUser(String pass){
        User userToSet = null;
        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/production", "student", "student");

            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM USERS WHERE PASSWORD=" + pass
            );

            while(proceduresResultSet.next()){
                userToSet = getUserFromResult(proceduresResultSet);
            }

            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return userToSet;
    }
    public static User getUserFromResult(ResultSet procedureSet) throws SQLException{

        String id = procedureSet.getString("ID");
        String password = procedureSet.getString("PASSWORD");
        String name = procedureSet.getString("NAME");
        String surname = procedureSet.getString("SURNAME");
        String role = procedureSet.getString("ROLE");
        String oib = procedureSet.getString("OIB");


        return new User(id, password, name, surname, role, oib);

    }

}