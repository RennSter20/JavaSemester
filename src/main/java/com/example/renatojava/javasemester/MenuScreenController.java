package com.example.renatojava.javasemester;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class MenuScreenController {

    @FXML
    private Text welcomeText;

    @FXML
    public void initialize(){
        welcomeText.setText(Application.getLoggedUser().getName());
    }
}
