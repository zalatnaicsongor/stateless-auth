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

public class TransientClientStateTest {

    @Mock
    StateRepository<ClientState> clientStateRepository;

    @Mock
    PersistentClientState persistentClientState;

    @Mock
    Client client;

    @Mock
    Token token;

    byte[] accessToken = {'a', 't'};

    byte[] refreshToken = {'r', 't'};

    //SUT
    @InjectMocks
    TransientClientState transientClientState;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        when(client.getToken()).thenReturn(token);
        when(clientStateRepository.getById(ClientState.STATE_PERSISTENT)).thenReturn(persistentClientState);

        when(token.getAccessToken()).thenReturn(accessToken);
        when(token.getRefreshToken()).thenReturn(refreshToken);
    }

    @Test
    public void testGetIdReturnsTransientAsTheId() throws Exception {
        assertEquals(ClientState.STATE_TRANSIENT, transientClientState.getId());
    }

    @Test
    public void testPersistDelegatesTheCallToTheToken() {
        transientClientState.persist(client);

        verify(token).hash();
    }

    @Test
    public void testPersistReturnsAPersistentClientState() throws Exception {
        ClientState retval = transientClientState.persist(client);

        verify(clientStateRepository).getById(ClientState.STATE_PERSISTENT);

        assertTrue(retval instanceof PersistentClientState);
    }

    @Test
    public void testGetAccessTokenDelegatesTheCallToTheToken() {
        transientClientState.getAccessToken(client);

        verify(token).getAccessToken();
    }

    @Test
    public void testGetAccessTokenReturnsTheAccessTokenOfTheToken() {
        assertArrayEquals(accessToken, transientClientState.getAccessToken(client));
    }

    @Test
    public void testGetRefreshTokenDelegatesTheCallToTheToken() {
        transientClientState.getRefreshToken(client);

        verify(token).getRefreshToken();
    }

    @Test
    public void testGetRefreshTokenReturnsTheRefreshTokenOfTheToken() {
        assertArrayEquals(refreshToken, transientClientState.getRefreshToken(client));
    }
}