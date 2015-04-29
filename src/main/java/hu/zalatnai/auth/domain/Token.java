package hu.zalatnai.auth.domain;

import hu.zalatnai.auth.service.infrastructure.client.TokenStateToIntegerConverter;
import hu.zalatnai.sdk.service.RandomGenerator;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Convert;
import javax.persistence.Embeddable;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

@Embeddable
public class Token {

    byte[] accessToken;

    byte[] refreshToken;

    @Convert(converter = TokenStateToIntegerConverter.class)
    TokenState tokenState;

    Instant issuedAt;

    Instant expirationTime;

    Duration accessTokenLifetime;

    Token() {
    }

    Token(@NotNull Clock clock, @NotNull Duration accessTokenLifetime, @NotNull RandomGenerator randomGenerator, TransientTokenState tokenState) {
        this.issuedAt = clock.instant();
        this.expirationTime = issuedAt.plus(accessTokenLifetime);

        this.accessTokenLifetime = accessTokenLifetime;
        this.tokenState = tokenState;

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
        tokenState = tokenState.refresh(this);
    }

    void persist() {
        tokenState = tokenState.hash(this);
    }

    void blacklist() {
        tokenState = tokenState.blacklist(this);
    }

    void setAccessToken(byte[] accessToken) {
        this.accessToken = accessToken;
    }

    void setRefreshToken(byte[] refreshToken) {
        this.refreshToken = refreshToken;
    }
}