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
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class EditRoomController {

    @FXML
    private TableView<DoctorRoom> roomTable;

    @FXML
    private TableColumn<DoctorRoom, String> roomNameColumn, doctorColumn;

    @FXML
    private TextField nameField;

    @FXML
    private ChoiceBox<String> doctorChoice;
    @FXML
    private TextField searchField;

    private Set<Doctor> allDoctors;

    @FXML
    public void initialize(){
        try{
            allDoctors = DoctorData.getAllDoctors();

            ObservableList<String> doctorsList = FXCollections.observableArrayList(allDoctors.stream().map(Doctor::getDoctorFullName).collect(Collectors.toList()));
            doctorChoice.setItems(doctorsList);
            fillRoomTable(DoctorRoomData.getAllRooms());

        } catch (SQLException | IOException e) {
            Application.logger.error(e.getMessage(), e);
        }
    }

    public void search(){
        String filter = searchField.getText();

        List<DoctorRoom> filteredRooms;

        filteredRooms = DoctorRoomData.getAllRooms().stream().filter(room -> room.getRoomName().toLowerCase().contains(filter.toLowerCase()) ||
                room.getDoctorName().toLowerCase().contains(filter.toLowerCase())).collect(Collectors.toList());

        fillRoomTable(filteredRooms);
    }
    public void fillRoomTable(List<DoctorRoom> rooms){

        ObservableList<DoctorRoom> observableList = FXCollections.observableArrayList(rooms);

        roomNameColumn.setCellValueFactory(room -> new SimpleStringProperty(room.getValue().getRoomName()));
        doctorColumn.setCellValueFactory(room -> new SimpleStringProperty(DoctorData.getCertainDoctorFromId(room.getValue().getDoctorID()).getDoctorFullName()));

        roomTable.setItems(observableList);
    }

    public void apply(){
        Optional<DoctorRoom> selectedRoom = Optional.ofNullable(roomTable.getSelectionModel().getSelectedItem());
        Optional<Doctor> newDoctor = allDoctors.stream().filter(doctor -> doctor.getDoctorFullName().contains(doctorChoice.getSelectionModel().getSelectedItem())).findAny();

        if(selectedRoom.isPresent()){
            if(!nameField.equals("") && !doctorChoice.getSelectionModel().getSelectedItem().equals("-1") && Notification.confirmEdit()){
                DoctorRoomData.updateRoom(nameField.getText(), newDoctor.get(), selectedRoom.get());
                nameField.setText("");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("INFORMATION");
                alert.setHeaderText("Room updated successfully.");
                alert.show();

                initialize();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Error with updating room info.");
                alert.setContentText("Room name cannot be empty!");
                alert.show();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Error with updating room info.");
            alert.setContentText("No room selected!");
            alert.show();
        }
    }

    public void fillFields(){
        Optional<DoctorRoom> selectedRoom = Optional.ofNullable(roomTable.getSelectionModel().getSelectedItem());

        if(selectedRoom.isPresent()){
            nameField.setText(selectedRoom.get().getRoomName());
            doctorChoice.getSelectionModel().select(DoctorData.getCertainDoctorFromId(selectedRoom.get().getDoctorID()).getDoctorFullName());
        }
    }


}
