package com.example.renatojava.javasemester.checkups;

import com.example.renatojava.javasemester.entity.ActiveCheckup;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import tornadofx.control.DateTimePicker;

public class EditCheckupsController {

    @FXML
    private TableView<ActiveCheckup> checkupTable;

    @FXML
    private TableColumn<ActiveCheckup, String> dateColumn, patientColumn, procedureColumn;

    @FXML
    private TextField filterField;

    @FXML
    private Button applyButton;

    @FXML
    private DateTimePicker datePicker;


    public void searchCheckup(){

    }

}
