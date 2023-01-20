package com.example.renatojava.javasemester.checkups;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.entity.*;
import com.example.renatojava.javasemester.exceptions.NoProceduresException;
import com.example.renatojava.javasemester.util.CheckObjects;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.StringConverter;
import tornadofx.control.DateTimePicker;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AddCheckupController {

    public static final DateTimeFormatter DATE_TIME_FORMAT_FULL = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
    @FXML
    private TableView<Patient> patientsTable;
    @FXML
    private TableColumn<Patient, String> nameColumn, surnameColumn, oibColumn;

    @FXML
    private TableView<Procedure> procedureTable;
    @FXML
    private TableColumn<Procedure, String> procedureColumn, priceColumn;
    @FXML
    private DateTimePicker datePicker;
    @FXML
    private ChoiceBox<PatientRoom> roomChoiceBox;

    private List<Patient> patientList = new ArrayList<>();
    private List<Procedure> procedureList = new ArrayList<>();

    @FXML
    public void initialize(){
        patientList = Data.getAllPatients();
        fillPatientsTable(patientList);

        List<PatientRoom> rooms = List.of(new RoomA("A"), new RoomB("B"), new RoomC("C"));
        ObservableList<PatientRoom> observableList = FXCollections.observableArrayList(rooms);
        roomChoiceBox.setItems(observableList);
        roomChoiceBox.getSelectionModel().selectFirst();
        roomChoiceBox.setConverter(new StringConverter<PatientRoom>() {
            @Override
            public String toString(PatientRoom patientRoom) {
                return patientRoom.getRoomType();
            }

            @Override
            public PatientRoom fromString(String s) {
                return null;
            }
        });


        try{
            procedureList = Data.getAllProcedures();
        } catch (SQLException | IOException e) {
            Application.logger.info("Message: " + e.getMessage() + " Stack trace: " + e.getStackTrace());
        }catch (NoProceduresException e){
            Application.logger.info("Message: " + e.getMessage() + " Stack trace: " + e.getStackTrace());
        }
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

    public void addNewCheckup(){
        if (!CheckObjects.isValidTime(String.valueOf(datePicker.getDateTimeValue()), DATE_TIME_FORMAT_FULL)) {
            Data.addNewActiveCheckup(procedureTable.getSelectionModel().getSelectedItem().id(), Integer.valueOf(patientsTable.getSelectionModel().getSelectedItem().getId()), datePicker.getDateTimeValue(), roomChoiceBox.getValue());
        }

    }

}
