package com.example.renatojava.javasemester.room;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.database.DoctorData;
import com.example.renatojava.javasemester.database.DoctorRoomData;
import com.example.renatojava.javasemester.entity.Doctor;
import com.example.renatojava.javasemester.entity.DoctorRoom;
import com.example.renatojava.javasemester.util.Notification;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AddRemoveRoomController implements DoctorData, DoctorRoomData, Notification {

    @FXML
    private TableView<DoctorRoom> roomTable;

    @FXML
    private TableColumn<DoctorRoom, String> roomNameColumn, doctorColumn;

    @FXML
    private TextField nameField;

    @FXML
    private ChoiceBox<String> doctorChoice;

    Set<Doctor> allDoctors;
    @FXML
    public void initialize(){
        try{
            allDoctors = DoctorData.getAllDoctors();

            ObservableList<String> doctorsList = FXCollections.observableArrayList(allDoctors.stream().map(Doctor::getDoctorFullName).collect(Collectors.toList()));
            doctorChoice.setItems(doctorsList);
            fillRoomTable();

        } catch (SQLException e) {
            Application.logger.error(e.getMessage(), e);
        } catch (IOException e) {
            Application.logger.error(e.getMessage(), e);
        }
    }

    public void fillRoomTable(){
        List<DoctorRoom> allDoctorRooms = DoctorRoomData.getAllRooms();

        ObservableList<DoctorRoom> observableList = FXCollections.observableArrayList(allDoctorRooms);

        roomNameColumn.setCellValueFactory(room -> new SimpleStringProperty(room.getValue().getRoomName()));

        doctorColumn.setCellValueFactory(room -> new SimpleStringProperty(DoctorData.getCertainDoctor(room.getValue().getDoctorID()).getDoctorFullName()));

        roomTable.setItems(observableList);
    }
    public void removeRoom(){
        if(Notification.confirmEdit()){
            DoctorRoom oldDoctorRoom = roomTable.getSelectionModel().getSelectedItem();
            DoctorRoomData.unlinkRoomFromDoctor(oldDoctorRoom);
            DoctorRoomData.removeRoom(oldDoctorRoom.getRoomID());
            initialize();
        }else{
            Alert failure = new Alert(Alert.AlertType.ERROR);
            failure.setTitle("ERROR");
            failure.setHeaderText("Failure!");
            failure.setContentText("Room is not removed from the system!");
            failure.show();
        }
    }

    public void addRoom(){
        List<String> errorMessages = new ArrayList<>();
        if(nameField.getText().equals("")){
            errorMessages.add("Name field cannot be empty!");
        }
        if(allDoctors.size() == 0){
            errorMessages.add("Please enter a doctor in the system first!");
        }else
        if(doctorChoice.getSelectionModel().getSelectedItem() == null){
            errorMessages.add("Please select a doctor from dropdown list!");
        }else if(DoctorRoomData.hasDoctorRoom(allDoctors.stream().filter(doctor -> doctor.getDoctorFullName().equals(doctorChoice.getSelectionModel().getSelectedItem())).toList().get(0).getId())){
            errorMessages.add("Doctor already has a room!");
        }

        if(errorMessages.size() > 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            String error = errorMessages.stream().collect(Collectors.joining("\n"));
            alert.setHeaderText(error);
            alert.show();
            return;
        }

        if(!Notification.confirmEdit()){
            Alert failure = new Alert(Alert.AlertType.ERROR);
            failure.setTitle("ERROR");
            failure.setHeaderText("Failure!");
            failure.setContentText("Room is not added to the system!");
            failure.show();
        }else{
            Doctor selectedDoctor = allDoctors.stream().filter(doctor -> doctor.getDoctorFullName().equals(doctorChoice.getSelectionModel().getSelectedItem())).toList().get(0);
            DoctorRoomData.addRoom(nameField.getText(), selectedDoctor.getId());
            initialize();
            clearFields();
        }

    }

    public void clearFields(){
        nameField.setText("");
        doctorChoice.getSelectionModel().clearSelection();
    }

}
