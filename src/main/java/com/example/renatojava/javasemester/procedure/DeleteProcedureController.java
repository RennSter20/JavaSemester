package com.example.renatojava.javasemester.procedure;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.database.Data;
import com.example.renatojava.javasemester.entity.ChangeWriter;
import com.example.renatojava.javasemester.entity.Procedure;
import com.example.renatojava.javasemester.exceptions.NoProceduresException;
import com.example.renatojava.javasemester.util.CheckObjects;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DeleteProcedureController {

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

        filteredProcedures = proceduresToShow.stream().filter(procedure -> procedure.description().toLowerCase().contains(inputString.toLowerCase())).collect(Collectors.toList());

        fillTable(filteredProcedures);
    }

    public void fillTable(List<Procedure> list){
        ObservableList<Procedure> observableList = FXCollections.observableArrayList(list);

        descriptionColumn.setCellValueFactory(procedure -> new SimpleStringProperty(procedure.getValue().description()));
        priceColumn.setCellValueFactory(procedure -> new SimpleStringProperty(procedure.getValue().price().toString()));

        priceTable.setItems(observableList);
    }

    public void deleteProcedure(){
        Optional<Procedure> selectedProcedure = Optional.of(priceTable.getSelectionModel().getSelectedItem());
        if(selectedProcedure.isPresent()){

            if(Data.confirmEdit()){
                Procedure procedure = priceTable.getSelectionModel().getSelectedItem();

                if(CheckObjects.checkIfPatientsHaveProcedure(procedure.description())){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Error while deleting procedure!");
                    alert.setContentText("Some patients have this checkup.");
                    alert.show();
                    return;
                }

                Data.deleteProcedure(procedure.description());

                ChangeWriter writer = new ChangeWriter(procedure, new Procedure(0, "-", Double.valueOf(0)));
                writer.addChange(Application.getLoggedUser().getRole());

                initialize();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INFORMATION");
            alert.setHeaderText("No procedure selected!");
            alert.setContentText("Please select procedure first.");
            alert.show();
        }
    }

}
