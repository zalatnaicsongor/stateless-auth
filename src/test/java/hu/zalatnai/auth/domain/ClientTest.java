package hu.zalatnai.auth.domain;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ClientTest {

    @Mock
    private Application application;

    @Mock
    private Application futureApplication;

    @Mock
    private Clock clock;

    @Mock
    private Clock futureClock;

    @Mock
    private TokenGenerator tokenGenerator;

    @Mock
    private Token token;

    private byte[] tokenValue = {'p', 'b', 'r', '5', 'x', 'e'};

    private final Duration applicationDuration = Duration.ofDays(1);

    private final Duration newApplicationDuration = Duration.ofDays(2);

    private final Instant now = Instant.ofEpochSecond(42);

    private final Instant future = Instant.ofEpochSecond(45);

    Client client;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        when(application.getDefaultTokenLifetime()).thenReturn(applicationDuration);
        when(application.getId()).thenReturn("applicationId");
        when(clock.instant()).thenReturn(now);
        when(tokenGenerator.generate()).thenReturn(token);

        when(token.getAccessToken()).thenReturn(tokenValue);

        when(futureApplication.getId()).thenReturn("newApplicationId");
        when(futureApplication.getDefaultTokenLifetime()).thenReturn(newApplicationDuration);
        when(futureClock.instant()).thenReturn(future);

        client = new Client(application, "deviceUuid", "deviceName", clock, tokenGenerator);
    }


    @Test(expected = IllegalArgumentException.class)
    public void itCannotBeConstructedIfTheDeviceUuidIsBlank() {
        new Client(application, "", "deviceName", clock, tokenGenerator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void itCannotBeConstructedIfTheDeviceNameIsBlank() {
        new Client(application, "deviceUuid", "", clock, tokenGenerator);

    }

    @Test
    public void itUsesTheCurrentTimeAsTheIssuanceTimeOfTheClient() {
        verify(clock).instant();

        assertEquals(clock.instant(), client.getTokenIssuedAt());
    }

    @Test
    public void itGenerates32BytesOfRandomAsTheToken() {
        verify(tokenGenerator).generate();

        assertArrayEquals(tokenValue, client.getToken());
    }
    @Test
    public void itReturnsTheSumOfTheApplicationsExpiryDurationAndTheIssuanceTimeOfTheClientAsTheExpiryTime() {
        assertEquals(clock.instant().plus(applicationDuration), client.getTokenExpirationTime());
    }

    @Test
    public void itStoresTheSuppliedDeviceName() {
        assertEquals("deviceName", client.getDeviceName());
    }

    @Test
    public void itStoresTheSuppliedDeviceUuid() {
        assertEquals("deviceUuid", client.getDeviceUuid());
    }

    //region refresh
    @Test(expected = IllegalArgumentException.class)
    public void itThrowsIfTheSuppliedApplicationIsNullWhenRefreshingTheClient() {
        client.refresh(null, clock, tokenGenerator);
    }

    @Test
    public void itMakesTheClientAnInstanceOfTheNewlySuppliedApplicationWhenRefreshingTheClient() {
        client.refresh(futureApplication, futureClock, tokenGenerator);

        assertEquals("newApplicationId", client.getApplicationId());
    }

    @Test
    public void itSetsTheIssuanceTimeToTheTimeOfTheRefreshingWhenRefreshingTheClient() {
        client.refresh(futureApplication, futureClock, tokenGenerator);

        verify(futureClock).instant();

        assertEquals(future, client.getTokenIssuedAt());
    }

    @Test
    public void itSetsTheExpiryTimeOfTheRefreshedClientToTheSumOfTheNewIssuanceTimeAndTheNewApplicationsDefaultExpiryDuration() {
        client.refresh(futureApplication, futureClock, tokenGenerator);

        assertEquals(future.plus(newApplicationDuration), client.getTokenExpirationTime());
    }

    @Test
    public void itGeneratesANewTokenOf32RandomBytesWhenRefreshingTheClient() {
        byte[] newTokenValue = {'x', 'e', '5', 'a', 'c', '8', 'p', '1'};
        TokenGenerator newTokenGenerator = Mockito.mock(TokenGenerator.class);

        Token newToken = new Token(newTokenValue);

        when(newTokenGenerator.generate()).thenReturn(newToken);

        client.refresh(application, futureClock, newTokenGenerator);

        verify(newTokenGenerator).generate();
        assertArrayEquals(newTokenValue, client.getToken());
    }
    //endregion
}