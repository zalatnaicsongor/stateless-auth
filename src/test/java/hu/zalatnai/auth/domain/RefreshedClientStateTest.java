package hu.zalatnai.auth.domain;

import hu.zalatnai.sdk.service.domain.StateRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RefreshedClientStateTest {

    @Mock
    StateRepository<ClientState> clientStateRepository;

    @Mock
    PersistentClientState persistentClientState;

    @Mock
    Client client;

    @Mock
    Token token;

    //SUT
    @InjectMocks
    RefreshedClientState refreshedClientState;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        when(client.getToken()).thenReturn(token);
        when(clientStateRepository.getById(ClientState.STATE_PERSISTENT)).thenReturn(persistentClientState);
    }

    @Test
    public void testGetIdReturnsRefreshedAsTheId() throws Exception {
        assertEquals(ClientState.STATE_REFRESHED, refreshedClientState.getId());
    }

    @Test
    public void testPersistDelegatesTheCallToTheToken() {
        refreshedClientState.persist(client);

        verify(token).hash();
    }

    @Test
    public void testPersistReturnsAPersistentClientState() throws Exception {
        ClientState retval = refreshedClientState.persist(client);

        verify(clientStateRepository).getById(ClientState.STATE_PERSISTENT);

        assertTrue(retval instanceof PersistentClientState);
    }
}