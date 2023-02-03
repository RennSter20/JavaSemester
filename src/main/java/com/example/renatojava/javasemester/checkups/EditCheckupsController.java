package com.example.renatojava.javasemester.checkups;

import com.example.renatojava.javasemester.database.CheckupData;
import com.example.renatojava.javasemester.database.PatientData;
import com.example.renatojava.javasemester.database.ProcedureData;
import com.example.renatojava.javasemester.entity.ActiveCheckup;
import com.example.renatojava.javasemester.entity.PatientRoom;
import com.example.renatojava.javasemester.util.CheckObjects;
import com.example.renatojava.javasemester.util.DateFormatter;
import com.example.renatojava.javasemester.util.Notification;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tornadofx.control.DateTimePicker;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EditCheckupsController {

    @FXML
    private TableView<ActiveCheckup> checkupTable;
    @FXML
    private TableColumn<ActiveCheckup, String> dateColumn, patientColumn, procedureColumn;
    @FXML
    private TextField filterField;
    @FXML
    private DateTimePicker datePicker;
    @FXML
    private ChoiceBox<String> roomChoiceBox;

    public static final DateTimeFormatter DATE_TIME_FORMAT_FULL = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @FXML
    public void initialize(){

        List<String> rooms = List.of("Consulting room","Sickroom", "Casualty");
        ObservableList<String> observableList = FXCollections.observableArrayList(rooms);
        roomChoiceBox.setItems(observableList);
        roomChoiceBox.getSelectionModel().selectFirst();

        fillCheckupTable(CheckupData.getAllActiveCheckups());
    }

    public void fillCheckupTable(List<ActiveCheckup> allCheckups){
        ObservableList<ActiveCheckup> list = FXCollections.observableArrayList(allCheckups);

        dateColumn.setCellValueFactory(checkup -> new SimpleStringProperty(DateFormatter.getDateTimeFormatted(checkup.getValue().getDateOfCheckup().toString())));
        patientColumn.setCellValueFactory(checkup -> new SimpleStringProperty(PatientData.getPatientWithID(checkup.getValue().getPatientID()).getFullName()));
        procedureColumn.setCellValueFactory(checkup -> new SimpleStringProperty(ProcedureData.getProcedureFromId(checkup.getValue().getProcedureID()).description()));

        checkupTable.setItems(list);
    }

    public void search(){
        String searchText = filterField.getText();

        List<ActiveCheckup> filteredCheckups = CheckupData.getAllActiveCheckups().stream()
                .filter(checkup -> checkup.getDateOfCheckup().toString().toLowerCase().contains(searchText.toLowerCase())
                        || checkup.getRoom().getRoomType().toLowerCase().contains(searchText.toLowerCase())
                        || PatientData.getPatientWithID(checkup.getPatientID()).getFullName().toLowerCase().contains(searchText))
                .collect(Collectors.toList());;

        fillCheckupTable(filteredCheckups);
    }

    public void onSelect(){
        Optional<ActiveCheckup> selectedCheckup = Optional.ofNullable(checkupTable.getSelectionModel().getSelectedItem());

        if(selectedCheckup.isPresent()){
            datePicker.setDateTimeValue(selectedCheckup.get().getDateOfCheckup());
            roomChoiceBox.getSelectionModel().select(selectedCheckup.get().getRoom().getRoomType());
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Error while getting selected checkup.");
            alert.setContentText("To edit a checkup, please select one first.");
            alert.show();
        }
    }

    public void apply(){
        if(CheckObjects.isValidTime(datePicker.getDateTimeValue().toString(), DATE_TIME_FORMAT_FULL)){
            if(CheckObjects.checkIfHospitalHasDoctors() && CheckObjects.checkCheckupTime(datePicker.getDateTimeValue(), checkupTable.getSelectionModel().getSelectedItem()) && Notification.confirmEdit()){
                CheckupData.updateActiveCheckup(checkupTable.getSelectionModel().getSelectedItem().getId(), datePicker.getDateTimeValue(), new PatientRoom(roomChoiceBox.getValue()));
                initialize();
            }
        }
    }

}
