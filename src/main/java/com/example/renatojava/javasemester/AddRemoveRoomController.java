package com.example.renatojava.javasemester;

import com.example.renatojava.javasemester.entity.CheckObjects;
import com.example.renatojava.javasemester.entity.Data;
import com.example.renatojava.javasemester.entity.Doctor;
import com.example.renatojava.javasemester.entity.Room;
import com.example.renatojava.javasemester.exceptions.ObjectExistsException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AddRemoveRoomController {

    @FXML
    private TableView<Room> roomTable;

    @FXML
    private TableColumn<Room, String> roomNameColumn, doctorIDColumn;

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
        List<Room> allRooms = Data.getAllRooms();

        ObservableList<Room> observableList = FXCollections.observableArrayList(allRooms);

        roomNameColumn.setCellValueFactory(room -> {
            return new SimpleStringProperty(room.getValue().getRoomName());
        });
        doctorIDColumn.setCellValueFactory(room -> {
            return new SimpleStringProperty(String.valueOf(room.getValue().getDoctorID()));
        });

        roomTable.setItems(observableList);
    }
    public void removeRoom(){
        Room oldRoom = roomTable.getSelectionModel().getSelectedItem();
        Data.removeRoom(oldRoom);
        initialize();
    }

    public void addRoom(){

        Doctor selectedDoctor = allDoctors.stream().filter(doctor -> doctor.getDoctorFullName().equals(doctorChoice.getSelectionModel().getSelectedItem())).toList().get(0);
        Data.addRoom(nameField.getText(), selectedDoctor.getId());
        initialize();
        clearFields();
    }

    public void clearFields(){
        nameField.setText("");
        doctorChoice.getSelectionModel().clearSelection();
    }

}
