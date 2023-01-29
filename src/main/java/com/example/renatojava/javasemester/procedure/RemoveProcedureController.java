package com.example.renatojava.javasemester.procedure;

import com.example.renatojava.javasemester.database.PatientData;
import com.example.renatojava.javasemester.database.ProcedureData;
import com.example.renatojava.javasemester.entity.Patient;
import com.example.renatojava.javasemester.util.Notification;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class RemoveProcedureController implements PatientData, ProcedureData, Notification {

    @FXML
    private TableView<Patient> patientTable;

    @FXML
    private ListView<String> procedureListView;
    @FXML
    private TableColumn<Patient, String> patientName, patientSurname, patientOIB, procedureDescription;

    public void initialize(){
        List<Patient> allPatients = PatientData.getAllPatients();

        ObservableList<Patient> observableList = FXCollections.observableArrayList(allPatients);

        patientName.setCellValueFactory(patient -> new SimpleStringProperty(patient.getValue().getName()));
        patientSurname.setCellValueFactory(patient -> new SimpleStringProperty(patient.getValue().getSurname()));
        patientOIB.setCellValueFactory(patient -> new SimpleStringProperty(patient.getValue().getOib()));

        patientTable.setItems(observableList);
    }

    public void onSelectedPatient(){
        if(patientTable.getSelectionModel().getSelectedItem() != null){
            String proceduresFromPatient = ProcedureData.getAllProceduresFromPatientString(patientTable.getSelectionModel().getSelectedItem());
            List<String> splittedProceduresFromPatient = List.of(proceduresFromPatient.split(","));
            ObservableList<String> observableList = FXCollections.observableArrayList(splittedProceduresFromPatient);

            if(observableList.size() > 0){
                procedureListView.setItems(observableList);
            }else{
                observableList = FXCollections.observableArrayList("None");
                procedureListView.setItems(observableList);
            }
        }
    }

    public void remove(){
        if(procedureListView.getSelectionModel().getSelectedItem() != null){
            String selectedProcedure = procedureListView.getSelectionModel().getSelectedItem();

            if(!Notification.confirmEdit()){
                Alert failure = new Alert(Alert.AlertType.ERROR);
                failure.setTitle("ERROR");
                failure.setHeaderText("Failure!");
                failure.setContentText("Procedure is not removed from the system!");
                failure.show();
                return;
            }else{
                ProcedureData.removeProcedure(selectedProcedure, patientTable.getSelectionModel().getSelectedItem().getId(), patientTable.getSelectionModel().getSelectedItem().getProcedures());
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("INFORMATION");
                success.setHeaderText("Success!");
                success.setContentText("Procedure successfully removed from the system!");
                success.show();
            }
            procedureListView.setItems(null);
            initialize();
        }
    }

}
