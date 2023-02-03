package com.example.renatojava.javasemester.room;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.database.DoctorData;
import com.example.renatojava.javasemester.database.DoctorRoomData;
import com.example.renatojava.javasemester.entity.Doctor;
import com.example.renatojava.javasemester.entity.DoctorRoom;
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

    private Set<Doctor> allDoctors;

    @FXML
    public void initialize(){
        try{
            allDoctors = DoctorData.getAllDoctors();

            ObservableList<String> doctorsList = FXCollections.observableArrayList(allDoctors.stream().map(Doctor::getDoctorFullName).collect(Collectors.toList()));
            doctorChoice.setItems(doctorsList);
            fillRoomTable();

        } catch (SQLException | IOException e) {
            Application.logger.error(e.getMessage(), e);
        }
    }

    public void fillRoomTable(){
        List<DoctorRoom> allDoctorRooms = DoctorRoomData.getAllRooms();
        ObservableList<DoctorRoom> observableList = FXCollections.observableArrayList(allDoctorRooms);

        roomNameColumn.setCellValueFactory(room -> new SimpleStringProperty(room.getValue().getRoomName()));
        doctorColumn.setCellValueFactory(room -> new SimpleStringProperty(DoctorData.getCertainDoctorFromId(room.getValue().getDoctorID()).getDoctorFullName()));

        roomTable.setItems(observableList);
    }

    public void apply(){
        Optional<DoctorRoom> selectedRoom = Optional.ofNullable(roomTable.getSelectionModel().getSelectedItem());
        Optional<Doctor> newDoctor = allDoctors.stream().filter(doctor -> doctor.getDoctorFullName().contains(doctorChoice.getSelectionModel().getSelectedItem())).findAny();

        if(selectedRoom.isPresent()){
            if(!nameField.equals("")){
                DoctorRoomData.updateRoom(nameField.getText(), newDoctor.get(), selectedRoom.get());
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
