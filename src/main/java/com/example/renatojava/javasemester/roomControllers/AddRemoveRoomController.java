package com.example.renatojava.javasemester.roomControllers;

import com.example.renatojava.javasemester.entity.Data;
import com.example.renatojava.javasemester.entity.Doctor;
import com.example.renatojava.javasemester.entity.DoctorRoom;
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

public class AddRemoveRoomController {

    @FXML
    private TableView<DoctorRoom> roomTable;

    @FXML
    private TableColumn<DoctorRoom, String> roomNameColumn, doctorColumn;

    @FXML
    private TextField nameField;

    @FXML
    private ChoiceBox<String> doctorChoice;

    Set<Doctor> allDoctors;

    public void initialize(){
        try{
            allDoctors = Data.getAllDoctors();

            ObservableList<String> doctorsList = FXCollections.observableArrayList(allDoctors.stream().map(Doctor::getDoctorFullName).collect(Collectors.toList()));
            doctorChoice.setItems(doctorsList);
            fillRoomTable();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void fillRoomTable(){
        List<DoctorRoom> allDoctorRooms = Data.getAllRooms();

        ObservableList<DoctorRoom> observableList = FXCollections.observableArrayList(allDoctorRooms);

        roomNameColumn.setCellValueFactory(room -> new SimpleStringProperty(room.getValue().getRoomName()));

        doctorColumn.setCellValueFactory(room -> new SimpleStringProperty(Data.getCertainDoctor(room.getValue().getDoctorID()).getDoctorFullName()));

        roomTable.setItems(observableList);
    }
    public void removeRoom(){
        if(Data.confirmEdit()){
            DoctorRoom oldDoctorRoom = roomTable.getSelectionModel().getSelectedItem();
            Data.unlinkRoomFromDoctor(oldDoctorRoom);
            Data.removeRoom(oldDoctorRoom.getRoomID());
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
        }else if(Data.hasDoctorRoom(allDoctors.stream().filter(doctor -> doctor.getDoctorFullName().equals(doctorChoice.getSelectionModel().getSelectedItem())).toList().get(0).getId())){
            errorMessages.add("Doctor already has a room!");
        }

        if(errorMessages.size() > 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            String error = errorMessages.stream().collect(Collectors.joining("\n"));
            alert.setHeaderText(error);
            alert.show();
            return;
        }

        if(!Data.confirmEdit()){
            Alert failure = new Alert(Alert.AlertType.ERROR);
            failure.setTitle("ERROR");
            failure.setHeaderText("Failure!");
            failure.setContentText("Room is not added to the system!");
            failure.show();
        }else{
            Doctor selectedDoctor = allDoctors.stream().filter(doctor -> doctor.getDoctorFullName().equals(doctorChoice.getSelectionModel().getSelectedItem())).toList().get(0);
            Data.addRoom(nameField.getText(), selectedDoctor.getId());
            initialize();
            clearFields();
        }

    }

    public void clearFields(){
        nameField.setText("");
        doctorChoice.getSelectionModel().clearSelection();
    }

}
