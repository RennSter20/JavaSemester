package com.example.renatojava.javasemester;

import com.example.renatojava.javasemester.entity.Data;
import com.example.renatojava.javasemester.entity.Patient;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class RemoveProcedureController {

    @FXML
    private TableView<Patient> patientTable;

    @FXML
    private ListView<String> procedureListView;
    @FXML
    private TableColumn<Patient, String> patientName, patientSurname, patientOIB, procedureDescription;

    public void initialize(){
        List<Patient> allPatients = Data.getAllPatients();

        ObservableList<Patient> observableList = FXCollections.observableArrayList(allPatients);

        patientName.setCellValueFactory(patient -> {
            return new SimpleStringProperty(patient.getValue().getName());
        });
        patientSurname.setCellValueFactory(patient -> {
            return new SimpleStringProperty(patient.getValue().getSurname());
        });
        patientOIB.setCellValueFactory(patient -> {
            return new SimpleStringProperty(patient.getValue().getOib());
        });

        patientTable.setItems(observableList);
    }

    public void onSelectedPatient(){
        if(patientTable.getSelectionModel().getSelectedItem() != null){
            String proceduresFromPatient = Data.getAllProceduresFromPatient(patientTable.getSelectionModel().getSelectedItem());
            List<String> splittedProceduresFromPatient = List.of(proceduresFromPatient.split("/"));
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
            Data.removeProcedure(selectedProcedure, patientTable.getSelectionModel().getSelectedItem().getOib(), patientTable.getSelectionModel().getSelectedItem().getProcedures());

            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("INFORMATION");
            success.setHeaderText("Success!");
            success.setContentText("Procedure successfully removed from the system!");
            success.show();

            initialize();
        }
    }

}
