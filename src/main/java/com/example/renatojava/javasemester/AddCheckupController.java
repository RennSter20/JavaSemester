package com.example.renatojava.javasemester;

import com.example.renatojava.javasemester.entity.Data;
import com.example.renatojava.javasemester.entity.Patient;
import com.example.renatojava.javasemester.entity.Procedure;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;

public class AddCheckupController implements Data {

    @FXML
    private TableView<Patient> patientsTable;
    @FXML
    private TableColumn<Patient, String> nameColumn, surnameColumn, oibColumn;

    @FXML
    private TableView<Procedure> procedureTable;
    @FXML
    private TableColumn<Procedure, String> procedureColumn, priceColumn;
    @FXML
    private ListView<String> listView;

    private List<Patient> patientList = new ArrayList<>();
    private List<Procedure> procedureList = new ArrayList<>();

    @FXML
    public void initialize(){
        patientList = Data.getAllPatients();
        fillPatientsTable(patientList);

        procedureList = Data.getAllProcedures();
        fillProceduresTable(procedureList);
    }

    public void fillPatientsTable(List<Patient> list){
        ObservableList<Patient> observableList = FXCollections.observableArrayList(list);

        nameColumn.setCellValueFactory(patient -> {
            return new SimpleStringProperty(patient.getValue().getName());
        });
        surnameColumn.setCellValueFactory(patient -> {
            return new SimpleStringProperty(patient.getValue().getSurname());
        });
        oibColumn.setCellValueFactory(patient -> {
            return new SimpleStringProperty(patient.getValue().getOib());
        });


        patientsTable.setItems(observableList);
    }

    public void fillProceduresTable(List<Procedure> list){
        ObservableList<Procedure> observableList = FXCollections.observableArrayList(list);

        procedureColumn.setCellValueFactory(procedure -> {
            return new SimpleStringProperty(procedure.getValue().description());
        });
        priceColumn.setCellValueFactory(procedure -> {
            return new SimpleStringProperty(String.valueOf(procedure.getValue().price()));
        });


        procedureTable.setItems(observableList);
    }

    public void fillTableView(){
        String proceduresFromPatient = Data.getAllProceduresFromPatient(patientsTable.getSelectionModel().getSelectedItem());
        List<String> splittedProceduresFromPatient = List.of(proceduresFromPatient.split("/"));
        ObservableList<String> observableList = FXCollections.observableArrayList(splittedProceduresFromPatient);

        if(observableList.size() > 0){
            listView.setItems(observableList);
        }else{
            observableList = FXCollections.observableArrayList("None");
            listView.setItems(observableList);
        }
    }

    public void addProcedure(){

        List<String> errorArray = new ArrayList<>();

        if(patientsTable.getSelectionModel().getSelectedItem() == null){
                errorArray.add("No patient selected!");
        }
        if(procedureTable.getSelectionModel().getSelectedItem() == null){
            errorArray.add("No procedure selected!");
        }
        if(errorArray.size() > 0){
            String errorMessage = "";
            for(String s : errorArray){
                errorMessage = errorMessage + s + "\n";
            }
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errors found!");
            alert.setContentText(errorMessage);
            alert.show();

            return;
        }

        Data.addProcedureToPatient(patientsTable.getSelectionModel().getSelectedItem().getOib(), String.valueOf(procedureTable.getSelectionModel().getSelectedItem().description()));
        initialize();
    }
}
