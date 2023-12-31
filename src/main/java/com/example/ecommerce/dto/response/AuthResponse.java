package com.example.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String message;

    @JsonProperty("data")
    private Object data;
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthTokens {
        @JsonProperty("access_token")
        private String accessToken;
        @JsonProperty("refresh_token")
        private String refreshToken;
        @JsonProperty("access_token_expires_in")
        private long expiresAccessTokenIn;
        @JsonProperty("refresh_token_expires_in")
        private long expiresRefreshTokenIn;
    }
}
