package com.lerstudios.space_debris_simulation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Space Debris Simulation");





        stage.getIcons().add(new Image("/icon.png"));
        //stage.getIcons().add(new Image(getClass().getResource("file:icon.png").toExternalForm()));


        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}