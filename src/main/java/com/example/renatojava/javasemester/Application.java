package com.example.renatojava.javasemester;

import com.example.renatojava.javasemester.entity.User;
import com.example.renatojava.javasemester.threads.LastChangeThread;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Application extends javafx.application.Application {

    public static final Logger logger = LoggerFactory.getLogger(Application.class);
    public static Stage mainStage;
    public static User loggedUser;

    public static Stage getStage() {
        return mainStage;
    }

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/fxml/loginScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 800);
        stage.setScene(scene);
        stage.setFullScreen(false);
        stage.setResizable(false);
        stage.show();

        Timeline latestChange = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.runLater(new LastChangeThread());
            }
        }));
        latestChange.setCycleCount(Timeline.INDEFINITE);
        latestChange.play();


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
//TODO data class, implementirati trazenje objekata kao i iz pripreme(Optional)

//TODO threads add second thread
//TODO change everything to label
//TODO fix all alerts

//TODO calendar
//TODO main menu todays checkups!!!
//TODO instructions to use program