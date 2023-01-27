package com.example.renatojava.javasemester;

import com.example.renatojava.javasemester.entity.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Application extends javafx.application.Application {

    public static final Logger logger = LoggerFactory.getLogger(Application.class);
    public static Stage mainStage;
    public static User loggedUser;

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("loginScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 800);
        stage.setScene(scene);
        stage.setFullScreen(false);
        stage.setResizable(false);
        stage.show();


    }

    public static void setMainPage(BorderPane root) {
        Scene scene = new Scene(root,1280,800);
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.setFullScreen(false);
        mainStage.show();
    }

    public static void setLoggedUser(User user) {
        loggedUser = user;
    }
    public static User getLoggedUser(){
        return loggedUser;
    }
    public static void main(String[] args) {
        launch();
    }
}

//TODO threads
//TODO admin procedures add
//TODO changes
//TODO editCheckups not showing
//TODO password hashing
//TODO fix exceptions everywhere


//TODO main screen stats
//TODO calendar
//TODO main menu todays checkups!!!