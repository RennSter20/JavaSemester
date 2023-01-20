package com.example.renatojava.javasemester.doctorControllers;

import com.example.renatojava.javasemester.entity.Data;
import com.example.renatojava.javasemester.entity.Doctor;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;
import java.util.stream.Collectors;

public class EditDoctorsController {

    @FXML
    private TableView<Doctor> doctorTable;

    @FXML
    private TableColumn<Doctor, String> nameColumn, surnameColumn, genderColumn, titleColumn, roomColumn;

    @FXML
    private TextField filterField, nameEditField, surnameEditField, titleEditField;

    @FXML
    private RadioButton maleRadio, femaleRadio;

    public void search(){
        String filter = filterField.getText();

        Set<Doctor> filteredDoctors;

        try{
            filteredDoctors = Data.getAllDoctors().stream().filter(doctor -> doctor.getName().toLowerCase().contains(filter.toLowerCase()) ||
                    doctor.getSurname().toLowerCase().contains(filter.toLowerCase()) ||
                    doctor.getTitle().toLowerCase().contains(filter.toLowerCase()) ||
                    doctor.getRoom().toLowerCase().contains(filter.toLowerCase())).collect(Collectors.toSet());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        fillDoctorTable(filteredDoctors);
    }

    public void showInfo(){
        Doctor selectedDoctor;
        if(doctorTable.getSelectionModel().getSelectedItem() != null){
            selectedDoctor = doctorTable.getSelectionModel().getSelectedItem();
            nameEditField.setText(selectedDoctor.getName());
            surnameEditField.setText(selectedDoctor.getSurname());
            titleEditField.setText(selectedDoctor.getTitle());
            if(selectedDoctor.getGender().equals("M")){
                maleRadio.setSelected(true);
            }else{
                femaleRadio.setSelected(true);
            }
        }

    }

    public void apply(){

        if(Data.confirmEdit()){
            String newGender = "";
            if(maleRadio.isSelected()){
                newGender = "M";
            }else{
                newGender = "F";
            }
            Data.updateDoctor(doctorTable.getSelectionModel().getSelectedItem().getId() ,nameEditField.getText(), surnameEditField.getText(), titleEditField.getText(), newGender, doctorTable.getSelectionModel().getSelectedItem());
            initialize();
            Data.addedSuccessfully("Doctor");
        }
    }

    public void initialize(){
        try{
            fillDoctorTable(Data.getAllDoctors());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void fillDoctorTable(Set<Doctor> doctorList){

        ObservableList<Doctor> observableList = FXCollections.observableArrayList(doctorList);

        nameColumn.setCellValueFactory(doctor -> new SimpleStringProperty(doctor.getValue().getName()));
        surnameColumn.setCellValueFactory(doctor -> new SimpleStringProperty(doctor.getValue().getSurname()));
        titleColumn.setCellValueFactory(doctor -> new SimpleStringProperty(doctor.getValue().getTitle()));
        roomColumn.setCellValueFactory(doctor -> new SimpleStringProperty(doctor.getValue().getRoom()));
        genderColumn.setCellValueFactory(doctor -> new SimpleStringProperty(doctor.getValue().getGender()));

        doctorTable.setItems(observableList);
    }

}
