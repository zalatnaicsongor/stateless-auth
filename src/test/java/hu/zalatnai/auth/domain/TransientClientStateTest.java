package hu.zalatnai.auth.domain;

import hu.zalatnai.sdk.service.UUIDGenerator;
import hu.zalatnai.sdk.service.domain.StateRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransientClientStateTest {

    @Mock
    StateRepository<ClientState> clientStateRepository;

    @Mock
    PersistentClientState persistentClientState;

    @Mock
    UUIDGenerator uuidGenerator;

    @Mock
    Client client;

    @Mock
    Token token;

    UUID uuid;

    //SUT
    @InjectMocks
    TransientClientState transientClientState;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        uuid = UUID.randomUUID();

        when(client.getToken()).thenReturn(token);
        when(clientStateRepository.getById(ClientState.STATE_PERSISTENT)).thenReturn(persistentClientState);
        when(uuidGenerator.generateV4()).thenReturn(uuid);
    }

    @Test
    public void testGetIdReturnsTransientAsTheId() throws Exception {
        assertEquals(ClientState.STATE_TRANSIENT, transientClientState.getId());
    }

    @Test
    public void testPersistGeneratesAUUID() {
        transientClientState.persist(client);
        verify(uuidGenerator).generateV4();
    }

    @Test
    public void testPersistAssignsTheClientTheGeneratedUUID() {
        transientClientState.persist(client);
        verify(client).assignClientUUID(uuid);
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
}