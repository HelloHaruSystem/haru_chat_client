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

import com.example.haru.util.TokenManager;

import javafx.application.Platform;

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
    //TODO: Split this method into small methods
    public void connect(String username, String serverAddress, int port) {
        this.username = username;
        this.serverAddress = serverAddress;
        this.port = port;

        this.executorService.submit(() -> {
            try {
                System.out.println("Connecting to server: " + serverAddress + ":" + port); // debug

                this.connection = new Socket(this.serverAddress, this.port);
                this.out = new PrintWriter(connection.getOutputStream(), true); // true for auto flush
                this.in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                // wait to get the chat servers authentication prompt
                String serverPrompt = in.readLine();
                System.out.println("Server: " + serverPrompt); // debug

                // send the authentication data (username and the JWT token)
                String token = TokenManager.getToken();
                if (token == null || token.isEmpty()) {
                    System.out.println("Error: No JWT token available");
                    disconnect();
                    return;
                }

                System.out.println("Sending auth credentials: " + username + ",TOKEN"); // debug

                // send username and token in the format expected by the chat server username and token separated by a coma
                out.println(this.username + "," + token);

                // wait for the auth result
                // this should be "Authentication successful since we already verified in the login controller"
                String authResult = in.readLine();
                System.out.println("Auth result: " + authResult);

                if (authResult != null && authResult.contains("Authentication successful")) {
                    this.isConnected = true;
                    System.out.println("Authentication confirmed"); // debug
                    // start the message listener
                    startMessageListener();
                } else {
                    System.out.println("Authentication failed: " + authResult);
                    disconnect();
                }

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

    public boolean verifyAuthentication(String username, String token, String serverAddress, int port) {
        try {
            Socket tempConnection = new Socket(serverAddress, port);
            PrintWriter tempOut = new PrintWriter(tempConnection.getOutputStream(), true); // true for auto flush
            BufferedReader tempIn = new BufferedReader(new InputStreamReader(tempConnection.getInputStream()));

            // wait for auth prompt
            String prompt = tempIn.readLine();
            System.out.println("Auth Prompt: " + prompt); // debug

            // send credentials
            tempOut.println(username + "," + token);

            // wait for the response
            String response = tempIn.readLine();
            System.out.println("Auth response: " + response); // debug

            // clean everything up
            tempOut.close();
            tempIn.close();
            tempConnection.close();

            // check if authentication was successful
            return response != null && response.contains("Authentication successful");
        } catch (IOException e) {
            System.out.println("Error verifying authentication: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // method to handle unexpected disconnection internally
    private void handleDisconnection() {
        this.isConnected = false;
        //TODO: implement reconnect logic!
    }

    // Regular message (format: "username: message")
    //TODO: potentially send it in JSON format in the future
    private ChatMessage parseMessage(String rawMessage) {
        // handle system messages
        if (rawMessage.startsWith("Authentication") || rawMessage.startsWith("Welcome")) {
            return new ChatMessage("System", rawMessage, LocalDateTime.now(), false);
        }

        // handle private messages
        if (rawMessage.startsWith("[Private")) {
            return new ChatMessage("Private", rawMessage, LocalDateTime.now(), rawMessage.startsWith("[Private to"));
        }

        // handle regular messages
        int colonIndex = rawMessage.indexOf(": ");
        if (colonIndex > 0) {
            String sender = rawMessage.substring(0, colonIndex);
            String messageContent = rawMessage.substring(colonIndex + 2);
            return new ChatMessage(sender, messageContent, LocalDateTime.now(), sender.equals(username));
        }
        
        // Fallback incase of other message formats
        return new ChatMessage("System", rawMessage, LocalDateTime.now(), false);
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
