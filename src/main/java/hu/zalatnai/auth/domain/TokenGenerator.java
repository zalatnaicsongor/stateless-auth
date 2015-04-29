package hu.zalatnai.auth.domain;

import hu.zalatnai.sdk.service.RandomGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;

@Service
class TokenGenerator {

    @Autowired
    RandomGenerator randomGenerator;

    @Autowired
    Clock clock;

    @Autowired
    TransientTokenState transientTokenState;

    public Token generate(Duration defaultAccessTokenLifetime) {
        return new Token(clock, defaultAccessTokenLifetime, randomGenerator, transientTokenState);
    }
}
