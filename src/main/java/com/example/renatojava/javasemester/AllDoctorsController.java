package com.example.renatojava.javasemester;

import com.example.renatojava.javasemester.entity.Data;
import com.example.renatojava.javasemester.entity.Doctor;
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

public class AllDoctorsController {

    @FXML
    private TableView<Doctor> doctorsTable;

    @FXML
    private TableColumn<Doctor, String> nameColumn, surnameColumn, titleColumn, roomColumn;

    @FXML
    private TextField searchField;

    public void initialize(){
        fillDoctorTable(Data.getAllDoctors());
    }

    public void fillDoctorTable(List<Doctor> doctorList){

            ObservableList<Doctor> observableList = FXCollections.observableArrayList(doctorList);

            nameColumn.setCellValueFactory(doctor -> {
                return new SimpleStringProperty(doctor.getValue().getName());
            });
            surnameColumn.setCellValueFactory(doctor -> {
                return new SimpleStringProperty(doctor.getValue().getSurname());
            });
            titleColumn.setCellValueFactory(doctor -> {
                return new SimpleStringProperty(doctor.getValue().getTitle());
            });
            roomColumn.setCellValueFactory(doctor -> {
                return new SimpleStringProperty(doctor.getValue().getRoom());
            });


        doctorsTable.setItems(observableList);

    }

    public void search(){
        String searchText = searchField.getText();

        List<Doctor> filteredDoctors;

        filteredDoctors = Data.getAllDoctors().stream().filter(doctor -> doctor.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                doctor.getSurname().toLowerCase().contains(searchText.toLowerCase()) ||
                doctor.getTitle().contains(searchText.toLowerCase()) ||
                doctor.getRoom().contains(searchText.toLowerCase())).collect(Collectors.toList());

        fillDoctorTable(filteredDoctors);
    }

}
