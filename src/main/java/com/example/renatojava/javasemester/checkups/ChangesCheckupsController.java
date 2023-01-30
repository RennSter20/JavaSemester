package com.example.renatojava.javasemester.checkups;

import com.example.renatojava.javasemester.database.PatientData;
import com.example.renatojava.javasemester.database.ProcedureData;
import com.example.renatojava.javasemester.entity.ActiveCheckup;
import com.example.renatojava.javasemester.entity.ChangeWriter;
import com.example.renatojava.javasemester.util.DateFormatter;
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

public class ChangesCheckupsController {

    @FXML
    private TableView<ActiveCheckup> checkupTable;

    @FXML
    private TableColumn<ActiveCheckup, String> dateColumn, patientColumn, procedureColumn;

    @FXML
    private Label changeText;
    ChangeWriter reader = new ChangeWriter();


    public void initialize(){
        fillCheckupTable(reader.readCheckups());
    }

    public void fillCheckupTable(List<ActiveCheckup> allCheckups){
        ObservableList<ActiveCheckup> list = FXCollections.observableArrayList(allCheckups);

        dateColumn.setCellValueFactory(checkup -> new SimpleStringProperty(DateFormatter.getDateTimeFormatted(checkup.getValue().getDateOfCheckup().toString())));
        patientColumn.setCellValueFactory(checkup -> new SimpleStringProperty(PatientData.getPatientWithID(checkup.getValue().getPatientID()).getFullName()));
        procedureColumn.setCellValueFactory(checkup -> new SimpleStringProperty(ProcedureData.getProcedureFromId(checkup.getValue().getProcedureID()).description()));

        checkupTable.setItems(list);

    }

    public void showInfo(){
        Optional<ActiveCheckup> selectedCheckup = Optional.ofNullable(checkupTable.getSelectionModel().getSelectedItem());

        List<String> checkups = reader.readTimeCheckups();
        List<String> rolesAndChanges = reader.readRoleChangeCheckups();

        List<String> roles = new ArrayList<>();
        List<String> changes = new ArrayList<>();

        for(int i = 0;i<rolesAndChanges.size();i+=2){
            roles.add(rolesAndChanges.get(i));
        }
        for(int i = 1;i<rolesAndChanges.size();i+=2){
            changes.add(rolesAndChanges.get(i));
        }

        if(selectedCheckup.isPresent()){
            String currentText = changeText.getText();
            changeText.setText("Changes made: " + checkups.get(checkupTable.getSelectionModel().getSelectedIndex()) + " " + changes.get(checkupTable.getSelectionModel().getSelectedIndex()) + " by " + roles.get(checkupTable.getSelectionModel().getSelectedIndex()));
        }
    }

}
