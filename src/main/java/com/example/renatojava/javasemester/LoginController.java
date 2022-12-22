package com.example.renatojava.javasemester;

import com.example.renatojava.javasemester.entity.Doctor;
import com.example.renatojava.javasemester.entity.Person;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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

}