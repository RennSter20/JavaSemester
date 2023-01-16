package com.example.renatojava.javasemester;

import com.example.renatojava.javasemester.entity.Data;
import com.example.renatojava.javasemester.entity.Doctor;
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

public class EditDoctorsController {

    @FXML
    private TextField nameField, surnameField, titleField, roomField;
    @FXML
    private RadioButton maleRadio, femaleRadio;
    @FXML
    private TableView<Doctor> doctorTable;
    @FXML
    private TableColumn<Doctor, String> nameColumn, surnameColumn, genderColumn, titleColumn, roomColumn;
    public void initialize(){
        try{
            fillDoctorTable(Data.getAllDoctors());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void addDoctor(){
        String name = nameField.getText();
        String surname = surnameField.getText();
        String title = titleField.getText();
        String room = roomField.getText();
        String gender = "";

        if(maleRadio.isSelected()){
            gender = "M";
        }if(femaleRadio.isSelected()){
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

        if(!Data.confirmEdit()){
            Alert failure = new Alert(Alert.AlertType.ERROR);
            failure.setTitle("ERROR");
            failure.setHeaderText("Failure!");
            failure.setContentText("Doctor is not added to the system!");
            failure.show();
        }else{
            Data.addDoctor(new Doctor.Builder().withName(name).withSurname(surname).withTitle(title).withRoom(room).withGender(gender).build());
            initialize();
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
            if(!Data.confirmEdit()){
                Alert failure = new Alert(Alert.AlertType.ERROR);
                failure.setTitle("ERROR");
                failure.setHeaderText("Failure!");
                failure.setContentText("Doctor is not removed from the system!");
                failure.show();
            }else{
                Data.removeDoctor(doctorTable.getSelectionModel().getSelectedItem());
                initialize();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
