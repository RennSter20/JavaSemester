package com.example.renatojava.javasemester;

import com.example.renatojava.javasemester.entity.CheckObjects;
import com.example.renatojava.javasemester.entity.Patient;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

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
    public void registerPatient() throws SQLException {
        String name = nameField.getText();
        String surname = surnameField.getText();
        String gender;
        if(maleRadio.isSelected()){
            gender = "M";
        }else{
            gender = "F";
        }

        String oib = oibField.getText();
        String regex = "^[0-9]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(oib);

        if(oib.length() != 10 || !m.matches()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText("OIB must have 10 numeric characters.");
            alert.show();
        }else{
            Connection veza = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/production", "student", "student");

            PreparedStatement stmnt = veza.prepareStatement("INSERT INTO PATIENTS(NAME, SURNAME, GENDER, DEBT, PROCEDURES, OIB) VALUES(?,?,?,?,?,?)");
            stmnt.setString(1, name);
            stmnt.setString(2, surname);
            stmnt.setString(3, gender);
            stmnt.setString(4, "0");
            stmnt.setString(5, "");
            stmnt.setString(6, oib);

            if(!CheckObjects.checkIfPatientExists(oib)){
                stmnt.executeUpdate();
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Info");
                alert.setHeaderText("Patient already exists in system.");
                alert.show();
            }
            veza.close();
        }
    }



}
