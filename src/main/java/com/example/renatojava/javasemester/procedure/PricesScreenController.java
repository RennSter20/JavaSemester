package com.example.renatojava.javasemester.procedure;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.database.Data;
import com.example.renatojava.javasemester.database.ProcedureData;
import com.example.renatojava.javasemester.entity.Procedure;
import com.example.renatojava.javasemester.exceptions.NoProceduresException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PricesScreenController implements Data, ProcedureData {

    @FXML
    private TableView<Procedure> priceTable;
    @FXML
    private TableColumn<Procedure, String> descriptionColumn;
    @FXML
    private TableColumn<Procedure, String> priceColumn;
    @FXML
    private TextField searchField;

    List<Procedure> proceduresToShow = new ArrayList<>();

    @FXML
    public void initialize(){
        try{
            proceduresToShow = ProcedureData.getAllProcedures();
        }catch (SQLException | IOException e) {
            Application.logger.error(e.getMessage(), e);
        }catch (NoProceduresException e){
            Application.logger.error(e.getMessage(), e);
        }
        fillTable(proceduresToShow);
    }


    public void searchProcedure(){
        String inputString = searchField.getText();

        List<Procedure> filteredProcedures;

        filteredProcedures = proceduresToShow.stream().filter(procedure -> procedure.description().toLowerCase().contains(inputString.toLowerCase())).collect(Collectors.toList());

        fillTable(filteredProcedures);
    }

    public void fillTable(List<Procedure> list){
        ObservableList<Procedure> observableList = FXCollections.observableArrayList(list);

        descriptionColumn.setCellValueFactory(procedure -> new SimpleStringProperty(procedure.getValue().description()));
        priceColumn.setCellValueFactory(procedure -> new SimpleStringProperty(procedure.getValue().price().toString()));

        priceTable.setItems(observableList);
    }
}
