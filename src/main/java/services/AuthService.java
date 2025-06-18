package services;

import config.Config;
import constants.ApiEndpoints;
import io.restassured.response.Response;
import models.auth.AuthRequest;
import models.auth.AuthResponse;
import models.auth.PasswordChangeRequest;
import models.auth.UserLoginResponse;
import utils.ApiClient;

public class AuthService {

    public static String authenticate(String login, String password) {
        AuthRequest credentials = new AuthRequest(login, password);

        return ApiClient.post(ApiEndpoints.LOGIN, credentials)
                .then()
                .statusCode(200)
                .extract()
                .as(AuthResponse.class)
                .getAuthorization();
    }

    public static String authenticate() {
        return authenticate(Config.getAuthLogin(), Config.getAuthPassword());
    }


    public static UserLoginResponse getUserLogin(String token) {
        return ApiClient.getAuthenticated(ApiEndpoints.USER_LOGIN, token)
                .then()
                .statusCode(200)
                .extract()
                .as(UserLoginResponse.class);
    }

    public static Response changePassword(PasswordChangeRequest request, String token) {
        return ApiClient.postAuthenticated(ApiEndpoints.PASSWORD_CHANGE, request, token);
    }

    // Метод для получения сырого ответа (для обработки ошибок)
    public static Response getUserLoginResponse(String token) {
        return ApiClient.getAuthenticated(ApiEndpoints.USER_LOGIN, token);
    }

}