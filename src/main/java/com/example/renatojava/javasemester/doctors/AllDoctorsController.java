package com.example.renatojava.javasemester.doctors;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.database.Data;
import com.example.renatojava.javasemester.entity.Doctor;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;
import java.util.stream.Collectors;

public class AllDoctorsController {

    @FXML
    private TableView<Doctor> doctorsTable;

    @FXML
    private TableColumn<Doctor, String> nameColumn, surnameColumn, titleColumn, roomColumn;

    @FXML
    private TextField searchField;

    public void initialize(){
        try{
            fillDoctorTable(Data.getAllDoctors());
        }catch (SQLException | IOException e) {
            Application.logger.error(e.getMessage(), e);
        }


    }
    public void fillDoctorTable(Set<Doctor> doctorList){

        ObservableList<Doctor> observableList = FXCollections.observableArrayList(doctorList);

        nameColumn.setCellValueFactory(doctor -> new SimpleStringProperty(doctor.getValue().getName()));
        surnameColumn.setCellValueFactory(doctor -> new SimpleStringProperty(doctor.getValue().getSurname()));
        titleColumn.setCellValueFactory(doctor -> new SimpleStringProperty(doctor.getValue().getTitle()));
        roomColumn.setCellValueFactory(doctor -> new SimpleStringProperty(doctor.getValue().getRoom()));

        doctorsTable.setItems(observableList);
    }

    public void search(){
        String searchText = searchField.getText();

        Set<Doctor> filteredDoctors = null;

        try{
            filteredDoctors = Data.getAllDoctors().stream().filter(doctor -> doctor.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                    doctor.getSurname().toLowerCase().contains(searchText.toLowerCase()) ||
                    doctor.getTitle().contains(searchText.toLowerCase()) ||
                    doctor.getRoom().contains(searchText.toLowerCase()))
                    .collect(Collectors.toSet());

        } catch (SQLException | IOException e) {
            Application.logger.error(e.getMessage(), e);
        }

        fillDoctorTable(filteredDoctors);
    }

}
