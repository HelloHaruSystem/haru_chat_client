package com.example.haru.controller;

import com.example.haru.config.AppConfig;
import com.example.haru.model.AuthModel;
import com.example.haru.model.ConnectionModel;
import com.example.haru.util.TokenManager;
import com.example.haru.view.LoginView;

import javafx.application.Platform;

public class LoginController {
    private final LoginView view;
    private final AuthModel authModel;
    private final NavigationController navigationController;

    // delay on success constant
    private static final int SUCCESS_DELAY_MS = 750; // 0.75 seconds

    public LoginController(NavigationController navigationController) {
        this.navigationController = navigationController;
        this.authModel = new AuthModel();
        this.view = new LoginView(this);
    }

    //TODO: Split this method into samller methods 
    public void login(String username, String password) {
        // validate input given
        if (!validateLoginInput(username, password)) {
            return;
        }

        // show a loading message to the user
        // potentially do an animation in the future
        this.view.setStatusMessage("Logging in...", false);

        // Do the login in a separate thread to keep UI responsive
        new Thread(() -> {
            try {
                String token = this.authModel.login(username, password);

                if (token != null && !token.isEmpty()) {
                    // store the JWT token
                    TokenManager.setToken(token);

                    // update UI on the UI thread (javafx thread)
                    Platform.runLater(() -> {
                        this.view.setStatusMessage("Auth server login successful! Connecting to chat server", false);
                        this.view.clearFields();
                    });

                    // now, verify authentication with the chat server before changing to chat view
                    AppConfig config = AppConfig.getInstance();
                    ConnectionModel tempConnection = new ConnectionModel();
                    boolean chatAuthSuccessful = tempConnection.verifyAuthentication(username, token, config.getDefaultServerAddress(), config.getDefaultServerPort());

                    if(chatAuthSuccessful) {
                        // incase of both auth and chat server authentication are successful
                        Platform.runLater(() -> {
                            this.view.setStatusMessage("login successful!", false);
                            this.view.clearFields();

                            navigationController.showChatScreen(username);
                        });
                    } else {
                        // Auth server success but chat server failed
                        Platform.runLater(() -> {
                            this.view.setStatusMessage("Chat server authentication failed. Token may be invalid.", true);
                            TokenManager.clearToken();
                        });
                    }
                } else {
                    Platform.runLater(() -> {
                        this.view.setStatusMessage("Login failed. Invalid credentials", true);
                    });
                }
            } catch (Exception e) {
                Platform.runLater(() -> {
                    this.view.setStatusMessage("Error: " + e.getMessage(), true);
                });
            }
        }).start(); 
    }

    public void register (String username, String password) {
        // validate input given
        if (!validateLoginInput(username, password)) {
            return;
        }

        // display loading message
        this.view.setStatusMessage("Registering...", false);

        // Do the the registration on a separate thread to keep the UI responsive
        new Thread(() -> {
            try {
                boolean success = authModel.register(username, password);

                if (success) {
                    Platform.runLater(() -> {
                        this.view.setStatusMessage("Registration successful! You can now login.", false);

                        try {
                            Thread.sleep(SUCCESS_DELAY_MS);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }

                    });
                } else {
                    Platform.runLater(() -> {
                        this.view.setStatusMessage("Registration failed. Username may be taken", true);
                    });
                }
            } catch (Exception e) {
                Platform.runLater(() -> {
                    this.view.setStatusMessage("Error: " + e.getMessage(), true);
                });
            }
        }).start();
    }

    // returns true if valid input was given false if not
    private boolean validateLoginInput(String username, String password) {
        // validate input given
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            this.view.setStatusMessage("Username and password are required!", true);
            return false;
        }
        return true;
    }

    // getters and setters
    public LoginView getView() {
        return this.view;
    }
}
