package com.example.renatojava.javasemester.user;

import com.example.renatojava.javasemester.entity.User;
import com.example.renatojava.javasemester.util.ChangeWriter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserChangesController {

    @FXML
    private TableView<User> tableView;

    @FXML
    private TableColumn<User, String> idColumn, passwordColumn, nameColumn, surnameColumn, roleColumn;

    @FXML
    private Label changeLabel;

    private ChangeWriter reader;

    @FXML
    public void initialize(){
        reader = new ChangeWriter();
        List<User> allChanges = reader.readUsers();
        List<User> newUsers = new ArrayList<>();
        for(int i = 1;i<allChanges.size();i+=2){
            newUsers.add(allChanges.get(i));
        }

        ObservableList<User> userObservableList = FXCollections.observableList(newUsers);

        idColumn.setCellValueFactory(user -> new SimpleStringProperty(user.getValue().getId()));
        passwordColumn.setCellValueFactory(user -> new SimpleStringProperty(user.getValue().getPassword()));
        nameColumn.setCellValueFactory(user -> new SimpleStringProperty(user.getValue().getName()));
        surnameColumn.setCellValueFactory(user -> new SimpleStringProperty(user.getValue().getSurname()));
        roleColumn.setCellValueFactory(user -> new SimpleStringProperty(user.getValue().getRole()));

        tableView.setItems(userObservableList);
    }

    public void onSelect(){
        Optional<User> selectedUser = Optional.ofNullable(tableView.getSelectionModel().getSelectedItem());

        if(selectedUser.isPresent()){
            List<String> times = reader.readTimeUsers();
            List<String> roles = reader.readRoleChangeUsers();

            changeLabel.setText("Changes made: " + times.get(tableView.getSelectionModel().getSelectedIndex()) + " " + " by " + roles.get(tableView.getSelectionModel().getSelectedIndex()));

        }
    }

    public void moreInfo(){
        Optional<User> selectedUser = Optional.ofNullable(tableView.getSelectionModel().getSelectedItem());

        List<User> allChanges = reader.readUsers();
        List<User> oldUsers = new ArrayList<>();
        for(int i = 0;i<allChanges.size();i+=2){
            oldUsers.add(allChanges.get(i));
        }

        if(selectedUser.isPresent()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INFORMATION");
            alert.setHeaderText("More info about user change.");
            alert.setContentText("OLD VALUE:\n" + oldUsers.get(tableView.getSelectionModel().getSelectedIndex()) + "\n\nNEW VALUE:\n" + selectedUser.get());
            alert.show();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("No user change selected!");
            alert.setContentText("Please select user to show more info!");
            alert.show();
        }
    }


}
