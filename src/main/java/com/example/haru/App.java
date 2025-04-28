package com.example.haru;

import java.io.InputStream;

import com.example.haru.controller.ChatController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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

        // setup stage
        primaryStage.setTitle("Haru Chat");
        primaryStage.setScene(chatScene);

        // set icon for the stage
        try {
           InputStream iconStream = getClass().getResourceAsStream("/com/example/haru/images/penguin_icon.png");
            if (iconStream != null) {
                primaryStage.getIcons().add(new Image(iconStream));
            } else {
                System.out.println("Could not load application icon!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // close app when closing the window
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("App closing...");

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
