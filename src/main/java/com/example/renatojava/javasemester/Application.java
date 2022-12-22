package com.example.renatojava.javasemester;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {

    public static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("loginScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 800);
        stage.setScene(scene);
        stage.show();
    }

    public static void setMainPage(BorderPane root) {
        Scene scene = new Scene(root,1280,800);
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

//TODO zasebne kartice za sve pacijente u povijesti sa povijesti bolesti
//TODO Novi pregled
//TODO doktori
//TODO naplata