package com.example.haru.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatMessage {
    private final String sender;
    private final String content;
    private final LocalDateTime timeStamp;
    private final boolean isMessageFromCurrentUser;

    // constructor
    public ChatMessage(String sender, String content, LocalDateTime timeStamp, boolean isMessageFromCurrentUser) {
        this.sender = sender;
        this.content = content;
        this.timeStamp = timeStamp;
        this.isMessageFromCurrentUser = isMessageFromCurrentUser;
    }

    // getters and setters
    public String getSender() {
        return this.sender;
    }

    public String getContent() {
        return this.content;
    }

    public LocalDateTime getTimeStamp() {
        return this.timeStamp;
    }

    public boolean isFromCurrentUser() {
        return this.isMessageFromCurrentUser;
    }

    public String getFormattedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:MM:SS");
        return this.timeStamp.format(formatter);
    }
}
