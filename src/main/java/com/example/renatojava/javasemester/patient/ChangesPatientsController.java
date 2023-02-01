package com.example.renatojava.javasemester.patient;

import com.example.renatojava.javasemester.util.ChangeWriter;
import com.example.renatojava.javasemester.entity.Patient;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChangesPatientsController {

    @FXML
    private TableView<Patient> oldTable, newTable;

    @FXML
    private Text changeTimeText, role;

    ChangeWriter changer = new ChangeWriter();
    List<String> changesTime = changer.readTimePatients();
    List<String> rolesList;
    Integer selectedPatient;
    @FXML
    private TableColumn<Patient, String> nameOld, nameNew, surnameOld, surnameNew, oibOld, oibNew, genderOld, genderNew, debtOld, debtNew, proceduresOld, proceduresNew;

    public void initialize(){
        List<Patient> patientList = changer.readPatients();
        rolesList = changer.readRoleChangePatients();

        List<Patient> oldPatients = new ArrayList<>();
        List<Patient> newPatients = new ArrayList<>();

        for(int i = 0;i<patientList.size();i+=2){
            oldPatients.add(patientList.get(i));
        }
        for(int i = 1;i<patientList.size();i+=2){
            newPatients.add(patientList.get(i));
        }

        fillOldTable(oldPatients);
        fillNewTable(newPatients);
    }

    public void fillOldTable(List<Patient> oldPatients){

        ObservableList<Patient> observableList = FXCollections.observableArrayList(oldPatients);

        nameOld.setCellValueFactory(patient -> new SimpleStringProperty(patient.getValue().getName()));
        surnameOld.setCellValueFactory(patient -> new SimpleStringProperty(patient.getValue().getSurname()));
        oibOld.setCellValueFactory(patient -> new SimpleStringProperty(patient.getValue().getOib()));
        genderOld.setCellValueFactory(patient -> new SimpleStringProperty(patient.getValue().getGender()));
        debtOld.setCellValueFactory(patient -> new SimpleStringProperty(Double.valueOf(patient.getValue().getDebt()).toString()));
        proceduresOld.setCellValueFactory(patient -> new SimpleStringProperty(patient.getValue().getProcedures()));

        oldTable.setItems(observableList);

    }

    public void fillNewTable(List<Patient> newPatients){

        ObservableList<Patient> observableList = FXCollections.observableArrayList(newPatients);

        nameNew.setCellValueFactory(patient -> new SimpleStringProperty(patient.getValue().getName()));
        surnameNew.setCellValueFactory(patient -> new SimpleStringProperty(patient.getValue().getSurname()));
        oibNew.setCellValueFactory(patient -> new SimpleStringProperty(patient.getValue().getOib()));
        genderNew.setCellValueFactory(patient -> new SimpleStringProperty(patient.getValue().getGender()));
        debtNew.setCellValueFactory(patient -> new SimpleStringProperty(Double.valueOf(patient.getValue().getDebt()).toString()));
        proceduresNew.setCellValueFactory(patient -> new SimpleStringProperty(patient.getValue().getProcedures()));

        newTable.setItems(observableList);

    }

    public void showTimeOld(){
        if(oldTable.getSelectionModel().getSelectedItem() != null){
            selectedPatient = oldTable.getSelectionModel().getSelectedIndex();
            newTable.getSelectionModel().select(oldTable.getSelectionModel().getFocusedIndex());
            changeTimeText.setText(changesTime.get(selectedPatient));
            role.setText("by role " + rolesList.get(oldTable.getSelectionModel().getSelectedIndex()));
        }
    }

    public void showTimeNew(){
        if(newTable.getSelectionModel().getSelectedItem() != null){
            selectedPatient = newTable.getSelectionModel().getSelectedIndex();
            oldTable.getSelectionModel().select(newTable.getSelectionModel().getFocusedIndex());
            changeTimeText.setText(changesTime.get(selectedPatient));
            role.setText("by role " + rolesList.get(newTable.getSelectionModel().getSelectedIndex()));
        }
    }

    public void moreInfo(){
        Optional<Patient> selOldPatient = Optional.ofNullable(oldTable.getSelectionModel().getSelectedItem());
        Optional<Patient> selNewPatient = Optional.ofNullable(newTable.getSelectionModel().getSelectedItem());

        if(selOldPatient.isPresent() || selNewPatient.isPresent()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INFORMATION");
            alert.setHeaderText("More info about patient change.");
            alert.setContentText("OLD VALUE:\n" + selOldPatient.get() + "\n\nNEW VALUE:\n" + selNewPatient.get());
            alert.show();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("No patient change selected!");
            alert.setContentText("Please select patient to show more info!");
            alert.show();
        }
    }

}
