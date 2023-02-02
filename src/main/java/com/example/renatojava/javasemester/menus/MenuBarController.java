package com.example.renatojava.javasemester.menus;

import com.example.renatojava.javasemester.entity.User;
import com.example.renatojava.javasemester.Application;
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
    private MenuItem addCheckup, removeCheckup, registerPatient, allPatients, allDoctors, procedures, addRemoveDoctors, editDoctors, addRemoveRoom, activeProcedures, doctorChanges, patientChanges, procedureChanges, createProcedure, deleteProcedure, editProcedure, roomChanges;

    public void initialize(){
        User currentUser = Application.getLoggedUser();
        if(currentUser.getRole().equals("Doctor")){
            addCheckup.setDisable(true);
            removeCheckup.setDisable(true);
            registerPatient.setDisable(true);
            addRemoveDoctors.setDisable(true);
            editDoctors.setDisable(true);
            addRemoveRoom.setDisable(true);
            doctorChanges.setDisable(true);
            patientChanges.setDisable(true);
            roomChanges.setDisable(true);
            procedureChanges.setDisable(true);
            createProcedure.setDisable(true);
            deleteProcedure.setDisable(true);
            editProcedure.setDisable(true);
        }else if(currentUser.getRole().equals("Receptionist")){
            addRemoveDoctors.setDisable(true);
            editDoctors.setDisable(true);
            addRemoveRoom.setDisable(true);
            doctorChanges.setDisable(true);
            patientChanges.setDisable(true);
            procedureChanges.setDisable(true);
            roomChanges.setDisable(true);
            createProcedure.setDisable(true);
            deleteProcedure.setDisable(true);
            editProcedure.setDisable(true);
        }
    }



    public void showEditCheckupScreen() {
        BorderPane root;
        try {
            root = FXMLLoader.load(
                    getClass().getResource("/fxml/editCheckups.fxml"));
            Application.setMainPage(root);
        } catch (IOException e) {
            Application.logger.error(e.getMessage(), e);
        }
    }

    public void showPricesScreen() {
        BorderPane root;
        try {
            root = FXMLLoader.load(
                    getClass().getResource("/fxml/proceduresScreen.fxml"));
            Application.setMainPage(root);
        } catch (IOException e) {
            Application.logger.error(e.getMessage(), e);
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
                    getClass().getResource("/fxml/registerPatientScreen.fxml"));
            Application.setMainPage(root);
        } catch (IOException e) {
            Application.logger.error(e.getMessage(), e);
        }
    }

    public void showAllPatientsScreen() {
        BorderPane root;
        try {
            root = FXMLLoader.load(
                    getClass().getResource("/fxml/allPatientsScreen.fxml"));
            Application.setMainPage(root);
        } catch (IOException e) {
            Application.logger.error(e.getMessage(), e);
        }
    }

    public void logout() {
        Application.setLoggedUser(null);

        BorderPane root;
        try {
            root = FXMLLoader.load(
                    getClass().getResource("/fxml/loginScreen.fxml"));
            Application.setMainPage(root);
        } catch (IOException e) {
            Application.logger.error(e.getMessage(), e);
        }
    }

    public void showAddProcedureScreen() {
        BorderPane root;
        try {
            root = FXMLLoader.load(
                    getClass().getResource("/fxml/addProcedures.fxml"));
            Application.setMainPage(root);
        } catch (IOException e) {
            Application.logger.error(e.getMessage(), e);
        }
    }

    public void showRemoveProcedureScreen() {
        BorderPane root;
        try {
            root = FXMLLoader.load(
                    getClass().getResource("/fxml/removeProcedure.fxml"));
            Application.setMainPage(root);
        } catch (IOException e) {
            Application.logger.error(e.getMessage(), e);
        }
    }

    public void showAllDoctorsScreen() {
        BorderPane root;
        try {
            root = FXMLLoader.load(
                    getClass().getResource("/fxml/allDoctors.fxml"));
            Application.setMainPage(root);
        } catch (IOException e) {
            Application.logger.error(e.getMessage(), e);
        }
    }

    public void showChangesPatientsScreen() {
        BorderPane root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/changesPatientsScreen.fxml"));
            Application.setMainPage(root);
        }catch (IOException e){
            Application.logger.error(e.getMessage(), e);
        }
    }

    public void showStatsScreen(){
        BorderPane root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/menuScreen.fxml"));
            Application.setMainPage(root);
        }catch (IOException e){
            Application.logger.error(e.getMessage(), e);
        }
    }

    public void showAddRemoveDoctorsScreen(){
        BorderPane root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/addRemoveDoctors.fxml"));
            Application.setMainPage(root);
        }catch (IOException e){
            Application.logger.error(e.getMessage(), e);
        }
    }

    public void showEditDoctorsScreen(){
        BorderPane root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/editDoctors.fxml"));
            Application.setMainPage(root);
        }catch (IOException e){
            Application.logger.error(e.getMessage(), e);
        }
    }

    public void showChangesDoctorsScreen(){
        BorderPane root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/changesDoctorsScreen.fxml"));
            Application.setMainPage(root);
        }catch (IOException e){
            Application.logger.error(e.getMessage(), e);
        }
    }

    public void showAddRemoveRoomScreen(){
        BorderPane root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/addRemoveRoom.fxml"));
            Application.setMainPage(root);
        }catch (IOException e){
            Application.logger.error(e.getMessage(), e);
        }
    }

    public void showAddCheckupScreen(){
        BorderPane root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/addCheckup.fxml"));
            Application.setMainPage(root);
        }catch (IOException e){
            System.out.println(e);
        }
    }

    public void showAllCheckupsScreen(){
        BorderPane root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/listOfActiveCheckups.fxml"));
            Application.setMainPage(root);
        }catch (IOException e){
            Application.logger.error(e.getMessage(), e);
        }

    }

    public void showAllBillsScreen(){
        BorderPane root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/allBills.fxml"));
            Application.setMainPage(root);
        }catch (IOException e){
            Application.logger.error(e.getMessage(), e);
        }

    }

    public void showChangesProceduresScreen(){
        BorderPane root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/changesProceduresScreen.fxml"));
            Application.setMainPage(root);
        }catch (IOException e){
            Application.logger.error(e.getMessage(), e);
        }
    }

    public void showCreateProcedureScreen(){
        BorderPane root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/createProcedureScreen.fxml"));
            Application.setMainPage(root);
        }catch (IOException e){
            Application.logger.error(e.getMessage(), e);
        }
    }

    public void showDeleteProcedureScreen(){
        BorderPane root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/deleteProcedureScreen.fxml"));
            Application.setMainPage(root);
        }catch (IOException e){
            Application.logger.error(e.getMessage(), e);
        }
    }

    public void showEditProcedureScreen(){
        BorderPane root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/editProcedureScreen.fxml"));
            Application.setMainPage(root);
        }catch (IOException e){
            Application.logger.error(e.getMessage(), e);
        }
    }

    public void showRoomChangesScreen(){
        BorderPane root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/changesRoomScreen.fxml"));
            Application.setMainPage(root);
        }catch (IOException e){
            Application.logger.error(e.getMessage(), e);
        }
    }

    public void showCheckupChangesScreen(){
        BorderPane root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/changesCheckupsScreen.fxml"));
            Application.setMainPage(root);
        }catch (IOException e){
            Application.logger.error(e.getMessage(), e);
        }
    }

    public void showCreateUserScreen(){
        BorderPane root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/createUserScreen.fxml"));
            Application.setMainPage(root);
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public void showEditUserScreen(){
        BorderPane root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/editUserScreen.fxml"));
            Application.setMainPage(root);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void showUserChangesScreen(){
        BorderPane root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/changesUsersScreen.fxml"));
            Application.setMainPage(root);
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
