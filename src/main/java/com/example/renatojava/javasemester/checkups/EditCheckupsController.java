package com.example.renatojava.javasemester.checkups;

import com.example.renatojava.javasemester.database.CheckupData;
import com.example.renatojava.javasemester.database.PatientData;
import com.example.renatojava.javasemester.database.ProcedureData;
import com.example.renatojava.javasemester.entity.*;
import com.example.renatojava.javasemester.util.DateFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import tornadofx.control.DateTimePicker;

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
    private ChoiceBox<PatientRoom> roomChoiceBox;


    public void initialize(){

        List<PatientRoom> rooms = List.of(new RoomA("A"), new RoomB("B"), new RoomC("C"));
        ObservableList<PatientRoom> observableList = FXCollections.observableArrayList(rooms);
        roomChoiceBox.setItems(observableList);
        roomChoiceBox.getSelectionModel().selectFirst();
        roomChoiceBox.setConverter(new StringConverter<PatientRoom>() {
            @Override
            public String toString(PatientRoom patientRoom) {
                return patientRoom.getRoomType();
            }

            @Override
            public PatientRoom fromString(String s) {
                return null;
            }
        });

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

        List<ActiveCheckup> filteredCheckups = null;

        filteredCheckups = CheckupData.getAllActiveCheckups().stream()
                .filter(checkup -> checkup.getDateOfCheckup().toString().toLowerCase().contains(searchText.toLowerCase())
                        || checkup.getRoom().getRoomType().toLowerCase().contains(searchText.toLowerCase())
                        || PatientData.getPatientWithID(checkup.getPatientID()).getFullName().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        fillCheckupTable(filteredCheckups);
    }

    public void onSelect(){
        Optional<ActiveCheckup> selectedCheckup = Optional.ofNullable(checkupTable.getSelectionModel().getSelectedItem());

        if(selectedCheckup.isPresent()){
            datePicker.setDateTimeValue(selectedCheckup.get().getDateOfCheckup());
            roomChoiceBox.getSelectionModel().select(selectedCheckup.get().getRoom());
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Error while getting selected checkup.");
            alert.setContentText("To edit a checkup, please select one.");
            alert.show();
        }
    }

    public void apply(){
        CheckupData.updateActiveCheckup(checkupTable.getSelectionModel().getSelectedItem().getId(), datePicker.getDateTimeValue(), roomChoiceBox.getSelectionModel().getSelectedItem());
    }

}
