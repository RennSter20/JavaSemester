package com.example.renatojava.javasemester;

import com.example.renatojava.javasemester.entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Optional;

public class MenuBarController {

    @FXML
    private MenuItem addCheckup, removeCheckup, registerPatient, allPatients, allDoctors, makeBill, procedures, allChanges, editDoctors;

    public void initialize(){
        User currentUser = Application.getLoggedUser();
        if(currentUser.getRole().equals("Doctor")){
            addCheckup.setDisable(true);
            removeCheckup.setDisable(true);
            registerPatient.setDisable(true);
            makeBill.setDisable(true);
            allChanges.setDisable(true);
            editDoctors.setDisable(true);
        }else if(currentUser.getRole().equals("Receptionist")){
            allChanges.setDisable(true);
            editDoctors.setDisable(true);
        }
    }

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
            Application.logger.info("Message: " + e.getMessage() + " Stack trace: " + e);
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

    public void showAddProcedureScreen() {
        BorderPane root;
        try {
            root = FXMLLoader.load(
                    getClass().getResource("addCheckup.fxml"));
            Application.setMainPage(root);
        } catch (IOException e) {
            Application.logger.info("Message: " + e.getMessage() + " Stack trace: " + e.getStackTrace());
        }
    }

    public void showRemoveProcedureScreen() {
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

    public void showChangesPatientsScreen() {
        BorderPane root;
        try {
            root = FXMLLoader.load(getClass().getResource("changesPatientsScreen.fxml"));
            Application.setMainPage(root);
        }catch (IOException e){
            Application.logger.info("Message: " + e.getMessage() + " Stack trace: " + e.getStackTrace());
        }
    }

    public void showStatsScreen(){
        BorderPane root;
        try {
            root = FXMLLoader.load(getClass().getResource("menuScreen.fxml"));
            Application.setMainPage(root);
        }catch (IOException e){
            Application.logger.info("Message: " + e.getMessage() + " Stack trace: " + e.getStackTrace());
        }
    }

    public void showEditDoctorsScreen(){
        BorderPane root;
        try {
            root = FXMLLoader.load(getClass().getResource("editDoctors.fxml"));
            Application.setMainPage(root);
        }catch (IOException e){
            Application.logger.info("Message: " + e.getMessage() + " Stack trace: " + e.getStackTrace());
        }
    }

    public void showChangesDoctorsScreen(){
        BorderPane root;
        try {
            root = FXMLLoader.load(getClass().getResource("changesDoctorsScreen.fxml"));
            Application.setMainPage(root);
        }catch (IOException e){
            Application.logger.info("Message: " + e.getMessage() + " Stack trace: " + e.getStackTrace());
        }
    }
}
