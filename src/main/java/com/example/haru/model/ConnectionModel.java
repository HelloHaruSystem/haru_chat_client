package com.example.haru.model;

import java.net.http.WebSocket;
import java.util.function.Consumer;

public class ConnectionModel {
    private String serverAddress;
    private int serverPort;
    private String userName;
    private WebSocket connection; // match the servers connection technology

    // server communication methods
    //TODO: Establish connection to server
    public void connect(String username, String serverAddress, int port) {

    }

    //TODO: send message to server
    public void sendMessage(String content) {

    }

    // sets a callback when messages are received from the server
    //TODO: implement
    public void setMessageReceivedHandler(Consumer<ChatMessage> handler) {

    }
}
