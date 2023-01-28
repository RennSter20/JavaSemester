package com.example.renatojava.javasemester.checkups;

import com.example.renatojava.javasemester.entity.ActiveCheckup;
import com.example.renatojava.javasemester.database.Data;
import com.example.renatojava.javasemester.util.DateFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class ListOfActiveCheckupsController {

    @FXML
    private TableView<ActiveCheckup> table;

    @FXML
    private TableColumn<ActiveCheckup, String> dateColumn, patientColumn, procedureColumn, roomColumn, oibColumn;

    public void initialize(){
        fillCheckupTable(Data.getAllActiveCheckups());
    }

    public void fillCheckupTable(List<ActiveCheckup> list){
        ObservableList<ActiveCheckup> observableList = FXCollections.observableArrayList(list);

        dateColumn.setCellValueFactory(checkup -> new SimpleStringProperty(DateFormatter.getDateTimeFormatted(checkup.getValue().getDateOfCheckup().toString())));
        patientColumn.setCellValueFactory(checkup -> new SimpleStringProperty(Data.getPatientWithID(checkup.getValue().getPatientID()).getFullName()));
        oibColumn.setCellValueFactory(checkup -> new SimpleStringProperty(Data.getPatientWithID(checkup.getValue().getPatientID()).getOib()));
        procedureColumn.setCellValueFactory(checkup -> new SimpleStringProperty(Data.getProcedureFromId(checkup.getValue().getProcedureID()).description()));
        roomColumn.setCellValueFactory(checkup -> new SimpleStringProperty(checkup.getValue().getRoom().getRoomType()));

        table.setItems(observableList);
    }

    public void accept(){
        if(table.getSelectionModel().getSelectedItem() != null && Data.confirmEdit()){
            Data.addProcedureToPatient(Data.getPatientWithID(table.getSelectionModel().getSelectedItem().getPatientID()).getId(), Data.getProcedureFromId(table.getSelectionModel().getSelectedItem().getProcedureID()).description());
            Data.removeActiveCheckup(table.getSelectionModel().getSelectedItem().getId());
            initialize();
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INFORMATION");
            alert.setHeaderText("Error with checkup!");
            alert.setContentText("No checkup selected");
            alert.show();
        }
    }

    public void reject(){
        if(table.getSelectionModel().getSelectedItem() != null && Data.confirmEdit()){
            Data.removeActiveCheckup(table.getSelectionModel().getSelectedItem().getId());
            Data.removedSuccessfully("Checkup");
            initialize();
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INFORMATION");
            alert.setHeaderText("Error with checkup!");
            alert.setContentText("No checkup selected");
            alert.show();
        }
    }

}
