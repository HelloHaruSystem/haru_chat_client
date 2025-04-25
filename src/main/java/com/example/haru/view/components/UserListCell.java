package com.example.haru.view.components;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class UserListCell extends ListCell<String> {
    
    @Override
    protected void updateItem(String username, boolean empty) {
        super.updateItem(username, empty);

        if (empty || username == null) {
            setText(null);
            setGraphic(null);
        } else {
            HBox container = new HBox(10);
            container.getStyleClass().add("user-list-cell-container");

            // circle to indicate if online
            Circle onlineIndicator = new Circle(5);
            onlineIndicator.setFill(Color.GREEN);
            onlineIndicator.getStyleClass().add("online-indicator");

            // create the username label
            Label usernameLabel = new Label(username);
            usernameLabel.getStyleClass().add("username-label");

            // add the label to the container
            container.getChildren().add(usernameLabel);

            setGraphic(container);
            setText(null);

            getStyleClass().add("user-list-cell");
        }
    }
}
