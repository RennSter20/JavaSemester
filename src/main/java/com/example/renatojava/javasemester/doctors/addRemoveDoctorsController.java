package com.example.renatojava.javasemester.doctors;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.database.DoctorData;
import com.example.renatojava.javasemester.database.DoctorRoomData;
import com.example.renatojava.javasemester.entity.Doctor;
import com.example.renatojava.javasemester.util.Notification;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class addRemoveDoctorsController implements DoctorData, Notification, DoctorRoomData {

    @FXML
    private TextField nameField, surnameField, titleField;
    @FXML
    private RadioButton maleRadio, femaleRadio;
    @FXML
    private TableView<Doctor> doctorTable;
    @FXML
    private TableColumn<Doctor, String> nameColumn, surnameColumn, genderColumn, titleColumn, roomColumn;
    public void initialize(){
        try{
            fillDoctorTable(DoctorData.getAllDoctors());
        } catch (SQLException e) {
            Application.logger.error(e.getMessage(), e);
        } catch (IOException e) {
            Application.logger.error(e.getMessage(), e);
        }
    }
    public void addDoctor(){
        String name = nameField.getText();
        String surname = surnameField.getText();
        String title = titleField.getText();
        String room = "Not yet set";
        String gender = "";

        if(maleRadio.isSelected()){
            gender = "M";
        }else if(femaleRadio.isSelected()){
            gender = "F";
        }

        List<String> errorMessages = new ArrayList<>();
        if(name.equals("") || surname.equals("") || title.equals("")){
            errorMessages.add("Name, surname and title field cannot be empty!");
        }
        if(gender.equals("")){
            errorMessages.add("Gender must be selected!");
        }
        if(errorMessages.size() > 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            String error = errorMessages.stream().collect(Collectors.joining("\n"));
            alert.setHeaderText(error);
            alert.show();
            return;
        }

        if(!Notification.confirmEdit()){
            Alert failure = new Alert(Alert.AlertType.ERROR);
            failure.setTitle("ERROR");
            failure.setHeaderText("Failure!");
            failure.setContentText("Doctor is not added to the system!");
            failure.show();
        }else{
            DoctorData.addDoctor(new Doctor.Builder().withName(name).withSurname(surname).withTitle(title).withRoom(room).withGender(gender).build());
            initialize();
            clearFields();
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

    public void removeDoctor(){
        try{
            if(!Notification.confirmEdit()){
                Alert failure = new Alert(Alert.AlertType.ERROR);
                failure.setTitle("ERROR");
                failure.setHeaderText("Failure!");
                failure.setContentText("Doctor is not removed from the system!");
                failure.show();
            }else{
                DoctorRoomData.unlinkDoctorFromRoom(doctorTable.getSelectionModel().getSelectedItem().getRoom());
                DoctorData.removeDoctor(doctorTable.getSelectionModel().getSelectedItem());
                initialize();
            }
        } catch (SQLException e) {
            Application.logger.error(e.getMessage(), e);
        } catch (IOException e) {
            Application.logger.error(e.getMessage(), e);
        }
    }

    public void clearFields(){
        nameField.setText("");
        surnameField.setText("");
        titleField.setText("");
        maleRadio.setSelected(false);
        femaleRadio.setSelected(false);
    }

}
