package hu.zalatnai.auth.domain;

import hu.zalatnai.auth.domain.exception.OperationInapplicableException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class BlacklistedClientStateTest {

    @InjectMocks
    BlacklistedClientState blacklistedClientState;

    @Mock
    Client client;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetIdReturnsThatTheStatesIdIsBlacklisted() throws Exception {
        assertEquals(ClientState.STATE_BLACKLISTED, blacklistedClientState.getId());
    }

    @Test(expected = OperationInapplicableException.class)
    public void testPersistThrows() {
        blacklistedClientState.persist(client);
    }

    @Test(expected = OperationInapplicableException.class)
    public void testRefreshThrows() {
        blacklistedClientState.refreshToken(client, new byte[]{});
    }

    @Test(expected = OperationInapplicableException.class)
    public void testBlacklistThrows() {
        blacklistedClientState.blacklist(client);
    }
}