package com.example.haru;

import com.example.haru.controller.ChatController;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application{

    @Override
    public void start(Stage primaryStage) {
        ChatController chatController = new ChatController("Halfdan", "10.0.1.211", 2909);

        // get view from controller and set up scene
        Scene chatScene = new Scene(chatController.getView(), 800, 600);

        // setupstage
        primaryStage.setTitle("Haru Chat");
        primaryStage.setScene(chatScene);
        primaryStage.show();
    }

    public static void main( String[] args ) {
        launch(args);
    }
}
