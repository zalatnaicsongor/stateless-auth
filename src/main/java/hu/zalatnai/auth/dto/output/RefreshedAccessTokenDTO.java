package hu.zalatnai.auth.dto.output;

import java.time.Instant;

public class RefreshedAccessTokenDTO {
    private String refreshedAccessToken;
    private Instant refreshedTokenExpirationTime;
    private Instant refreshedTokenIssuedAt;

    public RefreshedAccessTokenDTO(String refreshedAccessToken, Instant refreshedTokenExpirationTime, Instant refreshedTokenIssuedAt) {
        this.refreshedAccessToken = refreshedAccessToken;
        this.refreshedTokenExpirationTime = refreshedTokenExpirationTime;
        this.refreshedTokenIssuedAt = refreshedTokenIssuedAt;
    }

    public String getRefreshedAccessToken() {
        return refreshedAccessToken;
    }

    public Instant getRefreshedTokenExpirationTime() {
        return refreshedTokenExpirationTime;
    }

    public Instant getRefreshedTokenIssuedAt() {
        return refreshedTokenIssuedAt;
    }
}
