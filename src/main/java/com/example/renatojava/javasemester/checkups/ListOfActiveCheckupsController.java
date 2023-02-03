package com.example.renatojava.javasemester.checkups;

import com.example.renatojava.javasemester.database.CheckupData;
import com.example.renatojava.javasemester.database.PatientData;
import com.example.renatojava.javasemester.database.ProcedureData;
import com.example.renatojava.javasemester.entity.ActiveCheckup;
import com.example.renatojava.javasemester.util.DateFormatter;
import com.example.renatojava.javasemester.util.Notification;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class ListOfActiveCheckupsController implements CheckupData, PatientData, ProcedureData, Notification {

    @FXML
    private TableView<ActiveCheckup> table;
    @FXML
    private TableColumn<ActiveCheckup, String> dateColumn, patientColumn, procedureColumn, roomColumn, oibColumn;
    @FXML
    public void initialize(){
        fillCheckupTable(CheckupData.getAllActiveCheckups());
    }

    public void fillCheckupTable(List<ActiveCheckup> list){
        ObservableList<ActiveCheckup> observableList = FXCollections.observableArrayList(list);

        dateColumn.setCellValueFactory(checkup -> new SimpleStringProperty(DateFormatter.getDateTimeFormatted(checkup.getValue().getDateOfCheckup().toString())));
        patientColumn.setCellValueFactory(checkup -> new SimpleStringProperty(PatientData.getPatientWithID(checkup.getValue().getPatientID()).getFullName()));
        oibColumn.setCellValueFactory(checkup -> new SimpleStringProperty(PatientData.getPatientWithID(checkup.getValue().getPatientID()).getOib()));
        procedureColumn.setCellValueFactory(checkup -> new SimpleStringProperty(ProcedureData.getProcedureFromId(checkup.getValue().getProcedureID()).description()));
        roomColumn.setCellValueFactory(checkup -> new SimpleStringProperty(checkup.getValue().getRoom().getRoomType()));

        table.setItems(observableList);
    }

    public void accept(){
        if(table.getSelectionModel().getSelectedItem() != null && Notification.confirmEdit()){

            ProcedureData.addProcedureToPatient(PatientData.getPatientWithID(table.getSelectionModel().getSelectedItem().getPatientID()).getId(),
                                                                            ProcedureData.getProcedureFromId(table.getSelectionModel().getSelectedItem().getProcedureID()).description());

            CheckupData.removeActiveCheckup(table.getSelectionModel().getSelectedItem().getId());
            initialize();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Error with checkup!");
            alert.setContentText("Error while accepting checkup!");
            alert.show();
        }
    }

    public void reject(){
        if(table.getSelectionModel().getSelectedItem() != null && Notification.confirmEdit()){

            CheckupData.removeActiveCheckup(table.getSelectionModel().getSelectedItem().getId());

            initialize();

        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Error with checkup!");
            alert.setContentText("Error while rejecting checkup!");
            alert.show();
        }
    }

}
