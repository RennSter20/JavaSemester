package com.example.renatojava.javasemester.doctors;

import com.example.renatojava.javasemester.entity.ChangeWriter;
import com.example.renatojava.javasemester.entity.Doctor;
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

public class ChangesDoctorsController {

    @FXML
    private TableView<Doctor> oldTable, newTable;
    @FXML
    private TableColumn<Doctor, String> nameOld, nameNew, surnameOld, surnameNew, genderOld, genderNew, titleOld, titleNew, roomOld, roomNew;

    @FXML
    private Text changeTimeText,role;
    ChangeWriter changer = new ChangeWriter();

    List<String> changesTime;
    List<String> rolesList;
    Integer selectedDoctor;

    public void initialize(){
        List<Doctor> doctorsList = changer.readDoctors();

        changesTime = changer.readTimeDoctors();
        rolesList = changer.readRoleChangeDoctors();

        List<Doctor> oldDoctor = new ArrayList<>();
        List<Doctor> newDoctors = new ArrayList<>();

        for(int i = 0;i<doctorsList.size();i+=2){
            oldDoctor.add(doctorsList.get(i));
        }
        for(int i = 1;i<doctorsList.size();i+=2){
            newDoctors.add(doctorsList.get(i));
        }

        fillOldTable(oldDoctor);
        fillNewTable(newDoctors);
    }
    public void showTimeOld(){
        if(oldTable.getSelectionModel().getSelectedItem() != null){
            selectedDoctor = oldTable.getSelectionModel().getSelectedIndex();
            newTable.getSelectionModel().select(oldTable.getSelectionModel().getFocusedIndex());
            changeTimeText.setText(changesTime.get(selectedDoctor));
            role.setText("by role " + rolesList.get(oldTable.getSelectionModel().getSelectedIndex()));
        }
    }

    public void showTimeNew(){
        if(newTable.getSelectionModel().getSelectedItem() != null){
            selectedDoctor = newTable.getSelectionModel().getSelectedIndex();
            oldTable.getSelectionModel().select(newTable.getSelectionModel().getFocusedIndex());
            changeTimeText.setText(changesTime.get(selectedDoctor));
            role.setText("by role " + rolesList.get(newTable.getSelectionModel().getSelectedIndex()));
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

    public void moreInfo(){
        Optional<Doctor> selOldDoctor = Optional.ofNullable(oldTable.getSelectionModel().getSelectedItem());
        Optional<Doctor> selNewDoctor = Optional.ofNullable(newTable.getSelectionModel().getSelectedItem());

        if(selOldDoctor.isPresent() || selNewDoctor.isPresent()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INFORMATION");
            alert.setHeaderText("More info about doctor change.");
            alert.setContentText("OLD VALUE:\n" + selOldDoctor.get() + "\n\nNEW VALUE:\n" + selNewDoctor.get());
            alert.show();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("No doctor change selected!");
            alert.setContentText("Please select doctor to show more info!");
            alert.show();
        }
    }

}
