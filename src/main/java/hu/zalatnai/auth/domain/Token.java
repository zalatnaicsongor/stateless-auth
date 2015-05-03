package hu.zalatnai.auth.domain;

import hu.zalatnai.auth.domain.exception.StateAlreadyResolvedException;
import hu.zalatnai.sdk.service.RandomGenerator;
import hu.zalatnai.sdk.service.domain.StateRepository;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

@Embeddable
class Token {

    private byte[] accessToken;

    private byte[] refreshToken;

    @Transient
    private TokenState tokenState;

    private int tokenStatus;

    private Instant issuedAt;

    private Instant expirationTime;

    private Duration accessTokenLifetime;

    Token() {
    }

    Token(@NotNull Clock clock, @NotNull Duration accessTokenLifetime, @NotNull RandomGenerator randomGenerator, UnhashedTokenState tokenState) {
        this.generateTokenExpirationTime(clock, accessTokenLifetime);

        this.accessTokenLifetime = accessTokenLifetime;
        this.tokenState = tokenState;

        this.accessToken = randomGenerator.getBytes(32);
        this.refreshToken = randomGenerator.getBytes(32);
    }

    Duration getAccessTokenLifetime() {
        return accessTokenLifetime;
    }

    void generateTokenExpirationTime(Clock clock, Duration accessTokenLifetime) {
        this.issuedAt = clock.instant();
        this.expirationTime = issuedAt.plus(accessTokenLifetime);
    }

    @NotNull
    byte[] getAccessToken() {
        return accessToken.clone();
    }

    @NotNull
    byte[] getRefreshToken() {
        return refreshToken.clone();
    }

    Instant getIssuedAt() {
        return issuedAt;
    }

    Instant getExpirationTime() {
        return expirationTime;
    }

    void refresh(byte[] hashedRefreshToken) {
        setTokenState(tokenState.refresh(this, hashedRefreshToken));
    }

    void hash() {
        setTokenState(tokenState.hash(this));
    }

    void setAccessToken(byte[] accessToken) {
        this.accessToken = accessToken;
    }

    void setRefreshToken(byte[] refreshToken) {
        this.refreshToken = refreshToken;
    }

    private void setTokenState(TokenState tokenState) {
        this.tokenState = tokenState;
        this.tokenStatus = tokenState.getId();
    }

    void resolveStateFromStatus(StateRepository<TokenState> tokenStateRepository) {
        if (tokenState != null) {
            throw new StateAlreadyResolvedException();
        }
        setTokenState(tokenStateRepository.getById(tokenStatus));
    }

    int getTokenStatus() {
        return this.tokenStatus;
    }
}
