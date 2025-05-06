package com.example.haru.view;

import com.example.haru.controller.LoginController;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class LoginView {
    private BorderPane root;
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button registerButton;
    private Label statusLabel;

    public LoginView(LoginController loginController) {
        createUI();
        bindToController(loginController);
    } 

    public void createUI() {
        // root creation
        this.root = new BorderPane();
        this.root.getStyleClass().add("login-view");

        // title for the form
        Text title = new Text("Welcome to Haru Chat");
        //TODO: move to chat-styles.css
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        title.getStyleClass().add("login-title");

        // input fields and labels
        Label usernameLabel = new Label("Username:");
        this.usernameField = new TextField();
        this.usernameField.setPromptText("username");
        this.usernameField.getStyleClass().add("login-field");

        Label passwordLabel = new Label("Password");
        this.passwordField = new PasswordField();
        this.passwordField.setPromptText("password");
        this.passwordField.getStyleClass().add("login-field");

        // buttons
        this.loginButton = new Button("Login");
        this.loginButton.getStyleClass().add("login-button");

        this.registerButton = new Button("Register");
        this.registerButton.getStyleClass().add("register-button");

        // status/error label 
        this.statusLabel = new Label("");
        statusLabel.getStyleClass().addAll("status-label");

        // create button layout
        HBox buttonBox = new HBox(10, loginButton, registerButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        
        // grid fro inputs
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 20, 20, 20));

        // add the components to the grid
        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(this.usernameField, 1, 0);
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(this.passwordField, 1, 1);

        // main container
        VBox loginContainer = new VBox(20);
        loginContainer.setAlignment(Pos.CENTER);
        loginContainer.getStyleClass().add("login-container");
        loginContainer.getChildren().addAll(title, gridPane, buttonBox, statusLabel);

        // add the loginContainer in the center of the root
        root.setCenter(loginContainer);
    }

    private void bindToController(LoginController loginController) {
        // connects the ui elements to controller methods
        this.loginButton.setOnAction(event -> {
            String username = this.usernameField.getText();
            String password = passwordField.getText();
            loginController.login(username, password);
        });

        this.registerButton.setOnAction(event -> {
            String username = this.usernameField.getText();
            String password = passwordField.getText();
            loginController.register(username, password);
        });

        // add trigger to password and login field to trigger login
        this.usernameField.setOnAction(event -> {
            String username = this.usernameField.getText();
            String password = passwordField.getText();
            loginController.login(username, password);
        });

        this.passwordField.setOnAction(event -> {
            String username = this.usernameField.getText();
            String password = passwordField.getText();
            loginController.login(username, password);
        });
    }

    // sets status message
    public void setStatusMessage(String message, boolean isError) {
        this.statusLabel.setText(message);
        if (isError) {
            this.statusLabel.getStyleClass().remove("status-success");
            this.statusLabel.getStyleClass().add("status-error");
        } else {
            this.statusLabel.getStyleClass().remove("status-error");
            this.statusLabel.getStyleClass().add("status-success");
        }
    }

    // clear input fields
    public void clearFields() {
        this.usernameField.clear();
        this.passwordField.clear();
    }

    // getters and setters
    public BorderPane getRoot() {
        return this.root;
    }

    // load the the login styles
    public void loadStyleSheet(javafx.scene.Scene scene) {
        scene.getStylesheets().add(getClass().getResource("/com/example/haru/css/login-styles.css").toExternalForm());
    }
}
