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
    private final Button serverSelectButton;
    private final Button clearChatButton;
    private final Button privateMessageButton;
    private final Button helpButton;
    private final Button logoutButton;
    private final Label welcomeLabel;

    public TopBar() {
        // spacing for the top bar
        super(5);

        // set alignment for the entire TopBar
        this.setAlignment(Pos.CENTER_LEFT);

        // create welcome label for the left side
        this.welcomeLabel = new Label("Welcome to Haru Chat!");
        
        // create a spacer region that will push the buttons to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // create button container and set alignment
        HBox buttonBox = new HBox(5);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        // buttons for the top bar
        this.serverSelectButton = new Button("Select Server");
        this.clearChatButton = new Button("Clear Chat");
        this.privateMessageButton = new Button("Private Message");
        this.helpButton = new Button("Help");
        this.logoutButton = new Button("Logout");

        // add the buttons to the button box
        buttonBox.getChildren().addAll(this.serverSelectButton, this.clearChatButton,
            this.privateMessageButton, this.helpButton, this.logoutButton);

        // set padding for the entire TopBar
        this.setPadding(new Insets(0, EDGE_PADDING, 0, EDGE_PADDING));

        // add label and button box to TopBar
        this.getChildren().addAll(this.welcomeLabel, spacer, buttonBox);

        // give a style class
        buttonBox.getStyleClass().add("top-bar-button-box");
        welcomeLabel.getStyleClass().add("top-bar-label");
        this.getStyleClass().add("top-bar");
    }

    // set the action of clear chat butotn
    public void setClearChatAction(Runnable action) {
        this.clearChatButton.setOnAction(event -> {
            if (action != null) {
                action.run();
            }
        });
    }

    // set the action of the logout button
    public void setLogoutAction(Runnable action) {
        this.logoutButton.setOnAction(event -> {
            if (action != null) {
                action.run();
            }
        });
    }

    // set the action of the help button
    public void setHelpAction(Runnable action) {
        helpButton.setOnAction(event -> {
            if (action != null) {
                action.run();
            }
        });
    }
}
