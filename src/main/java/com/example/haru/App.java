package com.example.haru;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// this is jsut for testing a gui will be implemented
public class App {
    public static void main( String[] args ) {
        if (args.length < 3) {
            System.out.println("Usage: java TestClient <server-ip> <port> <username>");
            return;
        }

        String serverAddress = args[0];
        int port = Integer.parseInt(args[1]);
        String username = args[2];

        try {
            Socket socket = new Socket(serverAddress, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // true for auto-flush
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // clear screen after connection
            clearScreen();
            System.out.println("Connected to chat server as: " + username);
            System.out.println("Type your messages below. /clear to clear the screen.");
            System.out.println("-----------------------------------------------------");

            // sets username
            out.println(username);
            out.flush(); // ensure username is send immediately

            // start a new thread to receive messages
            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server " + e.getMessage());
                }
            }).start();

            // read user input and send messages
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            String input;
            while ((input = userInput.readLine()) != null) {
                out.println(input);
                if (input.equals("/quit")) {
                    break;
                } else if (input.equals("/clear")) {
                    clearScreen();
                    System.out.println("Screen cleared.");
                }
            }

            // clean everything up
            socket.close();
            out.close();
            in.close();
            userInput.close();
            
        } catch (IOException e) {
            System.out.println("Error trying to connect to chat server " + e.getMessage());
        }
    }

    public static void clearScreen() {
        try {
            String os = System.getProperty("os.name").toLowerCase();

            ProcessBuilder processBuilder;
            if (os.contains("windows")) {
                processBuilder = new ProcessBuilder("cmd", "/c", "cls");
            } else {
                processBuilder = new ProcessBuilder("clear");
            }

            Process process = processBuilder.inheritIO().start();
            process.waitFor();
        } catch (Exception e) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
}
