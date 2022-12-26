package com.example.renatojava.javasemester;

import com.example.renatojava.javasemester.entity.Data;
import com.example.renatojava.javasemester.entity.Patient;
import com.example.renatojava.javasemester.entity.Procedure;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ManageCheckupsController implements Data {

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
        List<Procedure> proceduresFromPatient = Data.getAllProceduresFromPatient(patientsTable.getSelectionModel().getSelectedItem());
        List<String> proc;
        proc = proceduresFromPatient.stream().map(Procedure::description).collect(Collectors.toList());
        ObservableList<String> observableList = FXCollections.observableArrayList(proc);

        if(observableList.size() > 0){
            listView.setItems(observableList);
        }else{
            observableList = FXCollections.observableArrayList("None");
            listView.setItems(observableList);
        }
    }

    public void addProcedure(){
        Data.addProcedureToPatient(patientsTable.getSelectionModel().getSelectedItem().getOib(), String.valueOf(procedureTable.getSelectionModel().getSelectedItem()));
        initialize();
    }
}
