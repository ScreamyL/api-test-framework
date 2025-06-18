package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import models.auth.UserLoginResponse;
import models.common.ErrorResponse;

public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static ErrorResponse safeParseError(Response response) {
        try {
            String body = response.asString();
            if (body == null || body.isEmpty()) {
                return createDefaultError(response.statusCode());
            }
            return mapper.readValue(body, ErrorResponse.class);
        } catch (Exception e) {
            return createDefaultError(response.statusCode());
        }
    }

    public static UserLoginResponse parseUserLoginResponse(Response response) {
        try {
            return mapper.readValue(response.asString(), UserLoginResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse UserLoginResponse: " + e.getMessage());
        }
    }

    private static ErrorResponse createDefaultError(int statusCode) {
        ErrorResponse error = new ErrorResponse();
        error.setCode("PARSE_ERROR");
        error.setMessage("Failed to parse error response. Status code: " + statusCode);
        return error;
    }
}