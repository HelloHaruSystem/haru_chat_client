package com.example.haru.model;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.json.JSONObject;

import com.example.haru.config.AppConfig;
import com.example.haru.util.TokenManager;

public class AuthModel {
    private final AppConfig config;
    private final HttpClient httpClient;

    // timeout constant 
    private static final int TIMEOUT_SECONDS = 10;

    public AuthModel() {
        this.config = AppConfig.getInstance();
        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(TIMEOUT_SECONDS))
            .build();

        //TODO: remove later just for debugging
        // Debug output - print configuration
        System.out.println("API Base URL: " + this.config.getDefaultAPIBaseUrl());
        System.out.println("Login Endpoint: " + this.config.getDefaultLoginEndpoint());
        System.out.println("Register Endpoint: " + this.config.getDefaultRegisterEndPoint());
    }

    public String login(String username, String password) throws IOException, InterruptedException {
        URI uri = URI.create(this.config.getDefaultAPIBaseUrl() + this.config.getDefaultLoginEndpoint());
        String uriString = this.config.getDefaultAPIBaseUrl() + this.config.getDefaultLoginEndpoint(); // debug
        System.out.println("Login URI: " + uriString); // debug

        // create request body
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", username);
        requestBody.put("password", password);
        System.out.println("Request Body: " + requestBody.toString()); // debug

        // create the request
        HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
            .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
            .build();
        
            System.out.println("Sending login request..."); // debug

        // send the request
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // handle the response
        int statusCode = response.statusCode();
        String responseBody = response.body();

        System.out.println("Response Status Code: " + statusCode); // debug
        System.out.println("Response Body: " + responseBody); // debug

        if (statusCode == 200) {
            try {
                JSONObject jsonResponse = new JSONObject(responseBody);

                // check for the success flag send from auth server
                if (jsonResponse.has("success") && jsonResponse.getBoolean("success")) {
                    
                    String token = jsonResponse.getString("token");                         // debug
                    System.out.println("Token received: " + token.substring(0, 20) + "...");    // debug
                    
                    return jsonResponse.getString("token");
                } else {
                    System.out.println("Login failed: " +
                        (jsonResponse.has("message") ? jsonResponse.getString("message") : "Unknown error"));
                    return null;
                }
            } catch (Exception e) {
                System.out.println("Error parsing JSON response: " + e.getMessage());
                return null;
            }
        } else {
            System.out.println("Login failed with status code: " + statusCode);
            System.out.println("Response: " + responseBody);
            return null;
        }
    }

    public boolean register(String username, String password) throws IOException, InterruptedException {
        URI uri = URI.create(this.config.getDefaultAPIBaseUrl() + this.config.getDefaultRegisterEndPoint());

        // create request body
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", username);
        requestBody.put("password", password);

        // create the request
        HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
            .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
            .build();
        
        // send the request
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // handle the response
        int statusCode = response.statusCode();
        String responseBody = response.body();

        if (statusCode == 201) {
            try {
                JSONObject jsonResponse = new JSONObject(responseBody);

                // check for the success flag send from the auth server
                if (jsonResponse.has("success") && jsonResponse.getBoolean("success")) {
                    return true;
                } else {
                    System.out.println("Registration failed: " +
                        (jsonResponse.has("message") ? jsonResponse.getString("message") : "Unknown error"));
                    return false;
                }
            } catch (Exception e) {
                System.out.println("Error parsing JSON response: " + e.getMessage());
                return false;
            }
        } else {
            System.out.println("Registration failed with status code: " + statusCode);
            System.out.println("Response: " + responseBody);
            return false;
        }
    }

    //TODO: Finish this method
    // look at return
    public void getCurrentUserInfo() throws IOException, InterruptedException {
        URI uri = URI.create(this.config.getDefaultAPIBaseUrl() + this.config.getDefaultRegisterEndPoint());

        // create the request
        HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .header("Authorization", "Bearer " + TokenManager.getToken())
            .GET()
            .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
            .build();
        
        // send the request
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // handle the response
        int statusCode = response.statusCode();
        String responseBody = response.body();

        if (statusCode == 200) {
            try {
                JSONObject jsonResponse = new JSONObject(responseBody);

                // check for the success flag send from the auth server
                if (jsonResponse.has("success") && jsonResponse.getBoolean("success")) {
                    return;
                } else {
                    System.out.println("fetching user information failed: " +
                        (jsonResponse.has("message") ? jsonResponse.getString("message") : "Unknown error"));
                    return;
                }
            } catch (Exception e) {
                System.out.println("Error parsing JSON response: " + e.getMessage());
            }
        } else {
            System.out.println("Fetching failed with status code: " + statusCode);
            System.out.println("Response: " + responseBody);
        }
    }
    
    //TODO: add feature
    public String refreshToken(String currentToken) {
        throw new UnsupportedOperationException("Feature incomplete.");
    }
}
