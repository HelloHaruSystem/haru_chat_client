package com.example.haru;

import java.io.InputStream;

import com.example.haru.controller.ChatController;
import com.example.haru.controller.NavigationController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;



public class App extends Application {
    private NavigationController navigationController;

    @Override
    public void start(Stage primaryStage) {
        // setup application icon
        //TODO: move this to a method of it's own
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

        // set title and style
        primaryStage.setTitle("Haru Chat");
        primaryStage.initStyle(StageStyle.DECORATED);

        // init nav controller
        this.navigationController = new NavigationController(primaryStage);

        // handle app close request
        //TODO: move to a method of it's own
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("App closing...");

            if (this.navigationController != null) {
                this.navigationController.shutdown();
            }

            Platform.exit();
        });

        // show the stave
        primaryStage.show();
    }

    public static void main( String[] args ) {
        launch(args);
    }
}
