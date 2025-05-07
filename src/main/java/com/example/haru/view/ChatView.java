package com.example.haru.view;

import com.example.haru.controller.ChatController;
import com.example.haru.model.ChatMessage;
import com.example.haru.model.MessageStore;
import com.example.haru.view.components.MessageCell;
import com.example.haru.view.components.TopBar;
import com.example.haru.view.components.UserListSidebar;

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
    private UserListSidebar userListSidebar;
    private TopBar topBar;

    public ChatView(ChatController controller) {
        createUI(controller.getMessageStore());
        bindToController(controller);

        this.topBar.updateWelcomeMessage(controller.getUsername());
        setupTopBarActions(controller);
    }

    private void createUI(MessageStore messageStore) {
        // create ui elements 
        this.root = new BorderPane();
        this.messageList = new ListView<>();
        this.messageInputField = new TextField();
        this.sendMessageButton = new Button("Send");
        this.userListSidebar = new UserListSidebar(messageStore);
        this.topBar = new TopBar();
        
        // add the css styling
        this.root.getStyleClass().add("chat-view");
        this.messageList.getStyleClass().add("message-list");
        this.messageInputField.getStyleClass().add("message-input");
        this.sendMessageButton.getStyleClass().add("send-button");

        // setup layouts
        HBox inputArea = new HBox(10, messageInputField, sendMessageButton);
        inputArea.getStyleClass().add("input-area");

        // make text field expand if needed
        HBox.setHgrow(messageInputField, Priority.ALWAYS);
        
        // set the layout
        this.root.setTop(this.topBar);
        this.root.setCenter(this.messageList);
        this.root.setBottom(inputArea);
        this.root.setLeft(this.userListSidebar);

        // style components
        this.messageList.setCellFactory(param -> new MessageCell());
    }

    // loads the CSS stylesheet for this view
    public void loadStyleSheet(javafx.scene.Scene scene) {
        scene.getStylesheets().add(getClass().getResource("/com/example/haru/css/chat-styles.css").toExternalForm());
    }

    private void bindToController(ChatController controller) {
        // connects the ui components to the controller
        this.sendMessageButton.setOnAction(event -> controller.sendMessage(messageInputField.getText())); // if you click send
        this.messageInputField.setOnAction(event -> controller.sendMessage(messageInputField.getText())); // if you press enter in the text field
    }

    // actions for the top bar
    private void setupTopBarActions(ChatController controller) {
        this.topBar.setClearChatAction(() -> controller.clearMessages());
        this.topBar.setLogoutAction(() -> controller.logout());
        
        // yet to be implemented
        this.topBar.setServerSelectAction(() -> {
            // do this properly in the future
            controller.getMessageStore().addSystemMessage("Server selection not implemented yet.");
        });

        // also yet to be implemented :)
        this.topBar.setPrivateMessageAction(() -> {
            // do this properly in the future
            controller.getMessageStore().addSystemMessage("Private messaging not implemented yet.");
        });

        // same as above :/
        this.topBar.setHelpMessageAction(() -> {
            // do this properly in the future
            controller.getMessageStore().addSystemMessage("Help: Use /clear to clear the chat history.");
        });

    }

    public void clearMessageInput() {
        messageInputField.clear();
    }

    // bind the model data
    public void bindToModel(MessageStore messageStore) {
        this.messageList.setItems(messageStore.getMessages());

        // auto-scroll
        // TODO: Make this work in a nice way instead of this mess :)
        // TODO: sometimes doesn't auto scroll
        // TODO: if you scroll up make it so it doesn't auto scroll when a new message is recieved
        messageStore.getMessages().addListener((ListChangeListener<ChatMessage>) change -> {
            while (change.next()) {
               
                Platform.runLater(() -> {
                    scrollToBottom();
                    this.messageInputField.requestFocus();
                });
            }
        });
    }

    public void scrollToBottom() {
        int size = this.messageList.getItems().size();
        if (size > 0) {
            this.messageList.scrollTo(size -1);
        } 
    }

    // Toggle the user list visibility
    public void toggleUserList() {
        if (this.userListSidebar != null) {
            if (this.userListSidebar.getIsExpanded()) {
                this.userListSidebar.Collapse();
            } else {
                this.userListSidebar.expand();
            }
        }
    }

    // getters and setters 
    // here we just return the root node for the chat
    public BorderPane getRoot() {
        return this.root;
    }

    public UserListSidebar getUserListSidebar() {
        return this.userListSidebar;
    }
}
