package com.example.renatojava.javasemester.entity;

import javafx.scene.control.Alert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Validator {

    static Boolean isOibValid(String oib){
        String regex = "^[0-9]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(oib);
        if(m.matches()){
            return true;
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INFORMATION");
            alert.setHeaderText("Problem with setting new info!");
            alert.setContentText("NOTE:\nName and surname cannot be empty!\nOib must contain 10 numeric characters!");
            alert.show();
            return false;
        }
    }
    static Boolean isNameValid(String s){
        if(!s.equals("")){
            return true;
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INFORMATION");
            alert.setHeaderText("Problem with setting new info!");
            alert.setContentText("NOTE:\nName and surname cannot be empty!\nOib must contain 10 numeric characters!");
            alert.show();
            return false;
        }
    }

}
