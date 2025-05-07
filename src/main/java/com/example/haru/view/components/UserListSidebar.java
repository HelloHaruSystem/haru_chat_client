package com.example.haru.view.components;

import com.example.haru.model.MessageStore;

import javafx.animation.TranslateTransition;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;

public class UserListSidebar extends BorderPane {
    private final StackPane container;
    private final VBox sidebar;
    private final Button toggleButton;
    private final ListView<String> userList;
    private final Label userCount;
    private boolean isExpanded = true;

    // size constants
    private static final double SIDEBAR_WIDTH = 200.0;
    private static final double COLLAPSED_WIDTH = 40.0;
    // 250ms
    private static final double ANIMATION_DURATION = 250;

    // SVG (Scalable Vector Graphics) paths for toggle button icons
    // path strings that represent icons
    private static final String CHEVRON_LEFT = "M15.41 16.59L10.83 12l4.58-4.59L14 6l-6 6 6 6 1.41-1.41z";
    private static final String CHEVRON_RIGHT = "M8.59 16.59L13.17 12 8.59 7.41 10 6l6 6-6 6-1.41-1.41z";

    public UserListSidebar(MessageStore messageStore) {
        // set style class
        this.getStyleClass().add("user-list-sidebar");

        // create the sidebar
        this.sidebar = new VBox(10);
        this.sidebar.getStyleClass().add("user-list-sidebar-content");
        this.sidebar.setPrefWidth(SIDEBAR_WIDTH);
        this.sidebar.setMinWidth(SIDEBAR_WIDTH);
        this.sidebar.setMaxWidth(SIDEBAR_WIDTH);

        // create the top with user count
        HBox header = new HBox(5);
        header.getStyleClass().add("user-list-header");
        header.setPadding(new Insets(10));
        header.setAlignment(Pos.CENTER_LEFT);

        Label usersLabel = new Label("Active Users");
        this.userCount = new Label("(0)");

        header.getChildren().addAll(usersLabel, userCount);

        // create the user list
        this.userList = new ListView<>();
        this.userList.setCellFactory(param -> new UserListCell());
        this.userList.getStyleClass().add("user-list");

        // bind it to the message store
        this.userList.setItems(messageStore.activeUsers());

        // update count with changes
        messageStore.activeUsers().addListener((ListChangeListener<String>) change -> {
            while (change.next()) {
                updateUserCount(messageStore.activeUsers().size());
            }
        });

        // add components to the sidebar
        VBox.setVgrow(this.userList, Priority.ALWAYS);
        this.sidebar.getChildren().addAll(header, this.userList);

        // init toggle button
        this.toggleButton = createToggleButton();

        // create the container for the user list and toggle button
        this.container = new StackPane();
        this.container.getChildren().addAll(this.sidebar, this.toggleButton);

        // position the button
        StackPane.setAlignment(toggleButton, Pos.CENTER_RIGHT);
        StackPane.setMargin(toggleButton, new Insets(0, -20, 0, 0));

        // set the container as the center of this BorderPane
        this.setCenter(this.container);

        // initialize the count update
        updateUserCount(messageStore.activeUsers().size());
    }

    private Button createToggleButton() {
        // create the icon
        SVGPath chevronIcon = new SVGPath();
        chevronIcon.setContent(CHEVRON_LEFT);
        chevronIcon.getStyleClass().add("toggle-icon");

        // create the button
        Button button = new Button();
        button.setGraphic(chevronIcon);
        button.getStyleClass().add("toggle-button");

        // set on action for the toggle button
        button.setOnAction(event -> toggleSidebar());

        return button;
    }

    // remove if problems with this
    private void toggleSidebar() {
        // Create animation for smooth transition
        TranslateTransition transition = new TranslateTransition(Duration.millis(ANIMATION_DURATION), sidebar);
        
        if (isExpanded) {
            // Collapse the sidebar
            transition.setToX(-SIDEBAR_WIDTH + COLLAPSED_WIDTH);
            
            // Update button icon
            SVGPath icon = new SVGPath();
            icon.setContent(CHEVRON_RIGHT);
            icon.getStyleClass().add("toggle-icon");
            toggleButton.setGraphic(icon);
        } else {
            // Expand the sidebar
            transition.setToX(0);
            
            // Update button icon
            SVGPath icon = new SVGPath();
            icon.setContent(CHEVRON_LEFT);
            icon.getStyleClass().add("toggle-icon");
            toggleButton.setGraphic(icon);
        }
        
        // Play the animation
        transition.play();
        
        // Toggle state
        isExpanded = !isExpanded;
    }

    private void updateUserCount(int count) {
        this.userCount.setText("(" + count + ")");
    }

    public void Collapse() {
        if (this.isExpanded) {
            toggleSidebar();
        }
    }

    public void expand() {
        if (!isExpanded) {
            toggleSidebar();
        }
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        setPrefWidth(isExpanded ? SIDEBAR_WIDTH : COLLAPSED_WIDTH);
        setMinWidth(isExpanded ? SIDEBAR_WIDTH : COLLAPSED_WIDTH);
    }

    // getters and setters
    public boolean getIsExpanded() {
        return this.isExpanded;
    }
}
