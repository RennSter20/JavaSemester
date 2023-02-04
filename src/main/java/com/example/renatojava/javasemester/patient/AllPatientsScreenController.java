package com.example.renatojava.javasemester.patient;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.database.BillData;
import com.example.renatojava.javasemester.database.Data;
import com.example.renatojava.javasemester.database.PatientData;
import com.example.renatojava.javasemester.database.StatsData;
import com.example.renatojava.javasemester.entity.Patient;
import com.example.renatojava.javasemester.entity.Stats;
import com.example.renatojava.javasemester.exceptions.ObjectExistsException;
import com.example.renatojava.javasemester.threads.FreeBed;
import com.example.renatojava.javasemester.util.Notification;
import com.example.renatojava.javasemester.util.StatsChanger;
import com.example.renatojava.javasemester.util.Validator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class AllPatientsScreenController implements Data, Validator, PatientData, Notification, BillData {

    @FXML
    private TableColumn<Patient, String> nameColumn, surnameColumn, OIBColumn, genderColumn, debtColumn;
    @FXML
    private TableView<Patient> patientsTable;
    @FXML
    private TextField filterField, nameEditField, surnameEditField, oibEditField;
    @FXML
    private Button editButton, applyButton, removeButton, createBillButton;

    private List<Patient> patients;


    @FXML
    public void initialize(){

        patients = PatientData.getAllPatients();
        fillTable(patients);
        editButton.setVisible(false);
        applyButton.setDisable(true);
        removeButton.setDisable(true);
        createBillButton.setDisable(true);
        clearFields();
    }

    public void fillTable(List<Patient> list){
        ObservableList<Patient> observableList = FXCollections.observableArrayList(list);

        nameColumn.setCellValueFactory(patient -> new SimpleStringProperty(patient.getValue().getName()));
        surnameColumn.setCellValueFactory(patient -> new SimpleStringProperty(patient.getValue().getSurname()));
        OIBColumn.setCellValueFactory(patient -> new SimpleStringProperty(patient.getValue().getOib()));
        genderColumn.setCellValueFactory(patient -> new SimpleStringProperty(patient.getValue().getGender()));
        debtColumn.setCellValueFactory(patient -> new SimpleStringProperty(String.valueOf(patient.getValue().getDebt())));

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

            if(!Validator.isNameValid(newName) || !Validator.isNameValid(newSurname) ||!Validator.isOibValid(newOib)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Problem with setting new info!");
                alert.setContentText("NOTE:\nName and surname cannot be empty!\nOib must contain 10 numeric characters!");
                alert.show();
                return;
            }

            try{
                if(Notification.confirmEdit()){
                    PatientData.updatePatient(newName, newSurname, newOib, selectedPatient);
                    initialize();
                }
            }catch (ObjectExistsException e){
                Application.logger.error(e.getMessage(), e);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Info");
                alert.setHeaderText(e.getMessage());
                alert.show();
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
            if(Notification.confirmEdit()){
                Application.executorService.execute(new FreeBed(Application.hospital));
                PatientData.removePatient(patientsTable.getSelectionModel().getSelectedItem().getId());
            }
        }catch (SQLException | IOException e){
            Application.logger.error(e.getMessage(), e);
        }
        initialize();
    }

    public void createBill(){
        Patient selectedPatient = patientsTable.getSelectionModel().getSelectedItem();
        if(selectedPatient.getDebt() > 0){
            if(Notification.confirmEdit()){
                BillData.createBill(selectedPatient, LocalDateTime.now());

                Stats currentStats = StatsData.getCurrentStats();
                Integer currBills = currentStats.bills();
                List<String> changesSQL = new ArrayList<>();
                changesSQL.add("BILLS=" + (++currBills));
                StatsChanger.changeStats(changesSQL);

                initialize();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Error when creating a bill.");
            alert.setContentText("Patient has no debt.");
            alert.show();
        }
    }
}
