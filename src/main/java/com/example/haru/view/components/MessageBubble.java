package com.example.haru.view.components;

import com.example.haru.model.ChatMessage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MessageBubble extends HBox {
    
    private static final String SYSTEM_USERNAME = "System";

    public MessageBubble(ChatMessage message) {
        super(10);

        if (message.isFromCurrentUser()) {
            setAlignment(Pos.CENTER_RIGHT);
        } else {
            setAlignment(Pos.CENTER_LEFT);
        }

        // content 
        VBox bubble = createBubbleContent(message);

        // add styling
        getStyleClass().add("message-container");
        bubble.getStyleClass().add("message-bubble");

        if (message.isFromCurrentUser()) {
            bubble.getStyleClass().add("current-user-bubble");
        } else if (SYSTEM_USERNAME.equals(message.getSender())) {
            bubble.getStyleClass().add("system-message");
        } else {
            bubble.getStyleClass().add("other-user-bubble");
        }

        getChildren().add(bubble);
    }

    private VBox createBubbleContent(ChatMessage message) {
        VBox bubble = new VBox(5);
        bubble.setPadding(new Insets(8, 12, 8, 12));
        bubble.setMaxWidth(300);

        // add sender name to bubble if it's not from the currentUser
        if (!message.isFromCurrentUser()) {
            Label senderLabel = new Label(message.getSender());
            senderLabel.getStyleClass().add("sender-name");
            bubble.getChildren().add(senderLabel);
        }

        // add the message contents
        Label contentLabel = new Label(message.getContent());
        contentLabel.setWrapText(true);
        contentLabel.getStyleClass().add("message-content");

        // add the time stamp to the bubble
        Label timeLabel = new Label(message.getFormattedTime());
        timeLabel.getStyleClass().add("message-time");

        bubble.getChildren().addAll(contentLabel, timeLabel);

        return bubble;
    }
}
