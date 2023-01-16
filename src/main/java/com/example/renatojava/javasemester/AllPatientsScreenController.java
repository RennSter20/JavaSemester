package com.example.renatojava.javasemester;

import com.example.renatojava.javasemester.entity.Checker;
import com.example.renatojava.javasemester.entity.Data;
import com.example.renatojava.javasemester.entity.Patient;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AllPatientsScreenController implements Data, Checker {

    @FXML
    private TableColumn<Patient, String> nameColumn, surnameColumn, OIBColumn, genderColumn, debtColumn;
    @FXML
    private TableView<Patient> patientsTable;
    @FXML
    private TextField filterField, nameEditField, surnameEditField, oibEditField;
    @FXML
    private Text birthDate;
    @FXML
    private Button editButton, applyButton, removeButton;

    private List<Patient> patients;


    @FXML
    public void initialize(){

        patients = Data.getAllPatients();
        fillTable(patients);
        editButton.setVisible(false);
        applyButton.setDisable(true);
        removeButton.setDisable(true);
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
        editButton.setVisible(false);
        patientsTable.getSelectionModel().clearSelection();
    }

    public void editButtonAppear(){
        if(patientsTable.getSelectionModel().getSelectedItem() != null){
            editButton.setVisible(true);
            fillEditFields();
        }
    }

    public void fillEditFields(){

        Patient selectedPatient = patientsTable.getSelectionModel().getSelectedItem();
        if(selectedPatient != null){
            nameEditField.setText(selectedPatient.getName());
            surnameEditField.setText(selectedPatient.getSurname());
            oibEditField.setText(selectedPatient.getOib());
            birthDate.setText(String.valueOf(selectedPatient.getDate()));
            nameEditField.setEditable(false);
            surnameEditField.setEditable(false);
            oibEditField.setEditable(false);
        }
    }
    public void enableEdit(){
        nameEditField.setEditable(true);
        surnameEditField.setEditable(true);
        oibEditField.setEditable(true);
        applyButton.setDisable(false);
        removeButton.setDisable(false);
    }
    public void editPatient(){
        Patient selectedPatient = patientsTable.getSelectionModel().getSelectedItem();
        if(selectedPatient != null){
            String newName = nameEditField.getText();
            String newSurname = surnameEditField.getText();
            String newOib = oibEditField.getText();

            if(!Checker.isNameValid(newName) || !Checker.isNameValid(newSurname) ||!Checker.isOibValid(newOib)) return;

            if(Data.confirmEdit()){
                Data.updatePatient(newName, newSurname, newOib, selectedPatient);
                clearFields();
                initialize();
            }
        }
    }

    public void clearFields(){
        nameEditField.setText("");
        surnameEditField.setText("");
        oibEditField.setText("");
    }

    public void removePatient() {
        try{
            if(Data.confirmEdit()){
                Data.removePatient(patientsTable.getSelectionModel().getSelectedItem().getOib());
            }
        }catch (SQLException | IOException e){
            Application.logger.info("Message: " + e.getMessage() + " Stack trace: " + e.getStackTrace());
        }
        initialize();
        clearFields();
        birthDate.setText("");
    }
}
