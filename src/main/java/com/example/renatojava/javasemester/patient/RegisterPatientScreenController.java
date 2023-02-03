package com.example.renatojava.javasemester.patient;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.database.PatientData;
import com.example.renatojava.javasemester.util.CheckObjects;
import com.example.renatojava.javasemester.util.Notification;
import com.example.renatojava.javasemester.util.Validator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class RegisterPatientScreenController implements CheckObjects, Notification, PatientData {

    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField oibField;
    @FXML
    private RadioButton maleRadio, femaleRadio;
    @FXML
    private DatePicker datePicker;

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    public void registerPatient() {
        String name = nameField.getText();
        String surname = surnameField.getText();
        String gender;
        LocalDate date = null;

        if(maleRadio.isSelected()){
            gender = "M";
        }else if(femaleRadio.isSelected()){
            gender = "F";
        }else{
            gender = null;
        }

        String oib = oibField.getText();

        List<String> errorMessages = new ArrayList<>();
        if(!Validator.isOibValid(oib)){
            errorMessages.add("OIB must have 10 numeric characters.");
        }

        if(!Validator.isNameValid(name) || !Validator.isNameValid(surname)){
            errorMessages.add("Name and surname field cannot be empty and need to containt only alphabetic characters.");
        }
        if(gender == null){
            errorMessages.add("Gender must be selected!");
        }

        if(datePicker.getValue() == null || datePicker.getValue().isAfter(LocalDate.now()) || !Validator.isBirthDateValid(datePicker.getValue().toString(), DATE_TIME_FORMAT)){
            errorMessages.add("Valid date of birth must be selected!");
        }else{
            date = datePicker.getValue();
        }
        if(errorMessages.size() > 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            String error = errorMessages.stream().collect(Collectors.joining("\n"));
            alert.setHeaderText(error);
            alert.show();
            return;
        }
            try{
                if(!Notification.confirmEdit()){
                    Alert failure = new Alert(Alert.AlertType.ERROR);
                    failure.setTitle("ERROR");
                    failure.setHeaderText("Failure!");
                    failure.setContentText("Patient is not added to the system!");
                    failure.show();
                }else{
                    PatientData.addPatient(name, surname, gender, oib, date);
                    clearFields();
                }

            }catch (SQLException | IOException e){
                Application.logger.error(e.getMessage(), e);
            }
    }

    public void clearFields(){
        nameField.setText("");
        surnameField.setText("");
        oibField.setText("");
        maleRadio.setSelected(false);
        femaleRadio.setSelected(false);
        datePicker.setValue(null);
    }

}
