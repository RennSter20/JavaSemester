package com.example.renatojava.javasemester;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class MenuScreenController {

    @FXML
    private Text welcomeText;

    @FXML
    public void initialize(){
        String role = Application.getLoggedUser().getRole();
        if(role.equals("Doctor")){
            welcomeText.setText("Welcome back doctor " + Application.getLoggedUser().getSurname());
        }else{
            welcomeText.setText("Welcome back receptionist " + Application.getLoggedUser().getSurname());
        }
    }
}
