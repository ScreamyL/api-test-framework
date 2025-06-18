package models.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserLoginResponse {
    @JsonProperty("login")
    private String login;

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
}