package hu.zalatnai.auth.domain;

import hu.zalatnai.sdk.service.RandomGenerator;
import org.jetbrains.annotations.NotNull;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

public class Token {

    byte[] accessToken;

    byte[] refreshToken;

    TokenState state;

    Instant issuedAt;

    Instant expirationTime;

    Duration accessTokenLifetime;

    Token() {
    }

    Token(@NotNull Clock clock, @NotNull Duration accessTokenLifetime, @NotNull RandomGenerator randomGenerator, TransientTokenState tokenState) {
        this.issuedAt = clock.instant();
        expirationTime = issuedAt.plus(accessTokenLifetime);
        this.accessTokenLifetime = accessTokenLifetime;

        this.state = tokenState;

        this.accessToken = randomGenerator.getBytes(32);
        this.refreshToken = randomGenerator.getBytes(32);
    }

    @NotNull
    public byte[] getAccessToken() {
        return accessToken.clone();
    }

    @NotNull
    public byte[] getRefreshToken() {
        return refreshToken.clone();
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }

    public Instant getExpirationTime() {
        return expirationTime;
    }

    void refresh() {

    }

    void persist() {

    }

    void blacklist() {

    }
}
