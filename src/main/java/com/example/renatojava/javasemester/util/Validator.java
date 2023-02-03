package com.example.renatojava.javasemester.util;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.patient.AllPatientsScreenController;
import javafx.scene.control.Alert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public sealed interface Validator permits AllPatientsScreenController {

    static Boolean isOibValid(String oib){
        String regex = "^[0-9]{10}+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(oib);
        if(m.matches()){
            return true;
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Problem with setting new info!");
            alert.setContentText("NOTE:\nName and surname cannot be empty!\nOib must contain 10 numeric characters!");
            alert.show();
            return false;
        }
    }
    static Boolean isNameValid(String s){
        String regex = "^[a-zA-Z\s]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(s);
        if(m.matches()){
            return true;
        }else{
            return false;
        }
    }

    static boolean isValidTime(String input, DateTimeFormatter format) {
        try {
            LocalDate time = LocalDate.parse(input, format);
            return true;
        } catch (DateTimeParseException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Error while getting new date and time.");
            alert.setContentText("Please type date and time of checkup in valid format: yyyy-MM-dd HH:ss");
            alert.show();
            Application.logger.error(e.getMessage(), e);
            return false;
        }
    }

    static boolean isBirthDateValid(String input, DateTimeFormatter format){
        try {
            LocalDate time = LocalDate.parse(input, format);
            return true;
        } catch (DateTimeParseException e) {
            Application.logger.error(e.getMessage(), e);
            return false;
        }
    }

}
