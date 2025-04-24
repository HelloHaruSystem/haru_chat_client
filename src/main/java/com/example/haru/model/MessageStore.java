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

    // getters and setters
    public ObservableList<ChatMessage> getMessages() {
        return this.messages;
    }

    public ObservableList<String> activeUsers() {
        return this.activeUsersNames;
    }

}
