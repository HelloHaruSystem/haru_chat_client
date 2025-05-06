package com.example.haru.util;

public class TokenManager {
    private static String jwtToken;

    public static void setToken(String token) {
        jwtToken = token;
    }

    public static String getToken() {
        return jwtToken;
    }

    public static void clearToken() {
        jwtToken = null;
    }

    public static boolean hasToken() {
        return jwtToken != null && !jwtToken.trim().isEmpty();
    }
}
