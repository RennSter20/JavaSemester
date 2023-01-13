package com.example.renatojava.javasemester;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Optional;

public class MenuBarController {

    public void showPricesScreen() {
        BorderPane root;
        try {
            root = FXMLLoader.load(
                    getClass().getResource("proceduresScreen.fxml"));
            Application.setMainPage(root);
        } catch (IOException e) {
            Application.logger.info("Message: " + e.getMessage() + " Stack trace: " + e.getStackTrace());
        }
    }

    public void exitApplication(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to exit application?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            System.exit(0);
        }


    }

    public void showRegisterPatientScreen() {
        BorderPane root;
        try {
            root = FXMLLoader.load(
                    getClass().getResource("registerPatientScreen.fxml"));
            Application.setMainPage(root);
        } catch (IOException e) {
            Application.logger.info("Message: " + e.getMessage() + " Stack trace: " + e.getStackTrace());
        }
    }

    public void showAllPatientsScreen() {
        BorderPane root;
        try {
            root = FXMLLoader.load(
                    getClass().getResource("allPatientsScreen.fxml"));
            Application.setMainPage(root);
        } catch (IOException e) {
            Application.logger.info("Message: " + e.getMessage() + " Stack trace: " + e.getStackTrace());
        }
    }

    public void logout() {
        Application.setLoggedUser(null);

        BorderPane root;
        try {
            root = FXMLLoader.load(
                    getClass().getResource("loginScreen.fxml"));
            Application.setMainPage(root);
        } catch (IOException e) {
            Application.logger.info("Message: " + e.getMessage() + " Stack trace: " + e.getStackTrace());
        }
    }

    public void showManageCheckupsScreen() {
        BorderPane root;
        try {
            root = FXMLLoader.load(
                    getClass().getResource("addProcedure.fxml"));
            Application.setMainPage(root);
        } catch (IOException e) {
            Application.logger.info("Message: " + e.getMessage() + " Stack trace: " + e.getStackTrace());
        }
    }

    public void showRemoveCheckupScreen() {
        BorderPane root;
        try {
            root = FXMLLoader.load(
                    getClass().getResource("removeProcedure.fxml"));
            Application.setMainPage(root);
        } catch (IOException e) {
            Application.logger.info("Message: " + e.getMessage() + " Stack trace: " + e.getStackTrace());
        }
    }

    public void showAllDoctorsScreen() {
        BorderPane root;
        try {
            root = FXMLLoader.load(
                    getClass().getResource("allDoctors.fxml"));
            Application.setMainPage(root);
        } catch (IOException e) {
            Application.logger.info("Message: " + e.getMessage() + " Stack trace: " + e.getStackTrace());
        }
    }
}
