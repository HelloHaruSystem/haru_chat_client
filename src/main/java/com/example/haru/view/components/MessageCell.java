package com.example.haru.view.components;

import com.example.haru.model.ChatMessage;

import javafx.scene.control.ListCell;


public class MessageCell extends ListCell<ChatMessage> {
    
    @Override
    protected void updateItem(ChatMessage message, boolean empty) {
        super.updateItem(message, empty);

        if (empty || message == null) {
            setText(null);
            setGraphic(null);
        } else {
            // create a new bubble component
            MessageBubble messageBubble = new MessageBubble(message);

            // set the bubble as this cell's graphic
            setGraphic(messageBubble);
            setText(null);
        }
    }
}
