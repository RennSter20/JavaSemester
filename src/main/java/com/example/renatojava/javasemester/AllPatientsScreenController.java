package com.example.renatojava.javasemester;

import com.example.renatojava.javasemester.entity.Patient;
import com.example.renatojava.javasemester.entity.Procedure;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AllPatientsScreenController {

    @FXML
    private TableColumn<Patient, String> nameColumn, surnameColumn, OIBColumn, genderColumn, debtColumn, proceduresColumn;

    @FXML
    private TableView<Patient> patientsTable;
    @FXML
    private TextField filterField;

    private List<Patient> patients;
    public static List<Double> debts;

    

    @FXML
    public void initialize(){

        patients = new ArrayList<>();
        debts = new ArrayList<>();

        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/production", "student", "student");

            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM PATIENTS"
            );

            while(proceduresResultSet.next()){
                Patient newPatient = getProcedure(proceduresResultSet);
                patients.add(newPatient);
            }

            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for(int i = 0;i<debts.size();i++){
            patients.get(i).setDebt(debts.get(i));
        }

        fillTable(patients);

    }

    public static Patient getProcedure(ResultSet procedureSet) throws SQLException{

        String name = procedureSet.getString("name");
        String surname = procedureSet.getString("surname");
        String gender = procedureSet.getString("gender");
        String debt = procedureSet.getString("debt");
        String procedures = procedureSet.getString("procedures");
        String oib = procedureSet.getString("oib");

        debts.add(Double.valueOf(debt));

        return new Patient(null, name,surname,gender, Double.valueOf(debt),procedures,oib);

    }

    public void fillTable(List<Patient> list){
        ObservableList<Patient> observableList = FXCollections.observableArrayList(list);

        nameColumn.setCellValueFactory(patient -> {
            return new SimpleStringProperty(patient.getValue().getName());
        });
        surnameColumn.setCellValueFactory(patient -> {
            return new SimpleStringProperty(patient.getValue().getSurname());
        });
        OIBColumn.setCellValueFactory(patient -> {
            return new SimpleStringProperty(patient.getValue().getOib());
        });
        genderColumn.setCellValueFactory(patient -> {
            return new SimpleStringProperty(patient.getValue().getGender());
        });
        debtColumn.setCellValueFactory(patient -> {
            return new SimpleStringProperty(String.valueOf(patient.getValue().getDebt()));
        });
        proceduresColumn.setCellValueFactory(patient -> {
            return new SimpleStringProperty(patient.getValue().getProcedures());
        });

        patientsTable.setItems(observableList);
    }

    public void searchPatient(){
        String filter = filterField.getText();

        List<Patient> filteredPatients;

        filteredPatients = patients.stream().filter(patient -> patient.getName().toLowerCase().contains(filter.toLowerCase()) ||
                patient.getSurname().toLowerCase().contains(filter.toLowerCase()) ||
                patient.getOib().contains(filter)).collect(Collectors.toList());

        fillTable(filteredPatients);
    }

}
