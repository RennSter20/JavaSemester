package com.example.renatojava.javasemester.patientControllers;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.entity.Data;
import com.example.renatojava.javasemester.entity.Patient;
import com.example.renatojava.javasemester.entity.Validator;
import com.example.renatojava.javasemester.util.DateFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AllPatientsScreenController implements Data, Validator {

    @FXML
    private TableColumn<Patient, String> nameColumn, surnameColumn, OIBColumn, genderColumn, debtColumn;
    @FXML
    private TableView<Patient> patientsTable;
    @FXML
    private TextField filterField, nameEditField, surnameEditField, oibEditField;
    @FXML
    private Text birthDate;
    @FXML
    private Button editButton, applyButton, removeButton, createBillButton;

    private List<Patient> patients;


    @FXML
    public void initialize(){

        patients = Data.getAllPatients();
        fillTable(patients);
        editButton.setVisible(false);
        applyButton.setDisable(true);
        removeButton.setDisable(true);
        createBillButton.setDisable(true);
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
            birthDate.setText(DateFormatter.getDateFormatted(String.valueOf(selectedPatient.getDate())));
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
        createBillButton.setDisable(false);
    }
    public void editPatient(){
        Patient selectedPatient = patientsTable.getSelectionModel().getSelectedItem();
        if(selectedPatient != null){
            String newName = nameEditField.getText();
            String newSurname = surnameEditField.getText();
            String newOib = oibEditField.getText();

            if(!Validator.isNameValid(newName) || !Validator.isNameValid(newSurname) ||!Validator.isOibValid(newOib)) return;

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
                Data.removePatient(patientsTable.getSelectionModel().getSelectedItem().getId());
            }
        }catch (SQLException | IOException e){
            Application.logger.info("Message: " + e.getMessage() + " Stack trace: " + e.getStackTrace());
        }
        initialize();
        clearFields();
        birthDate.setText("");
    }

    public void createBill(){
        Patient selectedPatient = patientsTable.getSelectionModel().getSelectedItem();
        if(selectedPatient.getDebt() > 0){
            if(Data.confirmEdit()){
                Data.createBill(selectedPatient, LocalDateTime.now());
                initialize();
                clearFields();
                birthDate.setText("");
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INFORMATION");
            alert.setHeaderText("Error when creating a bill.");
            alert.setContentText("Patient has no debt.");
            alert.show();
        }
    }
}
