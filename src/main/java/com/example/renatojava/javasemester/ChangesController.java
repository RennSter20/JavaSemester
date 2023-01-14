package com.example.renatojava.javasemester;

import com.example.renatojava.javasemester.entity.ChangeWriter;
import com.example.renatojava.javasemester.entity.Doctor;
import com.example.renatojava.javasemester.entity.Patient;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChangesController {

    @FXML
    private TableView<Patient> oldTable, newTable;

    @FXML
    private Text changeTimeText;

    ChangeWriter changer = new ChangeWriter();
    List<String> changesTime = changer.readTime();
    Integer selectedPatient;
    @FXML
    private TableColumn<Patient, String> nameOld, nameNew, surnameOld, surnameNew, oibOld, oibNew, genderOld, genderNew, debtOld, debtNew, proceduresOld, proceduresNew;

    public void initialize(){
        Map<Patient, Patient> patientMap = changer.readChanges();

        List<Patient> oldPatients = new ArrayList<>(patientMap.keySet());
        List<Patient> newPatients = new ArrayList<>(patientMap.values());


        fillOldTable(oldPatients);
        fillNewTable(newPatients);
    }

    public void fillOldTable(List<Patient> oldPatients){

        ObservableList<Patient> observableList = FXCollections.observableArrayList(oldPatients);

        nameOld.setCellValueFactory(patient -> {
            return new SimpleStringProperty(patient.getValue().getName());
        });
        surnameOld.setCellValueFactory(patient -> {
            return new SimpleStringProperty(patient.getValue().getSurname());
        });
        oibOld.setCellValueFactory(patient -> {
            return new SimpleStringProperty(patient.getValue().getOib());
        });
        genderOld.setCellValueFactory(patient -> {
            return new SimpleStringProperty(patient.getValue().getGender());
        });
        debtOld.setCellValueFactory(patient -> {
            return new SimpleStringProperty(Double.valueOf(patient.getValue().getDebt()).toString());
        });
        proceduresOld.setCellValueFactory(patient -> {
            return new SimpleStringProperty(patient.getValue().getProcedures());
        });

        oldTable.setItems(observableList);

    }

    public void fillNewTable(List<Patient> newPatients){

        ObservableList<Patient> observableList = FXCollections.observableArrayList(newPatients);

        nameNew.setCellValueFactory(patient -> {
            return new SimpleStringProperty(patient.getValue().getName());
        });
        surnameNew.setCellValueFactory(patient -> {
            return new SimpleStringProperty(patient.getValue().getSurname());
        });
        oibNew.setCellValueFactory(patient -> {
            return new SimpleStringProperty(patient.getValue().getOib());
        });
        genderNew.setCellValueFactory(patient -> {
            return new SimpleStringProperty(patient.getValue().getGender());
        });
        debtNew.setCellValueFactory(patient -> {
            return new SimpleStringProperty(Double.valueOf(patient.getValue().getDebt()).toString());
        });
        proceduresNew.setCellValueFactory(patient -> {
            return new SimpleStringProperty(patient.getValue().getProcedures());
        });

        newTable.setItems(observableList);

    }

    public void showTimeOld(){
        selectedPatient = oldTable.getSelectionModel().getSelectedIndex();
        newTable.setSelectionModel(oldTable.getSelectionModel());
        changeTimeText.setText(changesTime.get(selectedPatient));
    }

    public void showTimeNew(){
        selectedPatient = newTable.getSelectionModel().getSelectedIndex();
        oldTable.setSelectionModel(newTable.getSelectionModel());
        changeTimeText.setText(changesTime.get(selectedPatient));
    }

}
