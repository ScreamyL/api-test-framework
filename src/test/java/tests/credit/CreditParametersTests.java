package tests.credit;

import base.BaseTest;
import constants.ApiEndpoints;
import constants.ErrorMessages;
import io.qameta.allure.*;
import models.credit.CreditParametersResponse;
import models.common.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import services.CreditService;
import utils.TokenManager;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static io.qameta.allure.SeverityLevel.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Epic("API тесты для сервиса кредитных параметров")
@Feature("Получение параметров кредитования")
@Tag("credit")
public class CreditParametersTests extends BaseTest {

    @Test
    @DisplayName("KRK-T8: Получение всех параметров по типу кредита 'Кредитная карта'")
    @Description("Тест проверяет получение всех параметров для кредитной карты")
    @Severity(BLOCKER)
    @Story("Успешное получение кредитных параметров")
    public void getCreditCardParameters() {
        String token = TokenManager.getToken();

        List<CreditParametersResponse> response = step("Отправить запрос на получение параметров для кредитной карты", () ->
                CreditService.getCreditParameters("credit_card", null, token)
                        .then()
                        .statusCode(200)
                        .extract()
                        .jsonPath()
                        .getList(".", CreditParametersResponse.class)
        );

        step("Проверить ответ", () -> {
            assertThat(response, not(empty()));
            assertThat(response.get(0).getLoanType(), equalTo("credit_card"));
            assertThat(response.get(0).getLoanAmountMax(), greaterThan(0));
        });
    }

    @Test
    @DisplayName("KRK-T10: Ошибка аутентификации 401 Unauthorized")
    @Description("Тест проверяет, что неавторизованный пользователь не может получить кредитные параметры")
    @Severity(CRITICAL)
    @Story("Ошибки доступа")
    public void getCreditParametersUnauthorized() {
        resetAuthToken(); // Сбрасываем токен

        step("Отправить запрос без токена", () -> {
            ErrorResponse response = CreditService.getCreditParameters("consumer", null, "invalid_token")
                    .then()
                    .statusCode(401)
                    .extract()
                    .as(ErrorResponse.class);

            assertThat(response.getCode(), equalTo("ERROR"));
            assertThat(response.getMessage(), containsString(ErrorMessages.UNAUTHORIZED));
        });
    }

    @Test
    @DisplayName("KRK-T4: Ошибка 400 Bad Request при запросе с несуществующим параметром")
    @Description("Тест проверяет обработку несуществующего параметра в запросе")
    @Severity(NORMAL)
    @Story("Обработка ошибок")
    public void getCreditParametersWithInvalidField() {
        String token = TokenManager.getToken();

        step("Отправить запрос с несуществующим полем", () -> {
            ErrorResponse response = CreditService.getCreditParameters("consumer", "invalid_field", token)
                    .then()
                    .statusCode(400)
                    .extract()
                    .as(ErrorResponse.class);

            assertThat(response.getCode(), equalTo("ERROR"));
            assertThat(response.getMessage(), containsString(ErrorMessages.INVALID_REQUEST_PARAMS));
        });
    }

    @Test
    @DisplayName("KRK-T1: Получение всех параметров по всем кредитам")
    @Description("Тест проверяет получение всех кредитных параметров")
    @Severity(BLOCKER)
    @Story("Успешное получение кредитных параметров")
    public void getAllCreditParameters() {
        String token = TokenManager.getToken();

        List<CreditParametersResponse> response = step("Отправить запрос на получение всех параметров", () ->
                CreditService.getCreditParameters(null, null, token)
                        .then()
                        .statusCode(200)
                        .extract()
                        .jsonPath()
                        .getList(".", CreditParametersResponse.class)
        );

        step("Проверить ответ", () -> {
            assertThat(response, not(empty()));
            assertThat(response.size(), greaterThanOrEqualTo(4)); // consumer, mortgage, auto, credit_card
        });
    }
}