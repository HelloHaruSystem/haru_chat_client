package com.example.haru.view;

import com.example.haru.controller.ChatController;
import com.example.haru.model.ChatMessage;
import com.example.haru.model.MessageStore;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class ChatView {
    private BorderPane root;
    private ListView<ChatMessage> messageList;
    private TextField messageInputField;
    private Button sendMessageButton;

    public ChatView(ChatController controller) {
        createUI();
        bindToController(controller);
    }

    private void createUI() {
        // create ui elements 
        this.root = new BorderPane();
        messageList = new ListView<>();
        messageInputField = new TextField();
        sendMessageButton = new Button("Send");

        // setup layouts
        HBox inputArea = new HBox(10, messageInputField, sendMessageButton);
        root.setCenter(messageList);
        root.setBottom(inputArea);

        // style components
        //TODO: do this in css later
        messageList.setCellFactory(param -> new MessageCell());
    }

    private void bindToController(ChatController controller) {
        // connects the ui components to the controller
        sendMessageButton.setOnAction(event -> controller.sendMessage(messageInputField.getText())); // if you click send
        messageInputField.setOnAction(event -> controller.sendMessage(messageInputField.getText())); // if you press enter in the text field
    }

    public void clearMessageInput() {
        messageInputField.clear();
    }

    // bind the model data
    public void bindToModel(MessageStore messageStore) {
        messageList.setItems(messageStore.getMessages());
    }

    // getters and setters 
    // here we just return the root node for the chat
    public BorderPane getRoot() {
        return this.root;
    }
}
