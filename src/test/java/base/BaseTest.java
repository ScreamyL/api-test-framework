package base;

import config.Config;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import utils.TokenManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class BaseTest {

    private ByteArrayOutputStream requestOutputStream;
    private ByteArrayOutputStream responseOutputStream;
    private PrintStream requestPrintStream;
    private PrintStream responsePrintStream;

    @BeforeAll
    public static void globalSetup() {
        RestAssured.baseURI = Config.getBaseUrl();
        // Предварительная аутентификация для всех тестов
        TokenManager.getToken();
    }

    @BeforeEach
    public void setup() {
        // Настройка логирования запросов/ответов для Allure
        requestOutputStream = new ByteArrayOutputStream();
        requestPrintStream = new PrintStream(requestOutputStream);

        responseOutputStream = new ByteArrayOutputStream();
        responsePrintStream = new PrintStream(responseOutputStream);

        RestAssured.filters(
                new RequestLoggingFilter(requestPrintStream),
                new ResponseLoggingFilter(responsePrintStream)
        );
    }

    @AfterEach
    public void teardown() {
        // Прикрепление логов к Allure отчету
        attachLogs();
        RestAssured.reset();
    }

    @Step("Прикрепление логов запроса/ответа")
    private void attachLogs() {
        String requestLog = requestOutputStream.toString();
        if (!requestLog.isEmpty()) {
            Allure.addAttachment("Request", "text/plain", requestLog);
        }

        String responseLog = responseOutputStream.toString();
        if (!responseLog.isEmpty()) {
            Allure.addAttachment("Response", "text/plain", responseLog);
        }
    }

    @Step("Сброс токена авторизации")
    protected void resetAuthToken() {
        TokenManager.resetToken();
    }
}