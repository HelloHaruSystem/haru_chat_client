package com.example.haru.controller;

import com.example.haru.config.AppConfig;
import com.example.haru.util.TokenManager;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class NavigationController {
    private final Stage primaryStage;
    private final AppConfig config;
    private LoginController loginController;
    private ChatController chatController;

    // Track current screen to prevent node reuse issues
    private boolean isOnLoginScreen = true;

    public NavigationController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.config = AppConfig.getInstance();
        this.loginController = new LoginController(this);

        //TODO: implement a way to stay logged in

        // when starting the login screen should first be displayed
        showLoginScreen();
    }

    public void showLoginScreen() {
        // clear stored token when going back to login screen
        TokenManager.clearToken();

        // If currently on chat screen, create a new LoginController
        // Remember this because it prevents javafx node reuse error
        if (!isOnLoginScreen) {
            System.out.println("Creating new login controller to prevent node reuse");
            this.loginController = new LoginController(this);
        }

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

        // update screen tracker
        this.isOnLoginScreen = true;

        System.out.println("Showing login screen"); // debug
    }

    public void showChatScreen(String username) {
        try {
            System.out.println("Attempting to show chat screen for user: " + username); // debug

            // get the JWT token
            //TODO: give the JWT to the chat server and let the server check if it's valid before continuing 
        
            if (!TokenManager.hasToken()) {
                System.out.println("No token available, returning to login"); // debug
                showLoginScreen();
                return;
            }

            System.out.println("Token is available, proceeding to chat screen"); // debug

            // initialize a new chat controller
            this.chatController = new ChatController(username, 
                this.config.getDefaultServerAddress(), 
                this.config.getDefaultServerPort(), this);

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

            // update screen tracker
            this.isOnLoginScreen = false;

            System.out.println("Successfully switched to chat screen"); // debug 
        } catch (Exception e) {
            System.out.println("Error showing chat screen: " + e.getMessage());
            e.printStackTrace();
        }

        
    }

    public void logout() {
        // makes sure we don't have a ref to chat controller
        ChatController tempChatController = this.chatController;
        this.chatController = null;

        // show login screen
        showLoginScreen();

        // then shutdown the chat controller
        if(tempChatController != null) {
            tempChatController.shutdown();
        }
    }

    public void shutdown() {
        System.out.println("Shutting down"); // debug
        TokenManager.clearToken();
        if (this.chatController != null) {
            this.chatController.shutdown();
        }
    }
}
