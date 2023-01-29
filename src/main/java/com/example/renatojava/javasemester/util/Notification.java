package com.example.renatojava.javasemester.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public interface Notification {

    static void addedSuccessfully(String type){
        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setTitle("INFORMATION");
        success.setHeaderText("Success!");
        success.setContentText(type + " successfully added to the system!");
        success.show();
    }
    static void removedSuccessfully(String type){
        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setTitle("INFORMATION");
        success.setHeaderText("Success!");
        success.setContentText(type + " successfully removed from the system!");
        success.show();
    }
    static void updatedSuccessfully(String type){
        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setTitle("INFORMATION");
        success.setHeaderText("Success!");
        success.setContentText(type + " successfully updated in system!");
        success.show();
    }

    static Boolean confirmEdit(){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRMATION");
        alert.setHeaderText("Are you sure you want to make this change in system?");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
        alert.setContentText("");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            return true;
        }else{
            return false;
        }
    }

}
