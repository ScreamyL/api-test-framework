package services;

import constants.ApiEndpoints;
import io.restassured.response.Response;
import utils.ApiClient;

public class CreditService {

    public static Response getCreditParameters(String loanType, String fields, String token) {
        String endpoint = ApiEndpoints.CREDIT_PARAMETERS +
                "?loan_type=" + loanType +
                (fields != null ? "&fields=" + fields : "");

        return ApiClient.getAuthenticated(endpoint, token);
    }

    public static Response getBankConfig(String token) {
        return ApiClient.getAuthenticated(ApiEndpoints.BANK_CONFIG, token);
    }

    public static Response getCreditParameters() {
        return ApiClient.get(ApiEndpoints.CREDIT_PARAMETERS);
    }
}