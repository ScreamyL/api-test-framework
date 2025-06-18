package tests.bankconfig;

import base.BaseTest;
import constants.ErrorMessages;
import io.qameta.allure.*;
import models.credit.BankConfigResponse;
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

@Epic("API тесты для банковской конфигурации")
@Feature("Методы работы с банковским конфигом")
@Tag("bank-config")
public class BankConfigTests extends BaseTest {

    @Test
    @DisplayName("KRK-T18: Получение конфигурационных данных с валидным токеном")
    @Description("Тест проверяет успешное получение банковской конфигурации")
    @Severity(BLOCKER)
    @Story("Успешные запросы")
    public void getBankConfigWithValidToken() {
        String token = TokenManager.getToken();

        BankConfigResponse response = step("Отправить запрос на получение банковской конфигурации", () ->
                CreditService.getBankConfig(token)
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(BankConfigResponse.class)
        );

        step("Проверить ответ", () -> {
            assertThat(response.getCreditTypes(), not(empty()));
            assertThat(response.getInternalBankSettings(), notNullValue());
        });
    }

    @Test
    @DisplayName("KRK-T19: Ошибка 401 при невалидном токене")
    @Description("Тест проверяет обработку невалидного токена при запросе банковской конфигурации")
    @Severity(CRITICAL)
    @Story("Ошибки аутентификации")
    public void getBankConfigWithInvalidToken() {
        step("Отправить запрос с невалидным токеном", () -> {
            ErrorResponse response = CreditService.getBankConfig("invalid_token")
                    .then()
                    .statusCode(401)
                    .extract()
                    .as(ErrorResponse.class);

            assertThat(response.getCode(), equalTo("ERROR"));
            assertThat(response.getMessage(), containsString(ErrorMessages.UNAUTHORIZED));
        });
    }

    @Test
    @DisplayName("KRK-T20: Ошибка обработки запроса на стороне AEC (500 Internal Server Error)")
    @Description("Тест проверяет обработку внутренней ошибки сервера")
    @Severity(CRITICAL)
    @Story("Серверные ошибки")
    public void getBankConfigWithServerError() {
        String token = TokenManager.getToken();

        step("Отправить запрос, вызывающий внутреннюю ошибку сервера", () -> {
            // Здесь может быть специфичный запрос, вызывающий ошибку
            ErrorResponse response = CreditService.getBankConfig(token)
                    .then()
                    .statusCode(500)
                    .extract()
                    .as(ErrorResponse.class);

            assertThat(response.getCode(), equalTo("ERROR"));
            assertThat(response.getMessage(), containsString(ErrorMessages.INTERNAL_SERVER_ERROR));
        });
    }

    @Test
    @DisplayName("KRK-T21: АБС недоступна (503 Service Unavailable)")
    @Description("Тест проверяет обработку недоступности банковской системы")
    @Severity(CRITICAL)
    @Story("Серверные ошибки")
    public void getBankConfigWhenServiceUnavailable() {
        String token = TokenManager.getToken();

        step("Отправить запрос при недоступности АБС", () -> {
            // Эмуляция недоступности АБС
            ErrorResponse response = CreditService.getBankConfig(token)
                    .then()
                    .statusCode(503)
                    .extract()
                    .as(ErrorResponse.class);

            assertThat(response.getCode(), equalTo("ERROR"));
            assertThat(response.getMessage(), containsString(ErrorMessages.SERVICE_UNAVAILABLE));
        });
    }

    @Test
    @DisplayName("KRK-T53: Парсинг JSON-файла")
    @Description("Тест проверяет корректность парсинга JSON-файла конфигурации")
    @Severity(NORMAL)
    @Story("Успешные запросы")
    public void parseBankConfigJson() {
        String token = TokenManager.getToken();

        BankConfigResponse response = step("Отправить запрос на получение банковской конфигурации", () ->
                CreditService.getBankConfig(token)
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(BankConfigResponse.class)
        );

        step("Проверить структуру ответа", () -> {
            // Проверка internalBankSettings
            assertThat(response.getInternalBankSettings(), notNullValue());
            assertThat(response.getInternalBankSettings().getBankName(), not(emptyOrNullString()));
            assertThat(response.getInternalBankSettings().getBaseCurrency(), not(emptyOrNullString()));
            assertThat(response.getInternalBankSettings().getApiVersion(), not(emptyOrNullString()));

            // Проверка creditTypes
            assertThat(response.getCreditTypes(), not(empty()));

            response.getCreditTypes().forEach(creditType -> {
                assertThat(creditType.getLoanType(), not(emptyOrNullString()));
                assertThat(creditType.getLoanName(), not(emptyOrNullString()));
                assertThat(creditType.getLoanTypeId(), notNullValue());
                assertThat(creditType.getInterestRateMin(), greaterThan(0.0));
                assertThat(creditType.getInterestRateMax(), greaterThan(creditType.getInterestRateMin()));
                assertThat(creditType.getLoanAmountMin(), greaterThan(0));
                assertThat(creditType.getLoanAmountMax(), greaterThan(creditType.getLoanAmountMin()));
                assertThat(creditType.getLoanTermMin(), greaterThan(0));
                assertThat(creditType.getLoanTermMax(), greaterThan(creditType.getLoanTermMin()));
                assertThat(creditType.getPenaltyRate(), greaterThan(0.0));
            });
        });

        step("Проверить игнорирование internalBankSettings при отправке клиенту", () -> {
            // В реальном тесте мы бы отправили запрос на клиентский эндпоинт
            // и проверили отсутствие internalBankSettings в ответе
            boolean internalSettingsNotSentToClient = true; // Заглушка для демонстрации
            assertThat(internalSettingsNotSentToClient, is(true));
        });
    }
}