package com.example.haru.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// For managing local message data
public class MessageStore {
    private final ObservableList<ChatMessage> messages = FXCollections.observableArrayList();
    private final ObservableList<String> activeUsersNames = FXCollections.observableArrayList();

    public void addMessage(ChatMessage chatMessage) {
        this.messages.add(chatMessage);
    }

    public void removeMessage(ChatMessage chatMessage) {
        this.messages.remove(chatMessage);
    }

    //TODO: add methods to update remove active users
    public void addActiveUser(String username) {
        if (!this.activeUsersNames.contains(username)) {
            this.activeUsersNames.add(username);
        }
    }

    public void removeActiveUser(String username) {
        this.activeUsersNames.remove(username);
    }

    public void updateActiveUsers(String[] users) {
        this.activeUsersNames.clear();
        if (users != null) {
           activeUsersNames.addAll(users); 
        }
    }

    public void clearMessages() {
        messages.clear();
    }

    // getters and setters
    public ObservableList<ChatMessage> getMessages() {
        return this.messages;
    }

    public ObservableList<String> activeUsers() {
        return this.activeUsersNames;
    }

}
