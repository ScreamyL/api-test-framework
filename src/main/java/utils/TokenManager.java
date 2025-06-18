package utils;

import services.AuthService;

public class TokenManager {
    private static String authToken;

    public static synchronized String getToken() {
        if (authToken == null) {
            authToken = AuthService.authenticate();
        }
        return authToken;
    }

    public static void resetToken() {
        authToken = null;
    }
}