package com.example.haru.view;

import java.time.format.DateTimeFormatter;

import com.example.haru.model.ChatMessage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MessageCell extends ListCell<ChatMessage> {
    
    @Override
    protected void updateItem(ChatMessage message, boolean empty) {
        super.updateItem(message, empty);

        if (empty || message == null) {
            setText(null);
            setGraphic(null);
        } else {
            // create message bubble
            HBox messageContainer = new HBox(10);

            // style based on the sender
            //TODO: do this in css later
            if (message.isFromCurrentUser()) {
                messageContainer.setAlignment(Pos.CENTER_RIGHT);
            } else {
                messageContainer.setAlignment(Pos.BASELINE_LEFT);
            }

            // create the message bubble itself
            //TODO: do this in css later
            VBox bubble = new VBox(5);
            bubble.getStyleClass().add("message-bubble");
            bubble.setPadding(new Insets(8, 12, 8, 12));
            bubble.setMaxWidth(300);

            // add sender name if not from current user
            //TODO: remove this feature from the server and let client handle it
            if (!message.isFromCurrentUser()) {
                Label senderLabel = new Label(message.getSender());
                senderLabel.setStyle("-fx-font-weight: bold;");
                bubble.getChildren().add(senderLabel);
            }

            // add the contents of the message
            Label contentLabel = new Label(message.getContent());
            contentLabel.setWrapText(true);

            // add timestamp to the message
            Label timeLabel = new Label(message.getTimeStamp().format(
                DateTimeFormatter.ofPattern("HH:MM")));
            timeLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #88888");

            bubble.getChildren().addAll(contentLabel, timeLabel);

            // style the bubble
            if (message.isFromCurrentUser()) {
                bubble.setStyle("-fx-background-color: #DCF8C&; -fx-background-radius: 10;");
            } else {
                bubble.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10;");
            }

            messageContainer.getChildren().add(bubble);
            setGraphic(messageContainer);
            setText(null);
        }
    }
}
