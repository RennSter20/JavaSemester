package com.example.renatojava.javasemester;

import com.example.renatojava.javasemester.entity.Data;
import com.example.renatojava.javasemester.entity.Patient;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.stream.Collectors;

public class AllPatientsScreenController implements Data {

    @FXML
    private TableColumn<Patient, String> nameColumn, surnameColumn, OIBColumn, genderColumn, debtColumn;

    @FXML
    private TableView<Patient> patientsTable;
    @FXML
    private TextField filterField;

    private List<Patient> patients;

    @FXML
    public void initialize(){

        patients = Data.getAllPatients();
        fillTable(patients);

    }

    public void fillTable(List<Patient> list){
        ObservableList<Patient> observableList = FXCollections.observableArrayList(list);

        nameColumn.setCellValueFactory(patient -> {
            return new SimpleStringProperty(patient.getValue().getName());
        });
        surnameColumn.setCellValueFactory(patient -> {
            return new SimpleStringProperty(patient.getValue().getSurname());
        });
        OIBColumn.setCellValueFactory(patient -> {
            return new SimpleStringProperty(patient.getValue().getOib());
        });
        genderColumn.setCellValueFactory(patient -> {
            return new SimpleStringProperty(patient.getValue().getGender());
        });
        debtColumn.setCellValueFactory(patient -> {
            return new SimpleStringProperty(String.valueOf(patient.getValue().getDebt()));
        });

        patientsTable.setItems(observableList);
    }

    public void searchPatient(){
        String filter = filterField.getText();

        List<Patient> filteredPatients;

        filteredPatients = patients.stream().filter(patient -> patient.getName().toLowerCase().contains(filter.toLowerCase()) ||
                patient.getSurname().toLowerCase().contains(filter.toLowerCase()) ||
                patient.getOib().contains(filter)).collect(Collectors.toList());

        fillTable(filteredPatients);
    }

}
