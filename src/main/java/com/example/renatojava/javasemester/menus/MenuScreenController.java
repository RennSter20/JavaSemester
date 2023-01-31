package com.example.renatojava.javasemester.menus;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.database.CheckupData;
import com.example.renatojava.javasemester.database.StatsData;
import com.example.renatojava.javasemester.entity.ActiveCheckup;
import com.example.renatojava.javasemester.entity.Stats;
import com.example.renatojava.javasemester.util.DateFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class MenuScreenController implements StatsData {

    @FXML
    private Text welcomeText, patients, debt, doctors, bills;

    @FXML
    private ListView checkupsToday;

    @FXML
    public void initialize(){
        String role = Application.getLoggedUser().getRole();
        if(role.equals("Doctor")){
            welcomeText.setText("Welcome back doctor " + Application.getLoggedUser().getSurname());
        }else if(role.equals("Receptionis")){
            welcomeText.setText("Welcome back receptionist " + Application.getLoggedUser().getSurname());
        }else{
            welcomeText.setText("Welcome back " + Application.getLoggedUser().getRole());
        }

        Stats stats = StatsData.getCurrentStats();
        patients.setText(String.valueOf(stats.patients()));
        debt.setText(String.valueOf(stats.debt()));
        doctors.setText(String.valueOf(stats.doctors()));
        bills.setText(String.valueOf(stats.bills()));

        List<ActiveCheckup> allCheckups = CheckupData.getAllActiveCheckups();
        List<ActiveCheckup> todaysCheckups = new ArrayList<>();

        for(ActiveCheckup checkup : allCheckups){
            if(DateFormatter.isEqualToday(checkup.getDateOfCheckup().toString())){
                todaysCheckups.add(checkup);
            }
        }
        ObservableList<ActiveCheckup> observableList = FXCollections.observableArrayList(todaysCheckups);
        if(todaysCheckups.size() > 0){
            checkupsToday.setItems(observableList);
        }
    }
}
