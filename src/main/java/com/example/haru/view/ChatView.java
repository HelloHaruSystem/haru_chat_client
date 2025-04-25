package com.example.haru.view;

import com.example.haru.controller.ChatController;
import com.example.haru.model.ChatMessage;
import com.example.haru.model.MessageStore;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

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

        // add the css styling
        root.getStyleClass().add("chat-view");
        messageList.getStyleClass().add("message-list");
        messageInputField.getStyleClass().add("message-input");
        sendMessageButton.getStyleClass().add("send-button");

        // setup layouts
        HBox inputArea = new HBox(10, messageInputField, sendMessageButton);
        inputArea.getStyleClass().add("input-area");

        // make text field expand if needed
        HBox.setHgrow(messageInputField, Priority.ALWAYS);
        
        // set the layout
        root.setCenter(messageList);
        root.setBottom(inputArea);

        // style components
        messageList.setCellFactory(param -> new MessageCell());
    }

    // loads the CSS stylesheet for this view
    public void loadStyleSheet(javafx.scene.Scene scene) {
        scene.getStylesheets().add(getClass().getResource("/com/example/haru/css/chat-styles.css").toExternalForm());
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

        // auto-scroll
        // TODO: Make this work in a nice way instead of this mess :)
        messageStore.getMessages().addListener((ListChangeListener<ChatMessage>) change -> {
            while (change.next()) {
               
                Platform.runLater(() -> {
                    scrollToBottom();
                    messageInputField.requestFocus();
                });
            }
        });
    }

    public void scrollToBottom() {
        int size = messageList.getItems().size();
        if (size > 0) {
            messageList.scrollTo(size -1);
        } 
    }

    // getters and setters 
    // here we just return the root node for the chat
    public BorderPane getRoot() {
        return this.root;
    }
}
