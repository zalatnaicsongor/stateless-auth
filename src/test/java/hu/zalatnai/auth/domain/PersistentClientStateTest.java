package hu.zalatnai.auth.domain;

import hu.zalatnai.sdk.service.domain.StateRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PersistentClientStateTest {

    @Mock
    StateRepository<ClientState> clientStateRepository;

    @Mock
    RefreshedClientState refreshedClientState;

    @Mock
    BlacklistedClientState blacklistedClientState;

    @Mock
    Client client;

    @Mock
    Token token;

    //SUT
    @InjectMocks
    PersistentClientState persistentClientState;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(client.getToken()).thenReturn(token);
        when(clientStateRepository.getById(ClientState.STATE_REFRESHED)).thenReturn(refreshedClientState);
        when(clientStateRepository.getById(ClientState.STATE_BLACKLISTED)).thenReturn(blacklistedClientState);
    }

    @Test
    public void testGetIdReturnsPersistent() throws Exception {
        assertEquals(ClientState.STATE_PERSISTENT, persistentClientState.getId());
    }

    @Test
    public void testRefreshTokenDelegatesTheCallToTheToken() {
        byte[] refreshToken = {'a', 'b'};

        persistentClientState.refreshToken(client, refreshToken);
        verify(token).refresh(refreshToken);
    }

    @Test
    public void testRefreshTokenReturnsRefreshedStateAsTheNextState() {
        ClientState returnedState = persistentClientState.refreshToken(client, new byte[]{'a'});

        assertTrue(returnedState instanceof RefreshedClientState);
    }

    @Test
    public void testBlacklistReturnsTheBlacklistedStateAsTheNextState() {
        ClientState returnedState = persistentClientState.blacklist(client);

        assertTrue(returnedState instanceof BlacklistedClientState);
    }
}