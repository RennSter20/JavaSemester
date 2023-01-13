package com.example.renatojava.javasemester;

import com.example.renatojava.javasemester.entity.Data;
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

public class PricesScreenController implements Data {

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
            proceduresToShow = Data.getAllProcedures();
        }catch (SQLException | IOException e) {
            Application.logger.info("Message: " + e.getMessage() + " Stack trace: " + e.getStackTrace());
        }catch (NoProceduresException e){
            Application.logger.info("Message: " + e.getMessage() + " Stack trace: " + e.getStackTrace());
        }
        fillTable(proceduresToShow);
    }

    public void searchProcedure(){
        String inputString = searchField.getText();

        List<Procedure> filteredProcedures;

        filteredProcedures = proceduresToShow.stream().filter(procedure -> procedure.description().toLowerCase().contains(inputString.toLowerCase()) || procedure.price().toString().contains(inputString.toLowerCase())).collect(Collectors.toList());

        fillTable(filteredProcedures);
    }

    public void fillTable(List<Procedure> list){
        ObservableList<Procedure> observableList = FXCollections.observableArrayList(list);

        descriptionColumn.setCellValueFactory(procedure -> {
            return new SimpleStringProperty(procedure.getValue().description());
        });
        priceColumn.setCellValueFactory(procedure -> {
            return new SimpleStringProperty(procedure.getValue().price().toString());
        });

        priceTable.setItems(observableList);
    }
}
