package com.example.renatojava.javasemester.util;

import com.example.renatojava.javasemester.patient.AllPatientsScreenController;
import javafx.scene.control.Alert;

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
        String regex = "^[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(s);
        if(m.matches()){
            return true;
        }else{
            return false;
        }
    }

}
