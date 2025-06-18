package utils;

import config.Config;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiClient {
    static {
        RestAssured.baseURI = Config.getBaseUrl();
        RestAssured.filters(new AllureRestAssured());
        RestAssured.defaultParser = Parser.JSON; // Устанавливаем JSON-парсер по умолчанию
    }

    public static RequestSpecification getAuthenticatedRequest(String token) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token);
    }

    public static Response post(String path, Object body) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .post(path);
    }

    public static Response postAuthenticated(String path, Object body, String token) {
        return getAuthenticatedRequest(token)
                .body(body)
                .post(path);
    }

    public static Response getAuthenticated(String path, String token) {
        return getAuthenticatedRequest(token).get(path);
    }

    public static Response get(String path) {
        return RestAssured.given().get(path);
    }
}