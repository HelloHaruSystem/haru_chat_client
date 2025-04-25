package com.example.haru.controller;

import com.example.haru.model.ConnectionModel;
import com.example.haru.model.MessageStore;
import com.example.haru.view.ChatView;

import javafx.application.Platform;
import javafx.scene.layout.BorderPane;

public class ChatController {
    private final MessageStore messageStore;
    private final ConnectionModel connection;
    private final ChatView view;
    private final String username;

    public ChatController(String username, String serverAddress, int port) {
        this.username = username;
        this.messageStore = new MessageStore();
        this.connection = new ConnectionModel();
        this.view = new ChatView(this);

        // bind the view to the model
        // TODO: implement
        view.bindToModel(messageStore);

        // setup message handling
        connection.setMessageReceivedHandler(message -> {
            // make it so the ui will update
            Platform.runLater(() -> {
                messageStore.addMessage(message);
            });
        });

        // lastly connect to the server
        connection.connect(this.username, serverAddress, port);
    }

    public void sendMessage(String messageText) {
        if (messageText != null && !messageText.trim().isEmpty()) {
            connection.sendMessage(messageText);

            // clear the message input field after an input is given
            view.clearMessageInput();
        }
    }

    // disconnects from the server
    public void disconnect() {
        if (connection != null) {
            connection.disconnect();
        }
    }

    // reconnect to the server with the same credentials TODO: implement this in connection model for automatic reconnection!
    // TODO: right now the credentials are just a username implement jwt authentication later
    public void reconnect(String serverAddress, int port) {
        this.connection.connect(this.username, serverAddress, port);
    }

    // shutdown the application properly
    public void shutdown() {
        connection.shutdown();
    }

    // clears message historic
    public void clearMessages() {
        messageStore.clearMessages();
    }

    // getters and setters
    public BorderPane getView() {
        return view.getRoot();
    }

    public String getUsername(){
        return this.username;
    }
}
