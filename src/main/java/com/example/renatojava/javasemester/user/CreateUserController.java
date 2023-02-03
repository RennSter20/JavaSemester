package com.example.renatojava.javasemester.user;

import com.example.renatojava.javasemester.database.UserData;
import com.example.renatojava.javasemester.util.Notification;
import com.example.renatojava.javasemester.util.Validator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class CreateUserController {

    @FXML
    private TextField idField, passwordField, nameField, surnameField;

    @FXML
    private ComboBox<String> roleField;
    @FXML
    public void initialize(){
        clearFields();
        roleField.setItems(FXCollections.observableArrayList("Doctor", "Receptionist"));
    }

    public void add(){
        if(roleField.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Error while creating new user.");
            alert.setContentText("Please select user's role!");
            alert.show();
        }else{
            if(Validator.isNameValid(nameField.getText()) && Validator.isNameValid(surnameField.getText()) && Notification.confirmEdit()){
                UserData.createNewUser(idField.getText(), passwordField.getText(), nameField.getText(), surnameField.getText(), roleField.getSelectionModel().getSelectedItem());
                initialize();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Error while creating new user.");
                alert.setContentText("Name and surname need to contain only alphabetic characters.\nOib needs to contain 10 numeric characters!");
                alert.show();
            }
        }
    }

    public void clearFields(){
        idField.setText("");
        passwordField.setText("");
        nameField.setText("");
        surnameField.setText("");
        roleField.getSelectionModel().select("Doctor");
    }

}
