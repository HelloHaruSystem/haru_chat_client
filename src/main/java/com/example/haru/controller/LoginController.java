package com.example.haru.controller;

import com.example.haru.model.AuthModel;
import com.example.haru.util.TokenManager;
import com.example.haru.view.LoginView;

import javafx.application.Platform;

public class LoginController {
    private final LoginView view;
    private final AuthModel authModel;
    private final NavigationController navigationController;

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
        // potentially do an animation
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
                        this.view.setStatusMessage("Login successful!", false);
                        this.view.clearFields();
                    });
                } else {
                    Platform.runLater(() -> {
                        this.view.setStatusMessage("Login failed. Invalid credentials", false);
                    });
                }
            } catch (Exception e) {
                Platform.runLater(() -> {
                    this.view.setStatusMessage("Login failed. Invalid credentials.", true);
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
