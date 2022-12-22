package com.example.renatojava.javasemester;

import java.sql.*;

import com.example.renatojava.javasemester.entity.Procedure;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;

public class PricesScreenController {

    @FXML
    private TableView<Procedure> priceTable;

    @FXML
    private TableColumn<Procedure, String> descriptionColumn;

    @FXML
    private TableColumn<Procedure, String> priceColumn;

    @FXML
    public void initialize(){

        List<Procedure> proceduresToShow = new ArrayList<>();

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

        ObservableList<Procedure> observableList = FXCollections.observableArrayList(proceduresToShow);

        descriptionColumn.setCellValueFactory(procedure -> {
            return new SimpleStringProperty(procedure.getValue().description());
        });
        priceColumn.setCellValueFactory(procedure -> {
            return new SimpleStringProperty(String.valueOf(procedure.getValue().price()) + " EUR");
        });

        priceTable.setItems(observableList);

    }

    public static Procedure getProcedure(ResultSet procedureSet) throws SQLException{

        String description = procedureSet.getString("description");
        String price = procedureSet.getString("price");


        return new Procedure(description, Double.valueOf(price));

    }
}
