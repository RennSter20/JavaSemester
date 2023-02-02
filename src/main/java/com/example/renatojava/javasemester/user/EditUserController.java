package com.example.renatojava.javasemester.user;

import com.example.renatojava.javasemester.database.UserData;
import com.example.renatojava.javasemester.entity.User;
import com.example.renatojava.javasemester.util.Notification;
import com.example.renatojava.javasemester.util.Validator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

public class EditUserController {

    @FXML
    private TextField passwordField, nameField, surnameField;

    @FXML
    private Label idLabel;
    @FXML
    private ComboBox<String> roleField;

    @FXML
    private TableView<User> userTableView;

    @FXML
    private TableColumn<User, String> idColumn, nameColumn, surnameColumn, roleColumn;

    public void initialize(){
        ObservableList<User> userObservableList = FXCollections.observableList(UserData.allUsersDatabase());

        idColumn.setCellValueFactory(user -> new SimpleStringProperty(user.getValue().getId()));
        nameColumn.setCellValueFactory(user -> new SimpleStringProperty(user.getValue().getName()));
        surnameColumn.setCellValueFactory(user -> new SimpleStringProperty(user.getValue().getSurname()));
        roleColumn.setCellValueFactory(user -> new SimpleStringProperty(user.getValue().getRole()));

        userTableView.setItems(userObservableList);

        roleField.setItems(FXCollections.observableArrayList("Doctor", "Receptionist"));
        clearFields();
    }

    public void onSelect(){
        Optional<User> selectedUser = Optional.ofNullable(userTableView.getSelectionModel().getSelectedItem());
        if(selectedUser.isPresent()){
            if(!selectedUser.get().getRole().equals("Admin")){
                passwordField.setText(selectedUser.get().getPassword());
                nameField.setText(selectedUser.get().getName());
                surnameField.setText(selectedUser.get().getSurname());
                roleField.getSelectionModel().select(selectedUser.get().getRole());
            }
        }
    }

    public void update(){
        if(Validator.isNameValid(nameField.getText()) && Validator.isNameValid(surnameField.getText()) && Notification.confirmEdit()){
            UserData.updateUser(userTableView.getSelectionModel().getSelectedItem().getId(), passwordField.getText(), nameField.getText(), surnameField.getText(), roleField.getSelectionModel().getSelectedItem());
            initialize();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Error while creating new user.");
            alert.setContentText("Name and surname need to contain only alphabetic characters.\nOib needs to contain 10 numeric characters!");
            alert.show();
        }
    }

    public void delete(){
        Optional<User> selectedUser = Optional.ofNullable(userTableView.getSelectionModel().getSelectedItem());
        if(selectedUser.isPresent() && Notification.confirmEdit()){
            UserData.deleteUser(selectedUser.get().getId());
            initialize();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setTitle("Error while deleting user.");
            alert.setContentText("Please select user first!");
            alert.show();
        }
    }

    public void clearFields(){
        idLabel.setText("");
        passwordField.setText("");
        nameField.setText("");
        surnameField.setText("");
        roleField.getSelectionModel().select("Doctor");
    }

}
