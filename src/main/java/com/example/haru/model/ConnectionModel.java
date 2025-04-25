package com.example.haru.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ConnectionModel {
    private String serverAddress;
    private int port;
    private String username;

    private Socket connection;
    private PrintWriter out;    
    private BufferedReader in;
    private boolean isConnected;
    private ExecutorService executorService;

    private Consumer<ChatMessage> messageReceivedHandler;

    public ConnectionModel() {
        this.executorService = Executors.newCachedThreadPool();
    }

    // server communication methods
    //TODO: Establish connection to server
    public void connect(String username, String serverAddress, int port) {
        this.username = username;
        this.serverAddress = serverAddress;
        this.port = port;

        this.executorService.submit(() -> {
            try {
                this.connection = new Socket(this.serverAddress, this.port);
                this.out = new PrintWriter(connection.getOutputStream(), true); // true for auto flush
                this.in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                // send the username the server
                out.println(this.username);
                out.flush();

                this.isConnected = true;

                // start the message listener
                startMessageListener();

            } catch (IOException e) {
                System.out.println("Failed to connect to the server: " + e.getMessage());
            }
        });
    }

    //TODO: send message to server
    public void sendMessage(String content) {
        if (!isConnected || out == null) {
            System.out.println("Cannot send message: not connected to server");
            return;
        }

        
        // send the message
        out.println(content);

        if (this.messageReceivedHandler != null) {
            // create a message like the message just send
            ChatMessage localMessage = new ChatMessage(username, content, LocalDateTime.now(), true);

            // let the ui know about the message
            this.messageReceivedHandler.accept(localMessage);
        }
    }

    // handler for incoming messages
    //TODO: implement
    public void setMessageReceivedHandler(Consumer<ChatMessage> handler) {
        this.messageReceivedHandler = handler;
    }

    // disconnect form the server
    public void disconnect() {
        if (!this.isConnected) {
            return;
        }

        try {
            this.isConnected = false;

            if (this.out != null) {
                this.out.close();
            }

            if (this.in != null) {
                this.in.close();
            }

            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }

        } catch (IOException e) {
            System.out.println("Error trying to disconnect");
        }
    }

    private void startMessageListener() {
        this.executorService.submit(() -> {
            try {
                String message;
                while (this.isConnected && (message = in.readLine()) != null) {
                    // handle the incoming message
                    if (messageReceivedHandler != null) {
                        ChatMessage incomingChatMessage = parseMessage(message);
                        messageReceivedHandler.accept(incomingChatMessage);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error readings messages from server: " + e.getMessage());
                handleDisconnection();
            }
        });
    }

    // method to handle unexpected disconnection internally
    private void handleDisconnection() {
        this.isConnected = false;
        //TODO: implement reconnect logic!
    }

    // turns the incoming message into a ChatMessage object
    private ChatMessage parseMessage(String rawMessage) {
        // TODO: changing server side how messages are send and create the logic to parse it here
        // for example json format
        
        // temporary solution below
        int firstSpace = rawMessage.indexOf(' ');
        String sender = rawMessage.substring(0, firstSpace) + ":";
        String message = rawMessage.substring(firstSpace + 1);
        ChatMessage parsedMessage = new ChatMessage(sender, message, LocalDateTime.now(), sender.equals(username));

        return parsedMessage;
    }

    // used to shutdown properly
    public void shutdown() {
        disconnect();
        executorService.shutdown();
    }

    // getters and setters
    public boolean isConnected() {
        return this.isConnected;
    }

    public String getUsername() {
        return this.username;
    }
}
