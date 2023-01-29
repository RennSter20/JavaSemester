package com.example.renatojava.javasemester.menus;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.database.StatsData;
import com.example.renatojava.javasemester.entity.Stats;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class MenuScreenController implements StatsData {

    @FXML
    private Text welcomeText, patients, debt, doctors, bills;

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
    }
}
