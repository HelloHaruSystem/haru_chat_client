package com.example.haru;

import com.example.haru.controller.ChatController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application{

    @Override
    public void start(Stage primaryStage) {
        ChatController chatController = new ChatController("Halfdan", "10.0.1.211", 2909);

        // get view from controller and set up scene
        Scene chatScene = new Scene(chatController.getView(), 800, 600);

        // apply the stylesheet
        chatController.applyStylesheet(chatScene);

        // setupstage
        primaryStage.setTitle("Haru Chat");
        primaryStage.setScene(chatScene);

        // close app when closing the window
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("app closing");

            if (chatController != null) {
                chatController.shutdown();
            }

            Platform.exit();
        });

        // attempt at changing the the window border color itself
        // this is usually controlled by the os
        primaryStage.initStyle(StageStyle.DECORATED);

        // show the stage
        primaryStage.show();
    }

    public static void main( String[] args ) {
        launch(args);
    }
}
