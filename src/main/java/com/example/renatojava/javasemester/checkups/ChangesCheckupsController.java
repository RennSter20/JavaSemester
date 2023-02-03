package com.example.renatojava.javasemester.checkups;

import com.example.renatojava.javasemester.entity.ActiveCheckup;
import com.example.renatojava.javasemester.util.ChangeWriter;
import com.example.renatojava.javasemester.util.DateFormatter;
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

public class ChangesCheckupsController {

    @FXML
    private TableView<ActiveCheckup> checkupTable;
    @FXML
    private TableColumn<ActiveCheckup, String> dateColumn, patientColumn, procedureColumn;
    @FXML
    private Label changeText;
    private ChangeWriter reader;

    @FXML
    public void initialize(){
        reader = new ChangeWriter();

        List<ActiveCheckup> allChanges = reader.readCheckups();
        List<ActiveCheckup> newCheckups = new ArrayList<>();
        for(int i = 1;i<allChanges.size();i+=2){
            newCheckups.add(allChanges.get(i));
        }

        fillCheckupTable(newCheckups);
    }

    public void fillCheckupTable(List<ActiveCheckup> allCheckups){
        ObservableList<ActiveCheckup> list = FXCollections.observableArrayList(allCheckups);

        dateColumn.setCellValueFactory(checkup -> new SimpleStringProperty(DateFormatter.getDateTimeFormatted(checkup.getValue().getDateOfCheckup().toString())));
        patientColumn.setCellValueFactory(checkup -> new SimpleStringProperty(checkup.getValue().getPatientFullName()));
        procedureColumn.setCellValueFactory(checkup -> new SimpleStringProperty(checkup.getValue().getProcedure()));

        checkupTable.setItems(list);
    }

    public void showInfo(){
        Optional<ActiveCheckup> selectedCheckup = Optional.ofNullable(checkupTable.getSelectionModel().getSelectedItem());

        if(selectedCheckup.isPresent()){
            List<String> times = reader.readTimeCheckups();
            List<String> roles = reader.readRoleChangeCheckups();

            changeText.setText("Changes made: " + times.get(checkupTable.getSelectionModel().getSelectedIndex()) + " " + " by " + roles.get(checkupTable.getSelectionModel().getSelectedIndex()));
        }
    }

    public void moreInfo(){
        Optional<ActiveCheckup> selectedCheckup = Optional.ofNullable(checkupTable.getSelectionModel().getSelectedItem());

        List<ActiveCheckup> allChanges = reader.readCheckups();
        List<ActiveCheckup> oldCheckups = new ArrayList<>();
        for(int i = 0;i<allChanges.size();i+=2){
            oldCheckups.add(allChanges.get(i));
        }

        Alert alert;
        if(selectedCheckup.isPresent()){
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INFORMATION");
            alert.setHeaderText("More info about checkup change.");
            alert.setContentText("OLD VALUE:\n" + oldCheckups.get(checkupTable.getSelectionModel().getSelectedIndex()) + "\n\nNEW VALUE:\n" + selectedCheckup.get());
        }else{
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("No checkup change selected!");
            alert.setContentText("Please select checkup to show more info!");
        }
        alert.show();
    }

}
