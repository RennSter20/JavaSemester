package com.example.renatojava.javasemester;

import com.example.renatojava.javasemester.entity.CheckObjects;
import com.example.renatojava.javasemester.entity.Data;
import com.example.renatojava.javasemester.entity.Patient;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterPatientScreenController implements CheckObjects {

    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField oibField;
    @FXML
    private RadioButton maleRadio, femaleRadio;

    @FXML
    public void registerPatient() {
        String name = nameField.getText();
        String surname = surnameField.getText();
        String gender;

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
        if(errorMessages.size() > 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            String error = "";
            for(String s : errorMessages){
                error = error + s + "\n";
            }
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

                    Data.addPatient(name, surname, gender, oib);
                }

            }catch (SQLException e){
                Application.logger.info(String.valueOf(e.getStackTrace()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }
    }
