package com.example.renatojava.javasemester;

import com.example.renatojava.javasemester.entity.Hospital;
import com.example.renatojava.javasemester.entity.User;
import com.example.renatojava.javasemester.threads.APICountries;
import com.example.renatojava.javasemester.threads.ShowInfoTitleThread;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Application extends javafx.application.Application {

    public static final Logger logger = LoggerFactory.getLogger(Application.class);
    public static Stage mainStage;
    public static User loggedUser;
    public static List<String> countries;
    public static Hospital hospital;

    public static ExecutorService executorService;

    public static Stage getStage() {
        return mainStage;
    }

    @Override
    public void start(Stage stage) throws IOException {
            hospital = new Hospital();
            executorService = Executors.newCachedThreadPool();

            mainStage = stage;
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/fxml/loginScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1280, 800);
            stage.setScene(scene);
            stage.setFullScreen(false);
            stage.setResizable(false);
            stage.getIcons().add(new Image("/icon.png"));
            stage.show();

            Timeline latestChange = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> Platform.runLater(new ShowInfoTitleThread(hospital))));
            latestChange.setCycleCount(Timeline.INDEFINITE);
            latestChange.play();

            Thread thread = new Thread(new APICountries());
            thread.start();
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

//TODO annotation