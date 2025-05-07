package com.example.haru.model;

import java.time.LocalDateTime;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// For managing local message data
public class MessageStore {
    private final ObservableList<ChatMessage> messages = FXCollections.observableArrayList();
    private final ObservableList<String> activeUsersNames = FXCollections.observableArrayList();

    // System username for messages
    private static final String SYSTEM_USERNAME = "System";

    public void addMessage(ChatMessage chatMessage) {
        this.messages.add(chatMessage);
    }

    public void addSystemMessage(String chatMessage) {
        ChatMessage systemMessage = new ChatMessage(
            this.SYSTEM_USERNAME,
            chatMessage,
            LocalDateTime.now(),
            false
        );
        this.addMessage(systemMessage);
    }

    public void removeMessage(ChatMessage chatMessage) {
        this.messages.remove(chatMessage);
    }

    public void addActiveUser(String username) {
        if (username != null && !username.isEmpty() && !this.activeUsersNames.contains(username)) {
            this.activeUsersNames.add(username);
            
            // Sort the list alphabetically
            FXCollections.sort(activeUsersNames);
        }
    }

    public void removeActiveUser(String username) {
        if (username != null && !username.isEmpty()) {
            this.activeUsersNames.remove(username);
        }
    }

    public void updateActiveUsers(String[] users) {
        this.activeUsersNames.clear();
        
        if (users != null && users.length > 0) {
            Arrays.stream(users)
                .filter(user -> user != null && !user.isEmpty())
                .forEach(this.activeUsersNames::add);
            
            // sort the collection alphabetically
            FXCollections.sort(this.activeUsersNames);
        }
    }

    public boolean isUserActive(String username) {
        return username != null && !username.isEmpty() && this.activeUsersNames.contains(username);
    }

    public void clearMessages() {
        messages.clear();
        addSystemMessage("Message history cleared.");
    }

    // getters and setters
    public ObservableList<ChatMessage> getMessages() {
        return this.messages;
    }

    public ObservableList<String> activeUsers() {
        return this.activeUsersNames;
    }

    public int getActiveUserCount() {
        return this.activeUsersNames.size();
    }
}
