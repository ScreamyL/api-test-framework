package models.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthResponse {
    @JsonProperty("Authorization")
    private String authorization;

    public AuthResponse() {}

    public AuthResponse(String authorization) {
        this.authorization = authorization;
    }

    public String getAuthorization() { return authorization; }
    public void setAuthorization(String authorization) { this.authorization = authorization; }
}