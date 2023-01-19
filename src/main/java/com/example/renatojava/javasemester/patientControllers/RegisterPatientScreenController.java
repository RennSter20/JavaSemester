package com.example.renatojava.javasemester.patientControllers;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.entity.CheckObjects;
import com.example.renatojava.javasemester.entity.Data;
import com.example.renatojava.javasemester.entity.Patient;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class RegisterPatientScreenController implements CheckObjects {

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
        String regex = "^[0-9]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(oib);

        List<String> errorMessages = new ArrayList<>();
        if(name.equals("") || surname.equals("")){
            errorMessages.add("Name and surname field cannot be empty!");
        }
        if(gender == null){
            errorMessages.add("Gender must be selected!");
        }
        if(oib.length() != 10 || !m.matches()){
            errorMessages.add("OIB must have 10 numeric characters.");
        }
        if(datePicker.getValue() == null || datePicker.getValue().isAfter(LocalDate.now())){
            errorMessages.add("Valid date of birth must be selected!");
        }else{
            date = datePicker.getValue();
        }
        if(errorMessages.size() > 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            String error = errorMessages.stream().collect(Collectors.joining("\n"));
            alert.setHeaderText(error);
            alert.show();
            return;
        }
            try{
                if(!Data.confirmEdit()){
                    Alert failure = new Alert(Alert.AlertType.ERROR);
                    failure.setTitle("ERROR");
                    failure.setHeaderText("Failure!");
                    failure.setContentText("Patient is not added to the system!");
                    failure.show();
                }else{

                    Data.addPatient(name, surname, gender, oib, date);
                }

            }catch (SQLException e){
                Application.logger.info("Message: " + e.getMessage() + " Stack trace: " + e.getStackTrace());
            } catch (IOException e) {
                Application.logger.info("Message: " + e.getMessage() + " Stack trace: " + e.getStackTrace());
            }
    }
    }
