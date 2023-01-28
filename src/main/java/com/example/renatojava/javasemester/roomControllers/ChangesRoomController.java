package com.example.renatojava.javasemester.roomControllers;

import com.example.renatojava.javasemester.database.Data;
import com.example.renatojava.javasemester.entity.ChangeWriter;
import com.example.renatojava.javasemester.entity.DoctorRoom;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ChangesRoomController {

    @FXML
    private TableView<DoctorRoom> oldTable, newTable;

    @FXML
    private TableColumn<DoctorRoom, String> roomOld, doctorOld, roomNew, doctorNew;

    @FXML
    private Text role, changeTimeText;

    ChangeWriter changer = new ChangeWriter();

    List<String> changesTime = changer.readTimeRooms();

    List<String> rolesList;
    Integer selectedRoom;

    public void initialize(){
        List<DoctorRoom> roomsList = changer.readRooms();

        rolesList = changer.readRoleChangeRooms();

        List<DoctorRoom> oldRooms = new ArrayList<>();
        List<DoctorRoom> newRooms = new ArrayList<>();

        for(int i = 0;i<roomsList.size();i+=2){
            oldRooms.add(roomsList.get(i));
        }
        for(int i = 1;i<roomsList.size();i+=2){
            newRooms.add(roomsList.get(i));
        }

        fillOldTable(oldRooms);
        fillNewTable(newRooms);
    }

    public void fillOldTable(List<DoctorRoom> oldRooms){

        ObservableList<DoctorRoom> observableList = FXCollections.observableArrayList(oldRooms);

        roomOld.setCellValueFactory(room -> new SimpleStringProperty(room.getValue().getRoomName()));
        doctorOld.setCellValueFactory(room -> new SimpleStringProperty(Data.getCertainDoctor(room.getValue().getDoctorID()).getDoctorFullName()));

        oldTable.setItems(observableList);
    }

    public void fillNewTable(List<DoctorRoom> newRooms){

        ObservableList<DoctorRoom> observableList = FXCollections.observableArrayList(newRooms);

        roomNew.setCellValueFactory(room -> new SimpleStringProperty(room.getValue().getRoomName()));
        doctorNew.setCellValueFactory(room -> new SimpleStringProperty(Data.getCertainDoctor(room.getValue().getDoctorID()).getDoctorFullName()));

        newTable.setItems(observableList);
    }

    public void showTimeOld(){
        if(oldTable.getSelectionModel().getSelectedItem() != null){
            selectedRoom = oldTable.getSelectionModel().getSelectedIndex();
            newTable.getSelectionModel().select(oldTable.getSelectionModel().getFocusedIndex());
            changeTimeText.setText(changesTime.get(selectedRoom));
            role.setText("by role " + rolesList.get(oldTable.getSelectionModel().getSelectedIndex()));
        }
    }

    public void showTimeNew(){
        if(newTable.getSelectionModel().getSelectedItem() != null){
            selectedRoom = newTable.getSelectionModel().getSelectedIndex();
            oldTable.getSelectionModel().select(newTable.getSelectionModel().getFocusedIndex());
            changeTimeText.setText(changesTime.get(selectedRoom));
            role.setText("by role " + rolesList.get(newTable.getSelectionModel().getSelectedIndex()));
        }
    }

}