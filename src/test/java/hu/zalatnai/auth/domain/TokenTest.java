package hu.zalatnai.auth.domain;

import hu.zalatnai.sdk.service.RandomGenerator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TokenTest {

    @Mock
    Clock clock;

    Duration accessTokenLifetime = Duration.ofDays(1);

    @Mock
    RandomGenerator randomGenerator;

    @Mock
    UnhashedTokenState unhashedTokenState;

    Instant currentTime = Instant.ofEpochSecond(42);

    byte[] accessToken = {'a', 'b'};
    byte[] refreshToken = {'c', 'd'};

    //SUT
    Token token;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(clock.instant()).thenReturn(currentTime);
        when(randomGenerator.getBytes(32)).thenReturn(accessToken, refreshToken);

        token = new Token(clock, accessTokenLifetime, randomGenerator, unhashedTokenState);
    }

    @Test
    public void itSetsTheTimeOfIssuanceToTheCurrentTime() {
        verify(clock).instant();

        assertEquals(currentTime, token.getIssuedAt());
    }

    @Test
    public void itSetsTheExpirationTimeToTheSumOfTheTimeOfIssuanceAndTheAccessTokenLifetime() {
        assertEquals(currentTime.plus(accessTokenLifetime), token.getExpirationTime());
    }

    @Test
    public void itGenerates32RandomBytesAsTheAccessTokenAndTheRefreshToken() {
        verify(randomGenerator, times(2)).getBytes(32);
        assertArrayEquals(accessToken, token.getAccessToken());
        assertArrayEquals(refreshToken, token.getRefreshToken());
    }

    public void testRefreshDelegatesTheCallToTheInternalStateMachine() {

    }
}