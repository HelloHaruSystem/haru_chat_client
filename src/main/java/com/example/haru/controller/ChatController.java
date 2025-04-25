package com.example.haru.controller;

import com.example.haru.model.ConnectionModel;
import com.example.haru.model.MessageStore;
import com.example.haru.view.ChatView;

import javafx.scene.layout.BorderPane;

public class ChatController {
    private final MessageStore messageStore;
    private final ConnectionModel connection;
    private final ChatView view;

    public ChatController(String username, String serverAddress, int port) {
        this.messageStore = new MessageStore();
        this.connection = new ConnectionModel();
        this.view = new ChatView(this);

        // bind the view to the model
        // TODO: implement
        view.bindToModel(messageStore);

        // setup the connection
        connection.setMessageReceivedHandler(message -> {
            messageStore.addMessage(message);
        });

        // lastly connect to the server
        connection.connect(username, serverAddress, port);
    }

    public void sendMessage(String messageText) {
        if (messageText != null && !messageText.trim().isEmpty()) {
            connection.sendMessage(messageText);

            // clear the message input field after an input is given
            view.ClearMessageInput();
        }
    }

    // getters and setters
    public BorderPane getView() {
        return view.getRoot();
    }
}
