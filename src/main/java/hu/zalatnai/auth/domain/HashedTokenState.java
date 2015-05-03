package hu.zalatnai.auth.domain;

import hu.zalatnai.auth.domain.exception.InvalidRefreshTokenException;
import hu.zalatnai.sdk.service.RandomGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.Arrays;

@Service
class HashedTokenState extends TokenState {

    @Autowired
    private RandomGenerator randomGenerator;

    @Autowired
    private Clock clock;

    @Override
    public TokenState refresh(Token token, byte[] hashedRefreshToken) {
        if (!Arrays.equals(token.getRefreshToken(), hashedRefreshToken)) {
            throw new InvalidRefreshTokenException();
        }

        token.setAccessToken(randomGenerator.getBytes(32));
        token.generateTokenExpirationTime(clock, token.getAccessTokenLifetime());

        return tokenStateRepository.getById(TokenState.STATE_REFRESHED);
    }

    @Override
    public int getId() {
        return STATE_HASHED;
    }
}
