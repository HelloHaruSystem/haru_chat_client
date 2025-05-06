package com.example.haru.controller;

import com.example.haru.util.TokenManager;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class NavigationController {
    private final Stage primaryStage;
    private LoginController loginController;
    private ChatController chatController;

    // server config
    //TODO: Move to config file
    //TODO: make it so the user can chose server
    private final String serverAddress = "10.0.1.211";
    private final int serverPort = 2909;

    public NavigationController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.loginController = new LoginController(this);

        // when starting the login screen should first be displayed
        //TODO: implement a way to stay logged in
        showLoginScreen();
    }

    public void showLoginScreen() {
        // clear stored token when going back to login screen
        TokenManager.clearToken();

        // get the view from the controller
        BorderPane loginRoot = this.loginController.getView().getRoot();

        // create a scene with the login view
        Scene loginScene = new Scene(loginRoot, 400, 500);

        // apply the style sheet
        this.loginController.getView().loadStyleSheet(loginScene);

        // set the scene on the stage
        this.primaryStage.setTitle("Haru Chat - Login");
        this.primaryStage.setScene(loginScene);
        this.primaryStage.sizeToScene();
        this.primaryStage.centerOnScreen();
    }

    public void showChatScreen(String username) {
        // get the JWT token
        //TODO: give the JWT to the chat server and let the server check if it's valid before continuing 
        
        if (TokenManager.hasToken()) {
            showLoginScreen();
            return;
        }

        // initialize a new chat controller
        this.chatController = new ChatController(username, serverAddress, serverPort);

        // get the chat view from the controller
        BorderPane chatRoot = this.chatController.getView();

        // create a scene with the chat view
        Scene chatScene = new Scene(chatRoot, 800, 600);

        // apply the style sheet
        this.chatController.applyStylesheet(chatScene);

        // set the scene on the stage
        this.primaryStage.setTitle("Haru Chat - " + username);
        this.primaryStage.setScene(chatScene);
        this.primaryStage.centerOnScreen();
    }

    public void shutdown() {
        if (this.chatController != null) {
            this.chatController.shutdown();
        }
    }
}
