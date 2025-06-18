package tests.email;

import base.BaseTest;
import constants.ErrorMessages;
import io.qameta.allure.*;
import models.common.ErrorResponse;
import models.email.SendEmailRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import services.EmailService;
import utils.TokenManager;
import utils.TestDataGenerator;

import static io.qameta.allure.Allure.step;
import static io.qameta.allure.SeverityLevel.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Epic("API тесты для сервиса отправки email")
@Feature("Отправка писем")
@Tag("email")
public class EmailServiceTests extends BaseTest {

    @Test
    @DisplayName("KRK-T45: Успешная отправка email")
    @Description("Тест проверяет успешную отправку email")
    @Severity(BLOCKER)
    @Story("Успешная отправка")
    public void sendEmailSuccessfully() {
        String token = TokenManager.getToken();
        SendEmailRequest request = TestDataGenerator.createEmailRequest(
                new String[]{"recipient@example.com"},
                "Test Subject",
                "Test Content"
        );

        step("Отправить запрос на отправку email", () -> {
            EmailService.sendEmail(request, token)
                    .then()
                    .statusCode(200);
        });
    }

    @Test
    @DisplayName("KRK-T48: Ошибка 400 при невалидном email")
    @Description("Тест проверяет обработку невалидного email адреса")
    @Severity(CRITICAL)
    @Story("Обработка ошибок")
    public void sendEmailWithInvalidRecipient() {
        String token = TokenManager.getToken();
        SendEmailRequest request = TestDataGenerator.createEmailRequest(
                new String[]{"invalid-email"},
                "Test Subject",
                "Test Content"
        );

        step("Отправить запрос с невалидным email", () -> {
            ErrorResponse response = EmailService.sendEmail(request, token)
                    .then()
                    .statusCode(400)
                    .extract()
                    .as(ErrorResponse.class);

            assertThat(response.getCode(), equalTo("ERROR"));
            assertThat(response.getMessage(), containsString(ErrorMessages.INVALID_EMAIL_FORMAT));
        });
    }

    @Test
    @DisplayName("KRK-T49: Ошибка 401 при невалидном токене")
    @Description("Тест проверяет обработку невалидного токена при отправке email")
    @Severity(CRITICAL)
    @Story("Обработка ошибок")
    public void sendEmailWithInvalidToken() {
        resetAuthToken(); // Сбрасываем токен
        SendEmailRequest request = TestDataGenerator.createEmailRequest(
                new String[]{"recipient@example.com"},
                "Test Subject",
                "Test Content"
        );

        step("Отправить запрос с невалидным токеном", () -> {
            ErrorResponse response = EmailService.sendEmail(request, "invalid_token")
                    .then()
                    .statusCode(401)
                    .extract()
                    .as(ErrorResponse.class);

            assertThat(response.getCode(), equalTo("ERROR"));
            assertThat(response.getMessage(), containsString(ErrorMessages.UNAUTHORIZED));
        });
    }

    @Test
    @DisplayName("KRK-T47: Отправка email с вложением")
    @Description("Тест проверяет отправку email с вложением")
    @Severity(NORMAL)
    @Story("Успешная отправка")
    public void sendEmailWithAttachment() {
        String token = TokenManager.getToken();
        byte[] fileContent = "Test file content".getBytes();

        SendEmailRequest request = TestDataGenerator.createEmailWithAttachment(
                new String[]{"recipient@example.com"},
                "Test Subject",
                "Test Content",
                "test.txt",
                "text/plain",
                fileContent
        );

        step("Отправить запрос с вложением", () -> {
            EmailService.sendEmail(request, token)
                    .then()
                    .statusCode(200);
        });
    }
}