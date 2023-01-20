package com.example.renatojava.javasemester.checkups;

import com.example.renatojava.javasemester.entity.ActiveCheckup;
import com.example.renatojava.javasemester.entity.Data;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class ListOfActiveCheckupsController {

    @FXML
    private TableView<ActiveCheckup> table;

    @FXML
    private TableColumn<ActiveCheckup, String> dateColumn, patientColumn, procedureColumn, roomColumn;

    public void initialize(){
        fillCheckupTable(Data.getAllActiveCheckups());
    }

    public void fillCheckupTable(List<ActiveCheckup> list){
        ObservableList<ActiveCheckup> observableList = FXCollections.observableArrayList(list);

        dateColumn.setCellValueFactory(checkup -> new SimpleStringProperty(checkup.getValue().getDateOfCheckup().toString()));
        patientColumn.setCellValueFactory(checkup -> new SimpleStringProperty(Data.getPatientWithID(checkup.getValue().getPatientID()).getOib()));
        procedureColumn.setCellValueFactory(checkup -> new SimpleStringProperty(Data.getProcedureFromId(checkup.getValue().getProcedureID()).description()));
        roomColumn.setCellValueFactory(checkup -> new SimpleStringProperty(checkup.getValue().getRoom().getRoomType()));

        table.setItems(observableList);
    }

}
