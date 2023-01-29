package com.example.renatojava.javasemester.procedure;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.database.ProcedureData;
import com.example.renatojava.javasemester.entity.ChangeWriter;
import com.example.renatojava.javasemester.entity.Procedure;
import com.example.renatojava.javasemester.exceptions.NoProceduresException;
import com.example.renatojava.javasemester.exceptions.ObjectExistsException;
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

public class EditProcedureController implements ProcedureData, Notification {

    @FXML
    private TextField filterField, descriptionField, priceField;

    @FXML
    private TableView<Procedure> proceduresTable;

    @FXML
    private TableColumn<Procedure, String> descriptionColumn, priceColumn;

    public void initialize(){
        try{
            fillTable(ProcedureData.getAllProcedures());
            clearFields();
        }catch (SQLException | IOException e) {
            Application.logger.error(e.getMessage(), e);
        }catch (NoProceduresException e){
            Application.logger.error(e.getMessage(), e);
        }
    }

    public void editProcedure(){
        try{
            if(!descriptionField.getText().equals("") && !priceField.getText().equals("")){
                if(Notification.confirmEdit()){

                    Procedure oldProcedure = proceduresTable.getSelectionModel().getSelectedItem();

                    if(CheckObjects.checkIfPatientsHaveProcedure(oldProcedure.description())){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR");
                        alert.setHeaderText("Error while deleting procedure!");
                        alert.setContentText("Some patients have this checkup.");
                        alert.show();
                        return;
                    }

                    CheckObjects.checkIfProcedureExists(descriptionField.getText());
                    ProcedureData.updateProcedure(new Procedure(oldProcedure.id(), descriptionField.getText(), Double.valueOf(priceField.getText())));


                    ChangeWriter changeWriter = new ChangeWriter(oldProcedure, ProcedureData.getProcedureFromDescription(descriptionField.getText()));
                    changeWriter.addChange(Application.getLoggedUser().getRole());
                    initialize();
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Error while deleting procedure!");
                alert.setContentText("All fields must be filled.");
                alert.show();
            }
        } catch (ObjectExistsException e) {
            Alert emptyAlert = new Alert(Alert.AlertType.ERROR);
            emptyAlert.setTitle("INFORMATION");
            emptyAlert.setHeaderText("Deleting procedure error");
            emptyAlert.setContentText(e.getMessage());
            emptyAlert.show();
            Application.logger.error(e.getMessage(), e);
        }
    }

    public void fillFields(){
        Optional<Procedure> selectedProcedure = Optional.of(proceduresTable.getSelectionModel().getSelectedItem());
        if(selectedProcedure.isPresent()){
            descriptionField.setText(selectedProcedure.get().description());
            priceField.setText(selectedProcedure.get().price().toString());
        }
    }

    public void clearFields(){
        descriptionField.setText("");
        priceField.setText("");
    }
    public void searchProcedure(){
        String inputString = filterField.getText();

        List<Procedure> filteredProcedures = null;

        try{
            filteredProcedures = ProcedureData.getAllProcedures().stream().filter(procedure -> procedure.description().toLowerCase().contains(inputString.toLowerCase())).collect(Collectors.toList());
        }catch (IOException | SQLException | NoProceduresException e){
            Application.logger.error(e.getMessage(), e);
        }

        fillTable(filteredProcedures);
    }

    public void fillTable(List<Procedure> list){
        ObservableList<Procedure> observableList = FXCollections.observableArrayList(list);

        descriptionColumn.setCellValueFactory(procedure -> new SimpleStringProperty(procedure.getValue().description()));
        priceColumn.setCellValueFactory(procedure -> new SimpleStringProperty(procedure.getValue().price().toString()));

        proceduresTable.setItems(observableList);
    }

}
