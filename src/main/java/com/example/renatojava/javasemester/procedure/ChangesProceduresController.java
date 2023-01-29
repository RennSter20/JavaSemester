package com.example.renatojava.javasemester.procedure;

import com.example.renatojava.javasemester.entity.ChangeWriter;
import com.example.renatojava.javasemester.entity.Procedure;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ChangesProceduresController {

    @FXML
    private TableView<Procedure> oldTable, newTable;
    @FXML
    private TableColumn<Procedure, String> descriptionOld, priceOld, descriptionNew, priceNew;

    @FXML
    private Text changeTimeText,role;
    ChangeWriter changer = new ChangeWriter();

    List<String> changesTime;
    List<String> rolesList;
    Integer selectedProcedure;

    public void initialize(){
        List<Procedure> proceduresList = changer.readProcedures();

        changesTime = changer.readTimeProcedures();
        rolesList = changer.readRoleChangeProcedures();

        List<Procedure> oldProcedure = new ArrayList<>();
        List<Procedure> newProcedure = new ArrayList<>();

        for(int i = 0;i<proceduresList.size();i+=2){
            oldProcedure.add(proceduresList.get(i));
        }
        for(int i = 1;i<proceduresList.size();i+=2){
            newProcedure.add(proceduresList.get(i));
        }

        fillOldTable(oldProcedure);
        fillNewTable(newProcedure);

    }
    public void showTimeOld(){
        if(oldTable.getSelectionModel().getSelectedItem() != null){
            selectedProcedure = oldTable.getSelectionModel().getSelectedIndex();
            newTable.getSelectionModel().select(oldTable.getSelectionModel().getFocusedIndex());
            changeTimeText.setText(changesTime.get(selectedProcedure));
            role.setText("by role " + rolesList.get(oldTable.getSelectionModel().getSelectedIndex()));
        }
    }

    public void showTimeNew(){
        if(newTable.getSelectionModel().getSelectedItem() != null){
            selectedProcedure = newTable.getSelectionModel().getSelectedIndex();
            oldTable.getSelectionModel().select(newTable.getSelectionModel().getFocusedIndex());
            changeTimeText.setText(changesTime.get(selectedProcedure));
            role.setText("by role " + rolesList.get(newTable.getSelectionModel().getSelectedIndex()));
        }
    }

    public void fillOldTable(List<Procedure> oldProcedures){

        ObservableList<Procedure> observableList = FXCollections.observableArrayList(oldProcedures);

        descriptionOld.setCellValueFactory(procedure -> new SimpleStringProperty(procedure.getValue().description()));
        priceOld.setCellValueFactory(procedure -> new SimpleStringProperty(procedure.getValue().price().toString()));

        oldTable.setItems(observableList);

    }

    public void fillNewTable(List<Procedure> newDoctors){

        ObservableList<Procedure> observableList = FXCollections.observableArrayList(newDoctors);

        descriptionNew.setCellValueFactory(procedure -> new SimpleStringProperty(procedure.getValue().description()));
        priceNew.setCellValueFactory(procedure -> new SimpleStringProperty(procedure.getValue().price().toString()));

        newTable.setItems(observableList);

    }

}
