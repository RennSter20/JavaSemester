package com.example.renatojava.javasemester.user;

import com.example.renatojava.javasemester.entity.User;
import com.example.renatojava.javasemester.util.ChangeWriter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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
        ObservableList<User> userObservableList = FXCollections.observableList(reader.readUsers());

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
            List<String> users = reader.readTimeUsers();
            List<String> rolesAndChanges = reader.readRoleChangeUsers();

            List<String> roles = new ArrayList<>();
            List<String> changes = new ArrayList<>();

            for(int i = 0;i<rolesAndChanges.size();i+=2){
                roles.add(rolesAndChanges.get(i));
            }
            for(int i = 1;i<rolesAndChanges.size();i+=2){
                changes.add(rolesAndChanges.get(i));
            }


            changeLabel.setText("Changes made: " + users.get(tableView.getSelectionModel().getSelectedIndex()) + ", " + changes.get(tableView.getSelectionModel().getSelectedIndex()) + " by " + roles.get(tableView.getSelectionModel().getSelectedIndex()));

        }
    }


}
