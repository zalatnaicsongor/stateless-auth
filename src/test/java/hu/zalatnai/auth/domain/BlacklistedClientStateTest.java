package hu.zalatnai.auth.domain;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class BlacklistedClientStateTest {

    @InjectMocks
    BlacklistedClientState blacklistedClientState;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetIdReturnsThatTheStatesIdIsBlacklisted() throws Exception {
        assertEquals(ClientState.STATE_BLACKLISTED, blacklistedClientState.getId());
    }
}