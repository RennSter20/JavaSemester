package com.example.renatojava.javasemester;

import java.sql.*;

import com.example.renatojava.javasemester.entity.Procedure;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PricesScreenController {

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

        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/production", "student", "student");

            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM PROCEDURES"
            );

            while(proceduresResultSet.next()){
                Procedure noviPregled = getProcedure(proceduresResultSet);
                proceduresToShow.add(noviPregled);
            }

            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        fillTable(proceduresToShow);

    }

    public static Procedure getProcedure(ResultSet procedureSet) throws SQLException{

        String description = procedureSet.getString("description");
        String price = procedureSet.getString("price");


        return new Procedure(description, Double.valueOf(price));

    }

    public void searchProcedure(){
        String inputString = searchField.getText();

        List<Procedure> filteredProcedures = new ArrayList<>();

        filteredProcedures = proceduresToShow.stream().filter(procedure -> procedure.description().toLowerCase().contains(inputString.toLowerCase()) || procedure.price().toString().contains(inputString.toLowerCase())).collect(Collectors.toList());

        fillTable(filteredProcedures);
    }

    public void fillTable(List<Procedure> list){
        ObservableList<Procedure> observableList = FXCollections.observableArrayList(list);

        descriptionColumn.setCellValueFactory(procedure -> {
            return new SimpleStringProperty(procedure.getValue().description());
        });
        priceColumn.setCellValueFactory(procedure -> {
            return new SimpleStringProperty(procedure.getValue().price() + " EUR");
        });

        priceTable.setItems(observableList);
    }
}
