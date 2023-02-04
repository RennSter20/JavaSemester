package com.example.renatojava.javasemester.menus;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.api.APIManager;
import com.example.renatojava.javasemester.api.APIResponse;
import com.example.renatojava.javasemester.database.CheckupData;
import com.example.renatojava.javasemester.database.StatsData;
import com.example.renatojava.javasemester.entity.ActiveCheckup;
import com.example.renatojava.javasemester.entity.Stats;
import com.example.renatojava.javasemester.exceptions.NoCountryDataException;
import com.example.renatojava.javasemester.util.DateFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MenuScreenController implements StatsData {

    @FXML
    private Text welcomeText, patients, debt, doctors, bills;

    @FXML
    private ListView checkupsToday;

    @FXML
    private Text totalCases, totalDeaths, newDailyCases, lastUpdated;

    @FXML
    private ComboBox<String> countriesChoice;
    @FXML
    public void initialize(){
        String role = Application.getLoggedUser().getRole();
        if(role.equals("Doctor")){
            welcomeText.setText("Welcome back doctor " + Application.getLoggedUser().getSurname());
        }else if(role.equals("Receptionist")){
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
        if(todaysCheckups.size() > 0){
            ObservableList<ActiveCheckup> observableList = FXCollections.observableArrayList(todaysCheckups);
            checkupsToday.setItems(observableList);
        }


        ObservableList<String> countries = FXCollections.observableArrayList(Application.responseMap.keySet());
        countriesChoice.setItems(countries);

        countriesChoice.getSelectionModel().select("World");
        try{
            setInfo("World");
        }catch (IOException e){
            Application.logger.error(e.getMessage(), e);
        }catch (NoCountryDataException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Error while getting info about COVID-19.");
            alert.setContentText("Please check your internet connection and try again.");
            alert.show();
        }
    }

    public void onCountryChange(){
        try{
            setInfo(countriesChoice.getSelectionModel().getSelectedItem());
        }catch (IOException e){
            Application.logger.error(e.getMessage(), e);
        }catch (NoCountryDataException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Error while getting info about COVID-19.");
            alert.setContentText("Please check your internet connection and try again.");
            alert.show();
        }
    }

    public void setInfo(String country) throws IOException {

        totalCases.setText("...");
        totalDeaths.setText("...");
        newDailyCases.setText("...");
        lastUpdated.setText("...");

        APIResponse apiResponse;

            if(Application.responseMap.get(country) == null){
                if(country.equals("World")){
                    try {
                        Application.responseMap.put(country, APIManager.getWorldInfo());
                    } catch (IOException e) {
                        Application.logger.error(e.getMessage(), e);
                    }
                }else{
                    try {
                        Application.responseMap.put(country, APIManager.getCountryInfo(country));
                    } catch (IOException e) {
                        Application.logger.error(e.getMessage(), e);
                    }
                }
                apiResponse = Application.responseMap.get(country);
                if(apiResponse == null){
                    throw new NoCountryDataException("Cannot obtain data for selected country.");
                }
            }else{
                apiResponse = Application.responseMap.get(country);
            }
                try{
                    totalCases.setText(apiResponse.getTotalCases().toString());
                    totalDeaths.setText(apiResponse.getTotalDeaths().toString());
                    newDailyCases.setText(apiResponse.getNewCasesDay().toString());
                    lastUpdated.setText(DateFormatter.getTimeWithSeconds(apiResponse.getLastUpdatedFullTime()));
                }catch (NullPointerException e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Error with gathering newest info.");
                    alert.setContentText("It's not possible to get newest information about COVID-19, please try again later and check your connection.");
                    alert.show();
                    Application.logger.error(e.getMessage(), e);
                }
    }
}
