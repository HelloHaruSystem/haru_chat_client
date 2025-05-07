package com.example.haru.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

import com.example.haru.config.AppConfig;

public class AuthModel {
    private final AppConfig config;

    public AuthModel() {
        this.config = AppConfig.getInstance();
    }

    public String login(String username, String password) throws IOException {
        URI uri = URI.create(this.config.getDefaultAPIBaseUrl() + this.config.getDefaultLoginEndpoint());
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        try {
            // Set up the request
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            
            // Create the request body
            JSONObject requestBody = new JSONObject();
            requestBody.put("username", username);
            requestBody.put("password", password);
            
            // Send the request
            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            }
            
            // Handle the response
            int statusCode = connection.getResponseCode();
            
            if (statusCode == 200) {
                // Read the response
                try (BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = bufferedReader.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    
                    // Parse the JSON response
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    return jsonResponse.getString("token");
                }
            } else {
                //TODO: Handle error response
                return null;
            }
        } catch (IOException e) {
            System.out.println("Network error during login: " + e.getMessage());
            throw e;
        } finally {
            connection.disconnect();
        }
    }

    public boolean register(String username, String password) throws IOException {
        URI uri = URI.create(this.config.getDefaultAPIBaseUrl() + this.config.getDefaultRegisterEndPoint());
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            // set up the request
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            // create the request body
            JSONObject requestBody = new JSONObject();
            requestBody.put("username", username);
            requestBody.put("password", password);

            // send the request
            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            }

            // handle the response
            int statusCode = connection.getResponseCode();

            if (statusCode != 201) {
                //TODO: read error response for logging
            }
            // 201 created if successful
            return statusCode == 201;
        } catch (IOException e) {
            System.out.println("Network error during registration: " + e.getMessage());
            throw e;
        } finally {
            connection.disconnect();
        }
    }

    //TODO: add feature
    public String refreshToken(String currentToken) {
        throw new UnsupportedOperationException("Feature incomplete.");
    }
}
