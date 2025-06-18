package tests.auth;

import base.BaseTest;
import config.Config;
import constants.ErrorMessages;
import io.qameta.allure.*;
import models.auth.PasswordChangeRequest;
import models.common.ApiResponse;
import models.common.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import services.AuthService;
import utils.TokenManager;

import static io.qameta.allure.Allure.step;
import static io.qameta.allure.SeverityLevel.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Epic("API тесты для сервиса аутентификации")
@Feature("Смена пароля пользователя")
@Tag("authentication")
public class PasswordChangeTests extends BaseTest {

    @Test
    @DisplayName("KRK-T65: Смена пароля с неверным текущим паролем")
    @Description("Тест проверяет обработку ошибки при попытке сменить пароль с неверным текущим паролем")
    @Severity(CRITICAL)
    @Story("Неудачная смена пароля")
    public void changePasswordWithInvalidCurrentPassword() {
        String token = TokenManager.getToken();
        PasswordChangeRequest request = new PasswordChangeRequest(
                "invalid_password",
                "NewValidPass123!"
        );

        ErrorResponse response = step("Отправить запрос на смену пароля с неверным текущим паролем", () ->
                AuthService.changePassword(request, token)
                        .then()
                        .statusCode(403)
                        .extract()
                        .as(ErrorResponse.class)
        );

        step("Проверить ответ об ошибке", () -> {
            assertThat(response.getCode(), equalTo("ERROR"));
            assertThat(response.getMessage(), containsString(ErrorMessages.INVALID_CURRENT_PASSWORD));
        });
    }

    @Test
    @DisplayName("KRK-T63: Успешная смена пароля")
    @Description("Тест проверяет успешную смену пароля авторизованного пользователя")
    @Severity(BLOCKER)
    @Story("Успешная смена пароля")
    public void successfulPasswordChange() {
        String token = TokenManager.getToken();
        String newPassword = "NewValidPass123!";

        step("Изменить пароль", () -> {
            PasswordChangeRequest request = new PasswordChangeRequest(
                    Config.getAuthPassword(),
                    newPassword
            );

            AuthService.changePassword(request, token)
                    .then()
                    .statusCode(200);
        });

        step("Проверить авторизацию с новым паролем", () -> {
            String newToken = AuthService.authenticate(Config.getAuthLogin(), newPassword);
            assertThat(newToken, notNullValue());
        });
    }

    @Test
    @DisplayName("KRK-T64: Смена пароля неавторизованным пользователем")
    @Description("Тест проверяет невозможность смены пароля без авторизации")
    @Severity(NORMAL)
    @Story("Неудачная смена пароля")
    public void changePasswordUnauthorized() {
        resetAuthToken(); // Сбрасываем токен
        PasswordChangeRequest request = new PasswordChangeRequest(
                "oldPassword",
                "NewValidPass123!"
        );

        step("Отправить запрос без токена авторизации", () -> {
            ErrorResponse response = AuthService.changePassword(request, "invalid_token")
                    .then()
                    .statusCode(401)
                    .extract()
                    .as(ErrorResponse.class);

            assertThat(response.getCode(), equalTo("ERROR"));
            assertThat(response.getMessage(), containsString(ErrorMessages.UNAUTHORIZED));
        });
    }

    @Test
    @DisplayName("KRK-T66: Смена пароля на невалидный новый пароль")
    @Description("Тест проверяет валидацию нового пароля при смене")
    @Severity(CRITICAL)
    @Story("Неудачная смена пароля")
    public void changePasswordToInvalidNewPassword() {
        String token = TokenManager.getToken();
        PasswordChangeRequest request = new PasswordChangeRequest(
                Config.getAuthPassword(),
                "short" // Невалидный пароль
        );

        step("Отправить запрос с невалидным новым паролем", () -> {
            ErrorResponse response = AuthService.changePassword(request, token)
                    .then()
                    .statusCode(400)
                    .extract()
                    .as(ErrorResponse.class);

            assertThat(response.getCode(), equalTo("ERROR"));
            assertThat(response.getMessage(), containsString(ErrorMessages.NEW_PASSWORD_VALIDATION_FAILED));
        });
    }

    @Test
    @DisplayName("KRK-T67: Смена пароля на пароль, совпадающий с текущим")
    @Description("Тест проверяет, что нельзя установить новый пароль, совпадающий с текущим")
    @Severity(NORMAL)
    @Story("Неудачная смена пароля")
    public void changePasswordToSameAsCurrent() {
        String token = TokenManager.getToken();
        String currentPassword = Config.getAuthPassword();
        PasswordChangeRequest request = new PasswordChangeRequest(
                currentPassword,
                currentPassword
        );

        step("Отправить запрос с новым паролем, совпадающим с текущим", () -> {
            ErrorResponse response = AuthService.changePassword(request, token)
                    .then()
                    .statusCode(400)
                    .extract()
                    .as(ErrorResponse.class);

            assertThat(response.getCode(), equalTo("ERROR"));
            assertThat(response.getMessage(), containsString(ErrorMessages.SAME_PASSWORD));
        });
    }
}