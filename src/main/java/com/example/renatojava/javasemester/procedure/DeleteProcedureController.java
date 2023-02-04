package com.example.renatojava.javasemester.procedure;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.database.ProcedureData;
import com.example.renatojava.javasemester.entity.Procedure;
import com.example.renatojava.javasemester.exceptions.NoProceduresException;
import com.example.renatojava.javasemester.util.CheckObjects;
import com.example.renatojava.javasemester.util.Notification;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DeleteProcedureController implements ProcedureData, Notification {

    @FXML
    private TableView<Procedure> priceTable;
    @FXML
    private TableColumn<Procedure, String> descriptionColumn;
    @FXML
    private TableColumn<Procedure, String> priceColumn;
    @FXML
    private TextField searchField;

    private List<Procedure> proceduresToShow;

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

    public void deleteProcedure(){
        Optional<Procedure> selectedProcedure = Optional.ofNullable(priceTable.getSelectionModel().getSelectedItem());
        if(selectedProcedure.isPresent()){

            if(Notification.confirmEdit()){
                Procedure procedure = priceTable.getSelectionModel().getSelectedItem();

                if(CheckObjects.checkIfPatientsHaveProcedure(procedure.description())){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Error while deleting procedure!");
                    alert.setContentText("Some patients have this checkup.");
                    alert.show();
                    return;
                }

                ProcedureData.deleteProcedure(procedure.description());


                initialize();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("No procedure selected!");
            alert.setContentText("Please select procedure first.");
            alert.show();
        }
    }

}
