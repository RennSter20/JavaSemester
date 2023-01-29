package com.example.renatojava.javasemester.doctors;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.database.DoctorData;
import com.example.renatojava.javasemester.entity.Doctor;
import com.example.renatojava.javasemester.util.Notification;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;
import java.util.stream.Collectors;

public class EditDoctorsController implements DoctorData, Notification {

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

        Set<Doctor> filteredDoctors = null;

        try{
            filteredDoctors = DoctorData.getAllDoctors().stream().filter(doctor -> doctor.getName().toLowerCase().contains(filter.toLowerCase()) ||
                    doctor.getSurname().toLowerCase().contains(filter.toLowerCase()) ||
                    doctor.getTitle().toLowerCase().contains(filter.toLowerCase()) ||
                    doctor.getRoom().toLowerCase().contains(filter.toLowerCase())).collect(Collectors.toSet());
        } catch (SQLException | IOException e) {
            Application.logger.error(e.getMessage(), e);
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

        if(Notification.confirmEdit()){
            String newGender = "";
            if(maleRadio.isSelected()){
                newGender = "M";
            }else{
                newGender = "F";
            }

            DoctorData.updateDoctor(doctorTable.getSelectionModel().getSelectedItem().getId() ,nameEditField.getText(), surnameEditField.getText(), titleEditField.getText(), newGender, doctorTable.getSelectionModel().getSelectedItem());
            initialize();
        }
    }

    public void clearFields(){
        nameEditField.setText("");
        surnameEditField.setText("");
        titleEditField.setText("");
        maleRadio.setSelected(false);
        femaleRadio.setSelected(false);
    }

    public void initialize(){
        try{
            fillDoctorTable(DoctorData.getAllDoctors());
            clearFields();
        } catch (SQLException | IOException e) {
            Application.logger.error(e.getMessage(), e);
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
