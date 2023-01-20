package com.example.renatojava.javasemester.doctorControllers;

import com.example.renatojava.javasemester.entity.ChangeWriter;
import com.example.renatojava.javasemester.entity.Doctor;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ChangesDoctorsController {

    @FXML
    private TableView<Doctor> oldTable, newTable;
    @FXML
    private TableColumn<Doctor, String> nameOld, nameNew, surnameOld, surnameNew, genderOld, genderNew, titleOld, titleNew, roomOld, roomNew;

    @FXML
    private Text changeTimeText;
    ChangeWriter changer = new ChangeWriter();

    List<String> changesTime = changer.readTimeDoctors();

    Integer selectedDoctor;

    public void initialize(){
        List<Doctor> patientList = changer.readDoctors();

        List<Doctor> oldDoctor = new ArrayList<>();
        List<Doctor> newDoctors = new ArrayList<>();

        for(int i = 0;i<patientList.size();i+=2){
            oldDoctor.add(patientList.get(i));
        }
        for(int i = 1;i<patientList.size();i+=2){
            newDoctors.add(patientList.get(i));
        }

        fillOldTable(oldDoctor);
        fillNewTable(newDoctors);
    }
    public void showTimeOld(){
        if(oldTable.getSelectionModel().getSelectedItem() != null){
            selectedDoctor = oldTable.getSelectionModel().getSelectedIndex();
            newTable.getSelectionModel().select(oldTable.getSelectionModel().getFocusedIndex());
            changeTimeText.setText(changesTime.get(selectedDoctor));
        }
    }

    public void showTimeNew(){
        if(newTable.getSelectionModel().getSelectedItem() != null){
            selectedDoctor = newTable.getSelectionModel().getSelectedIndex();
            oldTable.getSelectionModel().select(newTable.getSelectionModel().getFocusedIndex());
            changeTimeText.setText(changesTime.get(selectedDoctor));
        }
    }

    public void fillOldTable(List<Doctor> oldDoctors){

        ObservableList<Doctor> observableList = FXCollections.observableArrayList(oldDoctors);

        nameOld.setCellValueFactory(doctor -> new SimpleStringProperty(doctor.getValue().getName()));
        surnameOld.setCellValueFactory(doctor -> new SimpleStringProperty(doctor.getValue().getSurname()));
        genderOld.setCellValueFactory(doctor -> new SimpleStringProperty(doctor.getValue().getGender()));
        titleOld.setCellValueFactory(doctor -> new SimpleStringProperty(doctor.getValue().getTitle()));
        roomOld.setCellValueFactory(doctor -> new SimpleStringProperty(doctor.getValue().getRoom()));


        oldTable.setItems(observableList);

    }

    public void fillNewTable(List<Doctor> newDoctors){

        ObservableList<Doctor> observableList = FXCollections.observableArrayList(newDoctors);

        nameNew.setCellValueFactory(doctor -> new SimpleStringProperty(doctor.getValue().getName()));
        surnameNew.setCellValueFactory(doctor -> new SimpleStringProperty(doctor.getValue().getSurname()));
        genderNew.setCellValueFactory(doctor -> new SimpleStringProperty(doctor.getValue().getGender()));
        titleNew.setCellValueFactory(doctor -> new SimpleStringProperty(doctor.getValue().getTitle()));
        roomNew.setCellValueFactory(doctor -> new SimpleStringProperty(doctor.getValue().getRoom()));


        newTable.setItems(observableList);

    }

}
