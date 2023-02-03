package com.example.renatojava.javasemester.procedure;

import com.example.renatojava.javasemester.util.ChangeWriter;
import com.example.renatojava.javasemester.entity.Procedure;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChangesProceduresController {

    @FXML
    private TableView<Procedure> oldTable, newTable;
    @FXML
    private TableColumn<Procedure, String> descriptionOld, priceOld, descriptionNew, priceNew;
    @FXML
    private Text changeTimeText,role;
    private List<String> changesTime;
    private List<String> rolesList;
    private Integer selectedProcedure;
    @FXML
    public void initialize(){
        ChangeWriter changer = new ChangeWriter();

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

    public void moreInfo(){
        Optional<Procedure> selOldProcedure = Optional.ofNullable(oldTable.getSelectionModel().getSelectedItem());
        Optional<Procedure> selNewProcedure = Optional.ofNullable(newTable.getSelectionModel().getSelectedItem());

        if(selOldProcedure.isPresent() || selNewProcedure.isPresent()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INFORMATION");
            alert.setHeaderText("More info about procedure change.");
            alert.setContentText("OLD VALUE:\n" + selOldProcedure.get() + "\n\nNEW VALUE:\n" + selNewProcedure.get());
            alert.show();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("No procedure change selected!");
            alert.setContentText("Please select procedure to show more info!");
            alert.show();
        }
    }

}
