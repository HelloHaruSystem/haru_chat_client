package com.example.haru.view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class TopBar extends HBox {
    
    private final int EDGE_PADDING = 10;

    public TopBar() {
        // spacing for the top bar
        super(5);

        // set alignment for the entire TopBar
        this.setAlignment(Pos.CENTER_LEFT);

        // create welcome label for the left side
        Label welcomeLabel = new Label("Welcome to Haru Chat!");
        
        // create a spacer region that will push the buttons to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // create button container and set alignment
        HBox buttonBox = new HBox(5);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        // buttons for the top bar
        Button serverSelectButton = new Button("Select Server");
        Button clearChatButton = new Button("Clear Chat");
        Button privateMessageButton = new Button("Private Message");
        Button helpButton = new Button("Help");

        // add the buttons to the button box
        buttonBox.getChildren().addAll(serverSelectButton, clearChatButton, privateMessageButton, helpButton);

        // set padding for the entire TopBar
        this.setPadding(new Insets(0, EDGE_PADDING, 0, EDGE_PADDING));

        // add label and button box to TopBar
        this.getChildren().addAll(welcomeLabel, spacer, buttonBox);

        // give a style class
        buttonBox.getStyleClass().add("top-bar-button-box");
        welcomeLabel.getStyleClass().add("top-bar-label");
        this.getStyleClass().add("top-bar");
    }
}
