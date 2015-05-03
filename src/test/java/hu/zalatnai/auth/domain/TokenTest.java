package hu.zalatnai.auth.domain;

import hu.zalatnai.auth.domain.exception.StateAlreadyResolvedException;
import hu.zalatnai.sdk.service.RandomGenerator;
import hu.zalatnai.sdk.service.domain.StateRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TokenTest {

    @Mock
    Clock clock;

    Duration accessTokenLifetime = Duration.ofDays(1);

    @Mock
    RandomGenerator randomGenerator;

    @Mock
    UnhashedTokenState initialTokenState;

    @Mock
    TokenState refreshResponse;

    @Mock
    TokenState resolveResponse;

    @Mock
    TokenState hashResponse;

    @Mock
    StateRepository<TokenState> stateResolver;

    Instant currentTime = Instant.ofEpochSecond(42);

    byte[] accessToken = {'a', 'b'};
    byte[] refreshToken = {'c', 'd'};
    byte[] hashedRefreshToken = {'k', 'x'};

    //SUT
    Token token;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(clock.instant()).thenReturn(currentTime);
        when(randomGenerator.getBytes(32)).thenReturn(accessToken, refreshToken);

        token = new Token(clock, accessTokenLifetime, randomGenerator, initialTokenState);

        when(initialTokenState.refresh(eq(token), AdditionalMatchers.aryEq(hashedRefreshToken))).thenReturn(
                refreshResponse);

        when(initialTokenState.hash(token)).thenReturn(
                hashResponse);

        when(refreshResponse.getId()).thenReturn(42);

        when(hashResponse.getId()).thenReturn(98);

        when(resolveResponse.getId()).thenReturn(66);
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

    @Test
    public void testRefreshDelegatesTheCallToTheInternalStateMachine() {
        token.refresh(hashedRefreshToken);

        verify(initialTokenState).refresh(token, hashedRefreshToken);
    }

    @Test
    public void testRefreshSetsTheStateReturnedByTheActualStateAsTheNewStateOfTheToken() {
        token.refresh(hashedRefreshToken);

        assertEquals(42, token.getTokenStatus());
    }

    @Test
    public void testHashDelegatesTheCallToTheInternalStateMachine() {
        token.hash();

        verify(initialTokenState).hash(token);
    }

    @Test
    public void testHashSetsTheStateReturnedByTheActualStateAsTheNewStateOfTheToken() {
        token.hash();

        assertEquals(98, token.getTokenStatus());
    }

    @Test(expected = StateAlreadyResolvedException.class)
    public void testResolveStateFromStatusThrowsIfTheStateHasAlreadyBeenResolved() {
        token.resolveStateFromStatus(stateResolver);
    }

    @Test
    public void testResolveStateFromStatusResolvesTheStateFromTheStatusUsingTheSuppliedStateResolver() throws NoSuchFieldException, IllegalAccessException {
        when(stateResolver.getById(45)).thenReturn(resolveResponse);

        Token token = new Token();

        Field tokenStatusFiled = token.getClass().getDeclaredField("tokenStatus");
        tokenStatusFiled.setAccessible(true);
        tokenStatusFiled.setInt(token, 45);

        token.resolveStateFromStatus(stateResolver);

        verify(stateResolver).getById(45);
        assertEquals(token.getTokenStatus(), 66);
    }
}