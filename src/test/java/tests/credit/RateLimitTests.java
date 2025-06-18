package tests.credit;

import base.BaseTest;
import constants.ErrorMessages;
import io.qameta.allure.*;
import models.common.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import services.CreditService;
import utils.TokenManager;

import static io.qameta.allure.Allure.step;
import static io.qameta.allure.SeverityLevel.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Epic("API тесты для сервиса кредитных параметров")
@Feature("Ограничение скорости запросов")
@Tag("rate-limit")
public class RateLimitTests extends BaseTest {

    @Test
    @DisplayName("KRK-T22: Превышен лимит запросов для неавторизованного пользователя")
    @Description("Тест проверяет ограничение скорости для неавторизованных запросов")
    @Severity(NORMAL)
    @Story("Ограничение скорости")
    public void rateLimitForUnauthorizedUser() {
        resetAuthToken(); // Сбрасываем токен, чтобы не использовать авторизацию

        step("Отправить 10 запросов в течение минуты", () -> {
            for (int i = 0; i < 10; i++) {
                CreditService.getCreditParameters()
                        .then()
                        .statusCode(200);
            }
        });

        step("Отправить 11-й запрос", () -> {
            ErrorResponse response = CreditService.getCreditParameters()
                    .then()
                    .statusCode(429)
                    .extract()
                    .as(ErrorResponse.class);

            assertThat(response.getCode(), equalTo("ERROR"));
            assertThat(response.getMessage(), containsString(ErrorMessages.RATE_LIMIT_EXCEEDED));
        });
    }

    @Test
    @DisplayName("KRK-T5: Превышен лимит запросов для авторизованного пользователя")
    @Description("Тест проверяет ограничение скорости для авторизованных запросов")
    @Severity(NORMAL)
    @Story("Ограничение скорости")
    public void rateLimitForAuthorizedUser() {
        String token = TokenManager.getToken();

        step("Отправить 10 запросов в течение минуты", () -> {
            for (int i = 0; i < 10; i++) {
                CreditService.getCreditParameters("consumer", null, token)
                        .then()
                        .statusCode(200);
            }
        });

        step("Отправить 11-й запрос", () -> {
            ErrorResponse response = CreditService.getCreditParameters("consumer", null, token)
                    .then()
                    .statusCode(429)
                    .extract()
                    .as(ErrorResponse.class);

            assertThat(response.getCode(), equalTo("ERROR"));
            assertThat(response.getMessage(), containsString(ErrorMessages.RATE_LIMIT_EXCEEDED));
        });
    }
}