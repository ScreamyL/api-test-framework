package services;

import constants.ApiEndpoints;
import io.restassured.response.Response;
import models.email.SendEmailRequest;
import utils.ApiClient;

public class EmailService {

    public static Response sendEmail(SendEmailRequest request, String token) {
        return ApiClient.postAuthenticated(ApiEndpoints.SEND_EMAIL, request, token);
    }
}